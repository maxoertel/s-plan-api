package de.sauter_gmbh.s_plan_api.user_management_application.usecase

import de.sauter_gmbh.s_plan_api.application_security.provider.ActorProvider
import de.sauter_gmbh.s_plan_api.domain_identity.Actor
import de.sauter_gmbh.s_plan_api.domain_identity.Role
import de.sauter_gmbh.s_plan_api.domain_identity.UserId
import de.sauter_gmbh.s_plan_api.user_management_domain.repository.UserRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlin.test.Test
import kotlin.test.assertIs
import kotlin.test.assertTrue

class DeleteUserUseCaseImplTest {

  private val repository = mockk<UserRepository>(relaxUnitFun = true)
  private val actorProvider = mockk<ActorProvider>()

  private val sut by lazy { DeleteUserUseCaseImpl(repository, actorProvider) }

  private val adminId = UserId("admin1")
  private val admin = Actor.User(adminId, Role.ADMIN)
  private val targetId = UserId("jdoe")

  @Test
  fun `should delete user when actor is admin user`() {
    // given:
    every { actorProvider.get() } returns admin

    // when:
    val result = sut(targetId)

    // then:
    assertTrue(result.isSuccess)
    verify(exactly = 1) { repository.delete(targetId) }
  }

  @Test
  fun `should return IllegalArgumentException when admin deletes themselves`() {
    // given:
    every { actorProvider.get() } returns admin

    // when:
    val result = sut(adminId)

    // then:
    assertTrue(result.isFailure)
    assertIs<IllegalArgumentException>(result.exceptionOrNull())
    verify(exactly = 0) { repository.delete(any()) }
  }

  @Test
  fun `should return SecurityException when actor is viewer user`() {
    // given:
    every { actorProvider.get() } returns Actor.User(UserId("user1"), Role.VIEWER)

    // when:
    val result = sut(targetId)

    // then:
    assertTrue(result.isFailure)
    assertIs<SecurityException>(result.exceptionOrNull())
    verify(exactly = 0) { repository.delete(any()) }
  }

  @Test
  fun `should return failure when repository throws`() {
    // given:
    every { actorProvider.get() } returns admin
    every { repository.delete(targetId) } throws NoSuchElementException("User not found: jdoe")

    // when:
    val result = sut(targetId)

    // then:
    assertTrue(result.isFailure)
    assertIs<NoSuchElementException>(result.exceptionOrNull())
  }
}
