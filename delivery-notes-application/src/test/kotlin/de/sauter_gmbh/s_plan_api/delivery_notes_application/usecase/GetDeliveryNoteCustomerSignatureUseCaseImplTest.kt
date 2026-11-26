package de.sauter_gmbh.s_plan_api.delivery_notes_application.usecase

import de.sauter_gmbh.s_plan_api.application_security.provider.ActorProvider
import de.sauter_gmbh.s_plan_api.delivery_notes_domain.repository.DeliveryNoteRepository
import de.sauter_gmbh.s_plan_api.domain_identity.Actor
import de.sauter_gmbh.s_plan_api.domain_identity.Role
import de.sauter_gmbh.s_plan_api.domain_identity.UserId
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

class GetDeliveryNoteCustomerSignatureUseCaseImplTest {

  private val repository = mockk<DeliveryNoteRepository>()
  private val actorProvider = mockk<ActorProvider>()

  private val sut by lazy { GetDeliveryNoteCustomerSignatureUseCaseImpl(repository, actorProvider) }

  private val deliveryNoteId = "100_55_20260418_083000"

  @Test
  fun `should return success with jpeg bytes when user is admin and signature exists`() {
    // given:
    val jpeg = byteArrayOf(4, 5, 6)
    every { actorProvider.get() } returns Actor.User(UserId("admin1"), Role.ADMIN)
    every { repository.findCustomerSignatureJpeg(deliveryNoteId) } returns jpeg

    // when:
    val result = sut(deliveryNoteId)

    // then:
    assertTrue(result.isSuccess)
    assertContentEquals(jpeg, result.getOrNull())
    verify(exactly = 1) { repository.findCustomerSignatureJpeg(deliveryNoteId) }
  }

  @Test
  fun `should return failure with NoSuchElementException when signature is null`() {
    // given:
    every { actorProvider.get() } returns Actor.User(UserId("admin1"), Role.ADMIN)
    every { repository.findCustomerSignatureJpeg(deliveryNoteId) } returns null

    // when:
    val result = sut(deliveryNoteId)

    // then:
    assertTrue(result.isFailure)
    assertIs<NoSuchElementException>(result.exceptionOrNull())
  }

  @Test
  fun `should return failure with SecurityException when user is not admin`() {
    // given:
    every { actorProvider.get() } returns Actor.User(UserId("user1"), Role.VIEWER)

    // when:
    val result = sut(deliveryNoteId)

    // then:
    assertTrue(result.isFailure)
    assertIs<SecurityException>(result.exceptionOrNull())
    verify(exactly = 0) { repository.findCustomerSignatureJpeg(any()) }
  }

  @Test
  fun `should return failure when repository throws exception`() {
    // given:
    every { actorProvider.get() } returns Actor.User(UserId("admin1"), Role.ADMIN)
    every { repository.findCustomerSignatureJpeg(deliveryNoteId) } throws
      RuntimeException("db error")

    // when:
    val result = sut(deliveryNoteId)

    // then:
    assertTrue(result.isFailure)
    assertEquals("db error", result.exceptionOrNull()?.message)
  }
}
