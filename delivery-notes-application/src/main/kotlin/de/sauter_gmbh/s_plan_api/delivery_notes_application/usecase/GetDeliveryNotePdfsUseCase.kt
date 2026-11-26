package de.sauter_gmbh.s_plan_api.delivery_notes_application.usecase

import de.sauter_gmbh.s_plan_api.application_security.provider.ActorProvider
import de.sauter_gmbh.s_plan_api.delivery_notes_domain.model.DeliveryNoteId
import de.sauter_gmbh.s_plan_api.delivery_notes_domain.model.DeliveryNotePdf
import de.sauter_gmbh.s_plan_api.delivery_notes_domain.policy.canViewDeliveryNotePdf
import de.sauter_gmbh.s_plan_api.delivery_notes_domain.repository.DeliveryNoteRepository
import org.springframework.stereotype.Service

interface GetDeliveryNotePdfsUseCase {
  operator fun invoke(deliveryNoteId: DeliveryNoteId): Result<List<DeliveryNotePdf>>
}

@Service
internal class GetDeliveryNotePdfsUseCaseImpl(
  private val repository: DeliveryNoteRepository,
  private val actorProvider: ActorProvider,
) : GetDeliveryNotePdfsUseCase {

  override operator fun invoke(deliveryNoteId: DeliveryNoteId): Result<List<DeliveryNotePdf>> =
    runCatching {
      val actor = actorProvider.get()
      if (!actor.canViewDeliveryNotePdf())
        throw SecurityException("Actor not authorized to view delivery note pdfs")

      repository.findPdfsByDeliveryNote(deliveryNoteId)
    }
}
