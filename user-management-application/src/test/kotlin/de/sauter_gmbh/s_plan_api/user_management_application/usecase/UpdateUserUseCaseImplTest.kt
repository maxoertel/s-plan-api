package de.sauter_gmbh.s_plan_api.user_management_application.usecase

import de.sauter_gmbh.s_plan_api.application_security.provider.ActorProvider
import de.sauter_gmbh.s_plan_api.domain_identity.Actor
import de.sauter_gmbh.s_plan_api.domain_identity.Role
import de.sauter_gmbh.s_plan_api.domain_identity.UserId
import de.sauter_gmbh.s_plan_api.user_management_domain.model.User
import de.sauter_gmbh.s_plan_api.user_management_domain.repository.UserRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

class UpdateUserUseCaseImplTest {

  private val repository = mockk<UserRepository>()
  private val actorProvider = mockk<ActorProvider>()

  private val sut by lazy { UpdateUserUseCaseImpl(repository, actorProvider) }

  private val admin = Actor.User(UserId("admin1"), Role.ADMIN)
  private val targetId = UserId("jdoe")

  @Test
  fun `should apply patch to stored user when actor is admin user`() {
    // given:
    val existing = testUser()
    val patchedSlot = slot<User>()
    every { actorProvider.get() } returns admin
    every { repository.findById(targetId) } returns existing
    every { repository.update(capture(patchedSlot), "admin1") } answers { patchedSlot.captured }

    // when:
    val result = sut(targetId) { user -> user.copy(lastName = "Smith", role = "admin") }

    // then:
    assertTrue(result.isSuccess)
    assertEquals("Smith", patchedSlot.captured.lastName)
    assertEquals("admin", patchedSlot.captured.role)
    assertEquals("John", patchedSlot.captured.firstName)
    verify(exactly = 1) { repository.update(any(), "admin1") }
  }

  @Test
  fun `should keep identity and creation audit fields when patch tries to change them`() {
    // given:
    val existing = testUser()
    val patchedSlot = slot<User>()
    every { actorProvider.get() } returns admin
    every { repository.findById(targetId) } returns existing
    every { repository.update(capture(patchedSlot), "admin1") } answers { patchedSlot.captured }

    // when:
    val result =
      sut(targetId) { user ->
        user.copy(id = UserId("hijacked"), createdAt = 0L, createdBy = "attacker")
      }

    // then:
    assertTrue(result.isSuccess)
    assertEquals(targetId, patchedSlot.captured.id)
    assertEquals(existing.createdAt, patchedSlot.captured.createdAt)
    assertEquals(existing.createdBy, patchedSlot.captured.createdBy)
  }

  @Test
  fun `should return NoSuchElementException when user does not exist`() {
    // given:
    every { actorProvider.get() } returns admin
    every { repository.findById(targetId) } returns null

    // when:
    val result = sut(targetId) { it }

    // then:
    assertTrue(result.isFailure)
    assertIs<NoSuchElementException>(result.exceptionOrNull())
    verify(exactly = 0) { repository.update(any(), any()) }
  }

  @Test
  fun `should return SecurityException when actor is viewer user`() {
    // given:
    every { actorProvider.get() } returns Actor.User(UserId("user1"), Role.VIEWER)

    // when:
    val result = sut(targetId) { it }

    // then:
    assertTrue(result.isFailure)
    assertIs<SecurityException>(result.exceptionOrNull())
    verify(exactly = 0) { repository.update(any(), any()) }
  }

  @Test
  fun `should return failure when repository throws`() {
    // given:
    every { actorProvider.get() } returns admin
    every { repository.findById(targetId) } returns testUser()
    every { repository.update(any(), "admin1") } throws RuntimeException("db error")

    // when:
    val result = sut(targetId) { it }

    // then:
    assertTrue(result.isFailure)
    assertIs<RuntimeException>(result.exceptionOrNull())
  }

  private fun testUser() =
    User(
      id = targetId,
      salutation = "Herr",
      title = null,
      firstName = "John",
      lastName = "Doe",
      authType = "auth0",
      authUser = "auth0|abc",
      role = "viewer",
      createdAt = 1_717_000_000_000L,
      createdBy = "system",
      updatedAt = 1_717_100_000_000L,
      updatedBy = "system",
    )
}
