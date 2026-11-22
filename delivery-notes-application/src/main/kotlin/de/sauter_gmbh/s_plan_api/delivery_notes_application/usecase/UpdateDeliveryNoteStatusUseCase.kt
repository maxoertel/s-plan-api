package de.sauter_gmbh.s_plan_api.delivery_notes_application.usecase

import de.sauter_gmbh.s_plan_api.application_security.provider.ActorProvider
import de.sauter_gmbh.s_plan_api.delivery_notes_domain.model.DeliveryNote
import de.sauter_gmbh.s_plan_api.delivery_notes_domain.model.DeliveryNoteId
import de.sauter_gmbh.s_plan_api.delivery_notes_domain.model.StatusUpdate
import de.sauter_gmbh.s_plan_api.delivery_notes_domain.policy.canEditDeliveryNoteStatus
import de.sauter_gmbh.s_plan_api.delivery_notes_domain.repository.DeliveryNoteRepository
import org.springframework.stereotype.Service

interface UpdateDeliveryNoteStatusUseCase {
  operator fun invoke(
    deliveryNoteId: DeliveryNoteId,
    update: StatusUpdate,
  ): Result<DeliveryNote>
}

@Service
internal class UpdateDeliveryNoteStatusUseCaseImpl(
  private val repository: DeliveryNoteRepository,
  private val actorProvider: ActorProvider,
) : UpdateDeliveryNoteStatusUseCase {

  override operator fun invoke(
    deliveryNoteId: DeliveryNoteId,
    update: StatusUpdate,
  ): Result<DeliveryNote> = runCatching {
    val actor = actorProvider.get()
    if (!actor.canEditDeliveryNoteStatus())
      throw SecurityException("Actor not authorized to edit delivery note status")

    val existing = repository.findById(deliveryNoteId) ?: throw notFound(deliveryNoteId)

    // An update without any time is a no-op rather than an error, so a save that turns out to
    // change nothing still answers with the delivery note the caller asked for.
    if (update.times.isEmpty()) return@runCatching existing

    repository.updateStatusTimes(deliveryNoteId, update)

    // Read back so the caller sees exactly what the database now holds, including the dates the
    // corrected times were attached to.
    repository.findById(deliveryNoteId) ?: throw notFound(deliveryNoteId)
  }

  private fun notFound(deliveryNoteId: DeliveryNoteId) =
    NoSuchElementException("Delivery note not found: ${deliveryNoteId.value}")
}
