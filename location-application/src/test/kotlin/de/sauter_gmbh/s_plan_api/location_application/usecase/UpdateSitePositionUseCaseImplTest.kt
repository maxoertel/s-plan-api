package de.sauter_gmbh.s_plan_api.location_application.usecase

import de.sauter_gmbh.s_plan_api.application_security.provider.ActorProvider
import de.sauter_gmbh.s_plan_api.domain_identity.Actor
import de.sauter_gmbh.s_plan_api.domain_identity.Role
import de.sauter_gmbh.s_plan_api.domain_identity.SiteKey
import de.sauter_gmbh.s_plan_api.domain_identity.UserId
import de.sauter_gmbh.s_plan_api.location_domain.model.Site
import de.sauter_gmbh.s_plan_api.location_domain.model.SitePosition
import de.sauter_gmbh.s_plan_api.location_domain.repository.SiteRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

class UpdateSitePositionUseCaseImplTest {

  private val siteRepository = mockk<SiteRepository>()
  private val actorProvider = mockk<ActorProvider>()

  private val sut by lazy { UpdateSitePositionUseCaseImpl(siteRepository, actorProvider) }

  private val siteKey = SiteKey(customerNr = 1L, siteNr = 5L)
  private val position = SitePosition(latitude = 48.1, longitude = 9.2, radiusInMeter = 80)
  private val admin = Actor.User(UserId("admin1"), Role.ADMIN)

  private val site =
    Site(
      customerNr = 1L,
      siteNr = 5L,
      matchCode = null,
      description1 = "Bau 1",
      description2 = null,
      description3 = null,
      description4 = null,
      street = null,
      houseNumber = null,
      zip = null,
      city = null,
      country = null,
      phone = null,
      mobile = null,
      fax = null,
      contact = null,
      note = null,
      internalInfo = null,
      deliveryNoteInfo = null,
      latitude = 48.1,
      longitude = 9.2,
      radiusInMeter = 80,
      factoryNr = null,
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

  @Test
  fun `should update position and return site when user is admin`() {
    // given:
    every { actorProvider.get() } returns admin
    every { siteRepository.updatePosition(siteKey, position) } returns site

    // when:
    val result = sut(siteKey, position)

    // then:
    assertTrue(result.isSuccess)
    assertEquals(site, result.getOrNull())
    verify(exactly = 1) { siteRepository.updatePosition(siteKey, position) }
  }

  @Test
  fun `should return SecurityException when user is not admin`() {
    // given:
    every { actorProvider.get() } returns Actor.User(UserId("user1"), Role.VIEWER)

    // when:
    val result = sut(siteKey, position)

    // then:
    assertIs<SecurityException>(result.exceptionOrNull())
    verify(exactly = 0) { siteRepository.updatePosition(any(), any()) }
  }

  @Test
  fun `should return IllegalArgumentException when latitude is out of range`() {
    // given:
    every { actorProvider.get() } returns admin

    // when:
    val result = sut(siteKey, position.copy(latitude = 91.0))

    // then:
    assertIs<IllegalArgumentException>(result.exceptionOrNull())
    verify(exactly = 0) { siteRepository.updatePosition(any(), any()) }
  }

  @Test
  fun `should return IllegalArgumentException when longitude is out of range`() {
    // given:
    every { actorProvider.get() } returns admin

    // when:
    val result = sut(siteKey, position.copy(longitude = -181.0))

    // then:
    assertIs<IllegalArgumentException>(result.exceptionOrNull())
    verify(exactly = 0) { siteRepository.updatePosition(any(), any()) }
  }

  @Test
  fun `should return IllegalArgumentException when radius is not positive`() {
    // given:
    every { actorProvider.get() } returns admin

    // when:
    val result = sut(siteKey, position.copy(radiusInMeter = 0))

    // then:
    assertIs<IllegalArgumentException>(result.exceptionOrNull())
    verify(exactly = 0) { siteRepository.updatePosition(any(), any()) }
  }

  @Test
  fun `should return NoSuchElementException when site does not exist`() {
    // given:
    every { actorProvider.get() } returns admin
    every { siteRepository.updatePosition(siteKey, position) } returns null

    // when:
    val result = sut(siteKey, position)

    // then:
    assertIs<NoSuchElementException>(result.exceptionOrNull())
  }
}
