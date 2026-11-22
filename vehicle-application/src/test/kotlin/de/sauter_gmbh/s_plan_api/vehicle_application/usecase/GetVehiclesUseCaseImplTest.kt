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
import io.mockk.verify
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

class GetVehiclesUseCaseImplTest {

  private val repository = mockk<VehicleRepository>()
  private val actorProvider = mockk<ActorProvider>()

  private val sut by lazy { GetVehiclesUseCaseImpl(repository, actorProvider) }

  @Test
  fun `should return success with vehicles when user is admin`() {
    // given:
    val vehicles =
      listOf(
        Vehicle(
          id = VehicleId(1L),
          factoryId = 100L,
          factoryName = "Werk",
          licensePlate = "B-AB 1234",
          latitude = 52.5,
          longitude = 13.4,
          trackingId = "t1",
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
        ),
      )
    every { actorProvider.get() } returns Actor.User(UserId("admin1"), Role.ADMIN)
    every { repository.findAll() } returns vehicles

    // when:
    val result = sut()

    // then:
    assertTrue(result.isSuccess)
    assertEquals(vehicles, result.getOrNull())
    verify(exactly = 1) { repository.findAll() }
  }

  @Test
  fun `should return failure with SecurityException when user is not admin`() {
    // given:
    every { actorProvider.get() } returns Actor.User(UserId("user1"), Role.VIEWER)

    // when:
    val result = sut()

    // then:
    assertTrue(result.isFailure)
    assertIs<SecurityException>(result.exceptionOrNull())
    verify(exactly = 0) { repository.findAll() }
  }

  @Test
  fun `should return failure when repository throws exception`() {
    // given:
    every { actorProvider.get() } returns Actor.User(UserId("admin1"), Role.ADMIN)
    every { repository.findAll() } throws RuntimeException("db error")

    // when:
    val result = sut()

    // then:
    assertTrue(result.isFailure)
    assertEquals("db error", result.exceptionOrNull()?.message)
  }
}
