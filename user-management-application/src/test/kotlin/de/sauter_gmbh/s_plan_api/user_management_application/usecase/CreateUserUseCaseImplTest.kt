package de.sauter_gmbh.s_plan_api.user_management_application.usecase

import de.sauter_gmbh.s_plan_api.application_security.provider.ActorProvider
import de.sauter_gmbh.s_plan_api.domain_identity.Actor
import de.sauter_gmbh.s_plan_api.domain_identity.Role
import de.sauter_gmbh.s_plan_api.domain_identity.UserId
import de.sauter_gmbh.s_plan_api.user_management_domain.model.NewUser
import de.sauter_gmbh.s_plan_api.user_management_domain.model.User
import de.sauter_gmbh.s_plan_api.user_management_domain.repository.UserRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

class CreateUserUseCaseImplTest {

  private val repository = mockk<UserRepository>()
  private val actorProvider = mockk<ActorProvider>()

  private val sut by lazy { CreateUserUseCaseImpl(repository, actorProvider) }

  private val adminId = UserId("admin1")
  private val admin = Actor.User(adminId, Role.ADMIN)
  private val newUserId = UserId("jdoe")

  @Test
  fun `should create user when actor is admin user`() {
    // given:
    val newUser = testNewUser()
    val created = testUser()
    every { actorProvider.get() } returns admin
    every { repository.findById(newUserId) } returns null
    every { repository.create(newUser, "admin1") } returns created

    // when:
    val result = sut(newUser)

    // then:
    assertTrue(result.isSuccess)
    assertEquals(created, result.getOrNull())
    verify(exactly = 1) { repository.create(newUser, "admin1") }
  }

  @Test
  fun `should return IllegalArgumentException when user already exists`() {
    // given:
    val newUser = testNewUser()
    every { actorProvider.get() } returns admin
    every { repository.findById(newUserId) } returns testUser()

    // when:
    val result = sut(newUser)

    // then:
    assertTrue(result.isFailure)
    assertIs<IllegalArgumentException>(result.exceptionOrNull())
    verify(exactly = 0) { repository.create(any(), any()) }
  }

  @Test
  fun `should return SecurityException when actor is viewer user`() {
    // given:
    every { actorProvider.get() } returns Actor.User(UserId("user1"), Role.VIEWER)

    // when:
    val result = sut(testNewUser())

    // then:
    assertTrue(result.isFailure)
    assertIs<SecurityException>(result.exceptionOrNull())
    verify(exactly = 0) { repository.create(any(), any()) }
  }

  @Test
  fun `should return failure when repository throws`() {
    // given:
    val newUser = testNewUser()
    every { actorProvider.get() } returns admin
    every { repository.findById(newUserId) } returns null
    every { repository.create(newUser, "admin1") } throws RuntimeException("db error")

    // when:
    val result = sut(newUser)

    // then:
    assertTrue(result.isFailure)
    assertIs<RuntimeException>(result.exceptionOrNull())
  }

  private fun testNewUser() =
    NewUser(
      id = newUserId,
      salutation = "Herr",
      title = null,
      firstName = "John",
      lastName = "Doe",
      authType = "auth0",
      authUser = "auth0|abc",
      role = "viewer",
    )

  private fun testUser() =
    User(
      id = newUserId,
      salutation = "Herr",
      title = null,
      firstName = "John",
      lastName = "Doe",
      authType = "auth0",
      authUser = "auth0|abc",
      role = "viewer",
      createdAt = 1_717_000_000_000L,
      createdBy = "admin1",
      updatedAt = 1_717_000_000_000L,
      updatedBy = "admin1",
    )
}
