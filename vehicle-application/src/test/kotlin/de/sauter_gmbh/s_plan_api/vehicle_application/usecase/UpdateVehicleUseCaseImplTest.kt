package de.sauter_gmbh.s_plan_api.vehicle_application.usecase

import de.sauter_gmbh.s_plan_api.application_security.provider.ActorProvider
import de.sauter_gmbh.s_plan_api.domain_identity.Actor
import de.sauter_gmbh.s_plan_api.domain_identity.Role
import de.sauter_gmbh.s_plan_api.domain_identity.UserId
import de.sauter_gmbh.s_plan_api.domain_identity.VehicleId
import de.sauter_gmbh.s_plan_api.vehicle_domain.model.Vehicle
import de.sauter_gmbh.s_plan_api.vehicle_domain.repository.VehicleRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

class UpdateVehicleUseCaseImplTest {

  private val vehicleRepository = mockk<VehicleRepository>()
  private val actorProvider = mockk<ActorProvider>()

  private val sut by lazy { UpdateVehicleUseCaseImpl(vehicleRepository, actorProvider) }

  private val admin = Actor.User(UserId("admin1"), Role.ADMIN)
  private val vehicleId = VehicleId(1L)

  @Test
  fun `should apply patch to stored vehicle when actor is admin user`() {
    // given:
    val patchedSlot = slot<Vehicle>()
    every { actorProvider.get() } returns admin
    every { vehicleRepository.findById(vehicleId) } returns testVehicle()
    every { vehicleRepository.update(capture(patchedSlot), "admin1") } answers
      {
        patchedSlot.captured
      }

    // when:
    val result = sut(vehicleId) { it.copy(licensePlate = "M-XY 99", loadCapacity = 8.0) }

    // then:
    assertTrue(result.isSuccess)
    assertEquals("M-XY 99", patchedSlot.captured.licensePlate)
    assertEquals(8.0, patchedSlot.captured.loadCapacity)
    assertEquals(2, patchedSlot.captured.vehicleType)
  }

  @Test
  fun `should keep vehicle id when patch tries to change it`() {
    // given:
    val patchedSlot = slot<Vehicle>()
    every { actorProvider.get() } returns admin
    every { vehicleRepository.findById(vehicleId) } returns testVehicle()
    every { vehicleRepository.update(capture(patchedSlot), "admin1") } answers
      {
        patchedSlot.captured
      }

    // when:
    val result = sut(vehicleId) { it.copy(id = VehicleId(99L)) }

    // then:
    assertTrue(result.isSuccess)
    assertEquals(vehicleId, patchedSlot.captured.id)
  }

  @Test
  fun `should return NoSuchElementException when vehicle does not exist`() {
    // given:
    every { actorProvider.get() } returns admin
    every { vehicleRepository.findById(vehicleId) } returns null

    // when:
    val result = sut(vehicleId) { it }

    // then:
    assertIs<NoSuchElementException>(result.exceptionOrNull())
    verify(exactly = 0) { vehicleRepository.update(any(), any()) }
  }

  @Test
  fun `should return SecurityException when user is not admin`() {
    // given:
    every { actorProvider.get() } returns Actor.User(UserId("user1"), Role.VIEWER)

    // when:
    val result = sut(vehicleId) { it }

    // then:
    assertIs<SecurityException>(result.exceptionOrNull())
    verify(exactly = 0) { vehicleRepository.update(any(), any()) }
  }

  private fun testVehicle() =
    Vehicle(
      id = vehicleId,
      factoryId = 100L,
      factoryName = "Werk",
      licensePlate = "B-AB 1234",
      latitude = 52.5,
      longitude = 13.4,
      trackingId = "t0",
      alias = null,
      driverName = "Max",
      lastSignalTimestamp = 1000L,
      lastStatusId = 3,
      vehicleType = 2,
      fleetGroupNr = 5,
      fleetGroupName = "Gruppe A",
      loadCapacity = 3.5,
      createdAt = null,
      updatedAt = null,
    )
}
