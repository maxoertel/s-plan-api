package de.sauter_gmbh.s_plan_api.delivery_notes_application.usecase

import de.sauter_gmbh.s_plan_api.application_security.provider.ActorProvider
import de.sauter_gmbh.s_plan_api.delivery_notes_domain.model.DeliveryNoteId
import de.sauter_gmbh.s_plan_api.delivery_notes_domain.model.DeliveryNotePdf
import de.sauter_gmbh.s_plan_api.delivery_notes_domain.model.DeliveryNotePdfId
import de.sauter_gmbh.s_plan_api.delivery_notes_domain.model.DeliveryNotePdfKind
import de.sauter_gmbh.s_plan_api.delivery_notes_domain.repository.DeliveryNoteRepository
import de.sauter_gmbh.s_plan_api.domain_identity.Actor
import de.sauter_gmbh.s_plan_api.domain_identity.Role
import de.sauter_gmbh.s_plan_api.domain_identity.UserId
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import java.time.LocalDate
import java.time.LocalTime
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

class GetDeliveryNotePdfsUseCaseImplTest {

  private val repository = mockk<DeliveryNoteRepository>()
  private val actorProvider = mockk<ActorProvider>()

  private val sut by lazy { GetDeliveryNotePdfsUseCaseImpl(repository, actorProvider) }

  private val deliveryNoteId = DeliveryNoteId("100_55_20260418_083000")

  @Test
  fun `should return pdfs when user is admin`() {
    // given:
    val pdfs = listOf(samplePdf())
    every { actorProvider.get() } returns Actor.User(UserId("admin1"), Role.ADMIN)
    every { repository.findPdfsByDeliveryNote(deliveryNoteId) } returns pdfs

    // when:
    val result = sut(deliveryNoteId)

    // then:
    assertTrue(result.isSuccess)
    assertEquals(pdfs, result.getOrNull())
    verify(exactly = 1) { repository.findPdfsByDeliveryNote(deliveryNoteId) }
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
    verify(exactly = 0) { repository.findPdfsByDeliveryNote(any()) }
  }

  @Test
  fun `should return failure when repository throws exception`() {
    // given:
    every { actorProvider.get() } returns Actor.User(UserId("admin1"), Role.ADMIN)
    every { repository.findPdfsByDeliveryNote(deliveryNoteId) } throws RuntimeException("db error")

    // when:
    val result = sut(deliveryNoteId)

    // then:
    assertTrue(result.isFailure)
    assertEquals("db error", result.exceptionOrNull()?.message)
  }

  private fun samplePdf() =
    DeliveryNotePdf(
      id = DeliveryNotePdfId("100_20260418_083000_55_1_20260418_083100_20260418_083200"),
      factoryNr = 100L,
      date = LocalDate.of(2026, 4, 18),
      time = LocalTime.of(8, 30, 0),
      deliveryNoteNr = 55,
      deliveryNoteCircle = 1,
      creationDate = LocalDate.of(2026, 4, 18),
      creationTime = LocalTime.of(8, 31, 0),
      storageDate = LocalDate.of(2026, 4, 18),
      storageTime = LocalTime.of(8, 32, 0),
      kind = DeliveryNotePdfKind.FINAL,
    )
}
