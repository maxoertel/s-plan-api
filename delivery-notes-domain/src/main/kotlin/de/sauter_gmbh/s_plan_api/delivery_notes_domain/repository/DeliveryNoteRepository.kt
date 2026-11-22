package de.sauter_gmbh.s_plan_api.delivery_notes_domain.repository

import de.sauter_gmbh.s_plan_api.delivery_notes_domain.model.DeliveryNote
import de.sauter_gmbh.s_plan_api.delivery_notes_domain.model.DeliveryNoteId
import de.sauter_gmbh.s_plan_api.delivery_notes_domain.model.DeliveryNotePdf
import de.sauter_gmbh.s_plan_api.delivery_notes_domain.model.DeliveryNotePdfId
import de.sauter_gmbh.s_plan_api.delivery_notes_domain.model.StatusUpdate
import java.time.LocalDate

interface DeliveryNoteRepository {
  fun findByDate(date: LocalDate): List<DeliveryNote>

  fun findById(deliveryNoteId: DeliveryNoteId): DeliveryNote?

  fun findDriverSignatureJpeg(deliveryNoteId: String): ByteArray?

  fun findCustomerSignatureJpeg(deliveryNoteId: String): ByteArray?

  fun findPdfsByDeliveryNote(deliveryNoteId: DeliveryNoteId): List<DeliveryNotePdf>

  fun findPdfBytes(pdfId: DeliveryNotePdfId): ByteArray?

  fun updateStatusTimes(deliveryNoteId: DeliveryNoteId, update: StatusUpdate)
}
