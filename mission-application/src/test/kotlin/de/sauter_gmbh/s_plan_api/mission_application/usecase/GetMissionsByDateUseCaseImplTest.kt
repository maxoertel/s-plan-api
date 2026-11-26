package de.sauter_gmbh.s_plan_api.mission_application.usecase

import de.sauter_gmbh.s_plan_api.application_security.provider.ActorProvider
import de.sauter_gmbh.s_plan_api.domain_identity.Actor
import de.sauter_gmbh.s_plan_api.domain_identity.Role
import de.sauter_gmbh.s_plan_api.domain_identity.UserId
import de.sauter_gmbh.s_plan_api.domain_identity.VehicleId
import de.sauter_gmbh.s_plan_api.mission_domain.model.Customer
import de.sauter_gmbh.s_plan_api.mission_domain.model.FactorySummary
import de.sauter_gmbh.s_plan_api.mission_domain.model.Mission
import de.sauter_gmbh.s_plan_api.mission_domain.model.MissionId
import de.sauter_gmbh.s_plan_api.mission_domain.model.Signature
import de.sauter_gmbh.s_plan_api.mission_domain.model.Status
import de.sauter_gmbh.s_plan_api.mission_domain.repository.MissionRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import java.time.LocalDate
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

class GetMissionsByDateUseCaseImplTest {

  private val repository = mockk<MissionRepository>()
  private val actorProvider = mockk<ActorProvider>()

  private val sut by lazy { GetMissionsByDateUseCaseImpl(repository, actorProvider) }

  private val date = LocalDate.of(2026, 4, 16)
  private val admin = Actor.User(UserId("admin1"), Role.ADMIN)

  @Test
  fun `should return missions for the date when actor is admin user`() {
    // given:
    val missions = listOf(testMission())
    every { actorProvider.get() } returns admin
    every { repository.findByDate(date) } returns missions

    // when:
    val result = sut(date)

    // then:
    assertTrue(result.isSuccess)
    assertEquals(missions, result.getOrNull())
    verify(exactly = 1) { repository.findByDate(date) }
  }

  @Test
  fun `should return SecurityException when actor is viewer user`() {
    // given:
    every { actorProvider.get() } returns Actor.User(UserId("user1"), Role.VIEWER)

    // when:
    val result = sut(date)

    // then:
    assertTrue(result.isFailure)
    assertIs<SecurityException>(result.exceptionOrNull())
    verify(exactly = 0) { repository.findByDate(any()) }
  }

  @Test
  fun `should return failure when repository throws`() {
    // given:
    every { actorProvider.get() } returns admin
    every { repository.findByDate(date) } throws RuntimeException("db error")

    // when:
    val result = sut(date)

    // then:
    assertTrue(result.isFailure)
    assertIs<RuntimeException>(result.exceptionOrNull())
  }

  private fun testMission() =
    Mission(
      id = MissionId("100_200_1_20260416"),
      orderNr = 200,
      loadNr = 1,
      deliveryNr = 1,
      deliveryNrExternal = null,
      date = date,
      orderType = null,
      deliveryType = null,
      articleNr = null,
      recipe = null,
      plannedQuantity = null,
      actualQuantity = null,
      loadQuantity = null,
      note1 = null,
      note2 = null,
      vehicleId = VehicleId(9013L),
      vehicleLicensePlate = null,
      driver = null,
      time = null,
      dispatchTime = null,
      deliveryTime = null,
      nextTime = null,
      factorySummary =
        FactorySummary(
          id = 100L,
          name = null,
          latitude = null,
          longitude = null,
          radiusInMeter = null,
          street = null,
          houseNr = null,
          city = null,
          zip = null,
          country = null,
          phone = null,
        ),
      siteSummary = null,
      customer =
        Customer(
          nr = null,
          matchCode = null,
          name1 = null,
          name2 = null,
          name3 = null,
          name4 = null,
          street = null,
          houseNr = null,
          country = null,
          zip = null,
          city = null,
          phone = null,
          fax = null,
          mobile = null,
          email1 = null,
          email2 = null,
          note = null,
        ),
      status =
        Status(
          id = null,
          label = null,
          color = null,
          loadingInProgressTimestamp = null,
          loadingCompletedTimestamp = null,
          enRouteToSiteTimestamp = null,
          onSiteTimestamp = null,
          unloadingStartedTimestamp = null,
          unloadingCompletedTimestamp = null,
          returningTimestamp = null,
          returnedTimestamp = null,
        ),
      signature = Signature(),
    )
}
