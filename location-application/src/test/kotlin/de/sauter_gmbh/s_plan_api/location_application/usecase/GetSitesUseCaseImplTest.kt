package de.sauter_gmbh.s_plan_api.location_application.usecase

import de.sauter_gmbh.s_plan_api.application_security.provider.ActorProvider
import de.sauter_gmbh.s_plan_api.domain_identity.Actor
import de.sauter_gmbh.s_plan_api.domain_identity.Role
import de.sauter_gmbh.s_plan_api.domain_identity.SiteKey
import de.sauter_gmbh.s_plan_api.domain_identity.UserId
import de.sauter_gmbh.s_plan_api.location_domain.model.Site
import de.sauter_gmbh.s_plan_api.location_domain.repository.SiteRepository
import de.sauter_gmbh.s_plan_api.mission_domain.repository.MissionRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import java.time.LocalDate
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

class GetSitesUseCaseImplTest {

  private val repository = mockk<SiteRepository>()
  private val missionRepository = mockk<MissionRepository>()
  private val actorProvider = mockk<ActorProvider>()

  private val sut by lazy { GetSitesUseCaseImpl(repository, missionRepository, actorProvider) }

  private val sites =
    listOf(
      Site(
        customerNr = 1L,
        siteNr = 5L,
        matchCode = null,
        description1 = "Bau 1",
        description2 = null,
        description3 = null,
        description4 = null,
        street = "Strasse",
        houseNumber = "1",
        zip = "10115",
        city = "Berlin",
        country = "DE",
        phone = null,
        mobile = null,
        fax = null,
        contact = null,
        note = null,
        internalInfo = null,
        deliveryNoteInfo = null,
        latitude = 52.5,
        longitude = 13.4,
        radiusInMeter = 50,
        factoryNr = 3L,
        externalId = null,
        isActive = true,
        isCompleted = false,
        isDeleted = false,
        releaseFrom = null,
        releaseUntil = null,
        firstDelivery = null,
        lastDelivery = null,
        createdAt = null,
        updatedAt = null,
      )
    )

  @Test
  fun `should return all sites when no date and user is admin`() {
    // given:
    every { actorProvider.get() } returns Actor.User(UserId("admin1"), Role.ADMIN)
    every { repository.findAll() } returns sites

    // when:
    val result = sut(date = null)

    // then:
    assertTrue(result.isSuccess)
    assertEquals(sites, result.getOrNull())
    verify(exactly = 1) { repository.findAll() }
    verify(exactly = 0) { repository.findBySiteKeys(any()) }
    verify(exactly = 0) { missionRepository.findSiteKeysWithMissionsOnDate(any()) }
  }

  @Test
  fun `should filter sites by date when date is provided`() {
    // given:
    val date = LocalDate.of(2026, 5, 6)
    val siteKeys =
      setOf(SiteKey(customerNr = 1L, siteNr = 5L), SiteKey(customerNr = 2L, siteNr = 7L))
    every { actorProvider.get() } returns Actor.User(UserId("admin1"), Role.ADMIN)
    every { missionRepository.findSiteKeysWithMissionsOnDate(date) } returns siteKeys
    every { repository.findBySiteKeys(siteKeys) } returns sites

    // when:
    val result = sut(date = date)

    // then:
    assertTrue(result.isSuccess)
    assertEquals(sites, result.getOrNull())
    verify(exactly = 1) { missionRepository.findSiteKeysWithMissionsOnDate(date) }
    verify(exactly = 1) { repository.findBySiteKeys(siteKeys) }
    verify(exactly = 0) { repository.findAll() }
  }

  @Test
  fun `should keep the customer nr when looking sites up by key`() {
    // given: two customers reuse the same site nr, only customer 2 has a mission today
    val date = LocalDate.of(2026, 5, 6)
    val siteKeys = setOf(SiteKey(customerNr = 2L, siteNr = 5L))
    every { actorProvider.get() } returns Actor.User(UserId("admin1"), Role.ADMIN)
    every { missionRepository.findSiteKeysWithMissionsOnDate(date) } returns siteKeys
    every { repository.findBySiteKeys(siteKeys) } returns sites

    // when:
    sut(date = date)

    // then: the site nr is never looked up without its customer nr
    verify(exactly = 1) { repository.findBySiteKeys(setOf(SiteKey(customerNr = 2L, siteNr = 5L))) }
  }

  @Test
  fun `should return empty list when no missions exist on the given date`() {
    // given:
    val date = LocalDate.of(2026, 5, 6)
    every { actorProvider.get() } returns Actor.User(UserId("admin1"), Role.ADMIN)
    every { missionRepository.findSiteKeysWithMissionsOnDate(date) } returns emptySet()

    // when:
    val result = sut(date = date)

    // then:
    assertTrue(result.isSuccess)
    assertEquals(emptyList(), result.getOrNull())
    verify(exactly = 1) { missionRepository.findSiteKeysWithMissionsOnDate(date) }
    verify(exactly = 0) { repository.findBySiteKeys(any()) }
  }

  @Test
  fun `should return SecurityException when user is not admin`() {
    // given:
    every { actorProvider.get() } returns Actor.User(UserId("user1"), Role.VIEWER)

    // when:
    val result = sut(date = null)

    // then:
    assertTrue(result.isFailure)
    assertIs<SecurityException>(result.exceptionOrNull())
    verify(exactly = 0) { repository.findAll() }
  }

  @Test
  fun `should return failure when repository throws`() {
    // given:
    every { actorProvider.get() } returns Actor.User(UserId("admin1"), Role.ADMIN)
    every { repository.findAll() } throws RuntimeException("db error")

    // when:
    val result = sut(date = null)

    // then:
    assertTrue(result.isFailure)
    assertEquals("db error", result.exceptionOrNull()?.message)
  }
}
