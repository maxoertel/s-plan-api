package de.sauter_gmbh.s_plan_api.delivery_notes_web

import de.sauter_gmbh.s_plan_api.delivery_notes_application.usecase.GetDeliveryNoteCustomerSignatureUseCase
import de.sauter_gmbh.s_plan_api.delivery_notes_application.usecase.GetDeliveryNoteDriverSignatureUseCase
import de.sauter_gmbh.s_plan_api.delivery_notes_application.usecase.GetDeliveryNotePdfBytesUseCase
import de.sauter_gmbh.s_plan_api.delivery_notes_application.usecase.GetDeliveryNotePdfsUseCase
import de.sauter_gmbh.s_plan_api.delivery_notes_application.usecase.GetDeliveryNotesUseCase
import de.sauter_gmbh.s_plan_api.delivery_notes_application.usecase.UpdateDeliveryNoteStatusUseCase
import de.sauter_gmbh.s_plan_api.delivery_notes_domain.model.DeliveryNoteId
import de.sauter_gmbh.s_plan_api.delivery_notes_domain.model.DeliveryNotePdfId
import de.sauter_gmbh.s_plan_api.delivery_notes_web.dto.UpdateDeliveryNoteStatusRequestDto
import de.sauter_gmbh.s_plan_api.delivery_notes_web.mapper.toDto
import de.sauter_gmbh.s_plan_api.delivery_notes_web.mapper.toModel
import de.sauter_gmbh.s_plan_api.infra_web.ErrorMapper
import de.sauter_gmbh.s_plan_api.infra_web.toResponse
import java.time.LocalDate
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/delivery-notes")
internal class DeliveryNoteController(
  private val getDeliveryNotes: GetDeliveryNotesUseCase,
  private val getDriverSignature: GetDeliveryNoteDriverSignatureUseCase,
  private val getCustomerSignature: GetDeliveryNoteCustomerSignatureUseCase,
  private val getDeliveryNotePdfs: GetDeliveryNotePdfsUseCase,
  private val getDeliveryNotePdfBytes: GetDeliveryNotePdfBytesUseCase,
  private val updateDeliveryNoteStatus: UpdateDeliveryNoteStatusUseCase,
) {

  @GetMapping
  fun get(
    @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) date: LocalDate
  ): ResponseEntity<*> {
    return getDeliveryNotes(date).toResponse { notes -> notes.map { it.toDto() } }
  }

  // Admin-only correction of the status timeline. A time that is omitted or blank keeps its
  // stored value, so a timestamp that is already set can be corrected but never cleared.
  @PatchMapping("/{id}/status")
  fun patchStatus(
    @PathVariable id: String,
    @RequestBody request: UpdateDeliveryNoteStatusRequestDto,
  ): ResponseEntity<*> =
    updateDeliveryNoteStatus(DeliveryNoteId(id), request.toModel()).toResponse { it.toDto() }

  @GetMapping("/{id}/driver-signature")
  fun driverSignature(@PathVariable id: String): ResponseEntity<*> =
    getDriverSignature(id).toJpegResponse()

  @GetMapping("/{id}/customer-signature")
  fun customerSignature(@PathVariable id: String): ResponseEntity<*> =
    getCustomerSignature(id).toJpegResponse()

  @GetMapping("/{id}/pdfs")
  fun pdfs(@PathVariable id: String): ResponseEntity<*> =
    getDeliveryNotePdfs(DeliveryNoteId(id)).toResponse { pdfs -> pdfs.map { it.toDto() } }

  @GetMapping("/{id}/pdfs/{pdfId}")
  fun pdfBytes(
    @PathVariable id: String,
    @PathVariable pdfId: String,
  ): ResponseEntity<*> = getDeliveryNotePdfBytes(DeliveryNotePdfId(pdfId)).toPdfResponse()

  private fun Result<ByteArray>.toJpegResponse(): ResponseEntity<*> =
    fold(
      onSuccess = { ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(it) },
      onFailure = {
        it.printStackTrace()
        ErrorMapper.toResponseEntity(it)
      },
    )

  private fun Result<ByteArray>.toPdfResponse(): ResponseEntity<*> =
    fold(
      onSuccess = { ResponseEntity.ok().contentType(MediaType.APPLICATION_PDF).body(it) },
      onFailure = {
        it.printStackTrace()
        ErrorMapper.toResponseEntity(it)
      },
    )
}
