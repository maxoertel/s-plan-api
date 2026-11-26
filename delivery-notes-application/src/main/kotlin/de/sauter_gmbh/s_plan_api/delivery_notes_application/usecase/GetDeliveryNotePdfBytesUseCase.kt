package de.sauter_gmbh.s_plan_api.delivery_notes_application.usecase

import de.sauter_gmbh.s_plan_api.application_security.provider.ActorProvider
import de.sauter_gmbh.s_plan_api.delivery_notes_domain.model.DeliveryNotePdfId
import de.sauter_gmbh.s_plan_api.delivery_notes_domain.policy.canViewDeliveryNotePdf
import de.sauter_gmbh.s_plan_api.delivery_notes_domain.repository.DeliveryNoteRepository
import org.springframework.stereotype.Service

interface GetDeliveryNotePdfBytesUseCase {
  operator fun invoke(pdfId: DeliveryNotePdfId): Result<ByteArray>
}

@Service
internal class GetDeliveryNotePdfBytesUseCaseImpl(
  private val repository: DeliveryNoteRepository,
  private val actorProvider: ActorProvider,
) : GetDeliveryNotePdfBytesUseCase {

  override operator fun invoke(pdfId: DeliveryNotePdfId): Result<ByteArray> = runCatching {
    val actor = actorProvider.get()
    if (!actor.canViewDeliveryNotePdf())
      throw SecurityException("Actor not authorized to view delivery note pdf")

    repository.findPdfBytes(pdfId)
      ?: throw NoSuchElementException("PDF not found for id ${pdfId.value}")
  }
}
