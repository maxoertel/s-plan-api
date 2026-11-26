package de.sauter_gmbh.s_plan_api.user_management_application.usecase

import de.sauter_gmbh.s_plan_api.application_security.provider.ActorProvider
import de.sauter_gmbh.s_plan_api.domain_identity.Actor
import de.sauter_gmbh.s_plan_api.domain_identity.Role
import de.sauter_gmbh.s_plan_api.domain_identity.UserId
import de.sauter_gmbh.s_plan_api.user_management_domain.model.User
import de.sauter_gmbh.s_plan_api.user_management_domain.repository.UserRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

class GetUsersUseCaseImplTest {

  private val repository = mockk<UserRepository>()
  private val actorProvider = mockk<ActorProvider>()

  private val sut by lazy { GetUsersUseCaseImpl(repository, actorProvider) }

  private val admin = Actor.User(UserId("admin1"), Role.ADMIN)

  @Test
  fun `should return all users when actor is admin user`() {
    // given:
    val users = listOf(testUser())
    every { actorProvider.get() } returns admin
    every { repository.findAll() } returns users

    // when:
    val result = sut()

    // then:
    assertTrue(result.isSuccess)
    assertEquals(users, result.getOrNull())
    verify(exactly = 1) { repository.findAll() }
  }

  @Test
  fun `should return SecurityException when actor is viewer user`() {
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
    every { actorProvider.get() } returns admin
    every { repository.findAll() } throws RuntimeException("db error")

    // when:
    val result = sut()

    // then:
    assertTrue(result.isFailure)
    assertIs<RuntimeException>(result.exceptionOrNull())
  }

  private fun testUser() =
    User(
      id = UserId("jdoe"),
      salutation = "Herr",
      title = null,
      firstName = "John",
      lastName = "Doe",
      authType = "auth0",
      authUser = "auth0|abc",
      role = "admin",
      createdAt = 1_717_000_000_000L,
      createdBy = "system",
      updatedAt = 1_717_100_000_000L,
      updatedBy = "system",
    )
}
