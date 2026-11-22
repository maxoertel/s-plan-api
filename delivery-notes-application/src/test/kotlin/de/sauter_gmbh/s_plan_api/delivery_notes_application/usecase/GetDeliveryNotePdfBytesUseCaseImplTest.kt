package de.sauter_gmbh.s_plan_api.delivery_notes_application.usecase

import de.sauter_gmbh.s_plan_api.application_security.provider.ActorProvider
import de.sauter_gmbh.s_plan_api.delivery_notes_domain.model.DeliveryNotePdfId
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

class GetDeliveryNotePdfBytesUseCaseImplTest {

  private val repository = mockk<DeliveryNoteRepository>()
  private val actorProvider = mockk<ActorProvider>()

  private val sut by lazy { GetDeliveryNotePdfBytesUseCaseImpl(repository, actorProvider) }

  private val pdfId = DeliveryNotePdfId("100_20260418_083000_55_1_20260418_083100_20260418_083200")

  @Test
  fun `should return pdf bytes when user is admin`() {
    // given:
    val bytes = byteArrayOf(1, 2, 3)
    every { actorProvider.get() } returns Actor.User(UserId("admin1"), Role.ADMIN)
    every { repository.findPdfBytes(pdfId) } returns bytes

    // when:
    val result = sut(pdfId)

    // then:
    assertTrue(result.isSuccess)
    assertContentEquals(bytes, result.getOrNull())
    verify(exactly = 1) { repository.findPdfBytes(pdfId) }
  }

  @Test
  fun `should return failure with NoSuchElementException when bytes are null`() {
    // given:
    every { actorProvider.get() } returns Actor.User(UserId("admin1"), Role.ADMIN)
    every { repository.findPdfBytes(pdfId) } returns null

    // when:
    val result = sut(pdfId)

    // then:
    assertTrue(result.isFailure)
    assertIs<NoSuchElementException>(result.exceptionOrNull())
  }

  @Test
  fun `should return failure with SecurityException when user is not admin`() {
    // given:
    every { actorProvider.get() } returns Actor.User(UserId("user1"), Role.VIEWER)

    // when:
    val result = sut(pdfId)

    // then:
    assertTrue(result.isFailure)
    assertIs<SecurityException>(result.exceptionOrNull())
    verify(exactly = 0) { repository.findPdfBytes(any()) }
  }

  @Test
  fun `should return failure when repository throws exception`() {
    // given:
    every { actorProvider.get() } returns Actor.User(UserId("admin1"), Role.ADMIN)
    every { repository.findPdfBytes(pdfId) } throws RuntimeException("db error")

    // when:
    val result = sut(pdfId)

    // then:
    assertTrue(result.isFailure)
    assertEquals("db error", result.exceptionOrNull()?.message)
  }
}
