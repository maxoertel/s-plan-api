package de.sauter_gmbh.s_plan_api.location_application.usecase

import de.sauter_gmbh.s_plan_api.application_security.provider.ActorProvider
import de.sauter_gmbh.s_plan_api.domain_identity.Actor
import de.sauter_gmbh.s_plan_api.domain_identity.Role
import de.sauter_gmbh.s_plan_api.domain_identity.UserId
import de.sauter_gmbh.s_plan_api.location_domain.model.Factory
import de.sauter_gmbh.s_plan_api.location_domain.repository.FactoryRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

class GetFactoriesUseCaseImplTest {

  private val repository = mockk<FactoryRepository>()
  private val actorProvider = mockk<ActorProvider>()

  private val sut by lazy { GetFactoriesUseCaseImpl(repository, actorProvider) }

  private val factories =
    listOf(
      Factory(
        id = 100L,
        matchCode = "F1",
        name = "Werk Berlin",
        street = "Strasse",
        houseNumber = "1",
        zip = "10115",
        city = "Berlin",
        country = "DE",
        phone = "030-123",
        latitude = 52.5,
        longitude = 13.4,
        radiusInMeter = 100,
      )
    )

  @Test
  fun `should return factories when user is admin`() {
    // given:
    every { actorProvider.get() } returns Actor.User(UserId("admin1"), Role.ADMIN)
    every { repository.findAll() } returns factories

    // when:
    val result = sut()

    // then:
    assertTrue(result.isSuccess)
    assertEquals(factories, result.getOrNull())
    verify(exactly = 1) { repository.findAll() }
  }

  @Test
  fun `should return SecurityException when user is not admin`() {
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
  fun `should return failure when repository throws`() {
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
