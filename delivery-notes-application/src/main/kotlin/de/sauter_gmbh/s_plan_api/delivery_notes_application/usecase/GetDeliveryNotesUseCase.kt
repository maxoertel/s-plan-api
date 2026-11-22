package de.sauter_gmbh.s_plan_api.delivery_notes_application.usecase

import de.sauter_gmbh.s_plan_api.application_security.provider.ActorProvider
import de.sauter_gmbh.s_plan_api.delivery_notes_domain.model.DeliveryNote
import de.sauter_gmbh.s_plan_api.delivery_notes_domain.policy.canListAllDeliveryNotes
import de.sauter_gmbh.s_plan_api.delivery_notes_domain.repository.DeliveryNoteRepository
import java.time.LocalDate
import org.springframework.stereotype.Service

interface GetDeliveryNotesUseCase {
  operator fun invoke(date: LocalDate): Result<List<DeliveryNote>>
}

@Service
internal class GetDeliveryNotesUseCaseImpl(
  private val repository: DeliveryNoteRepository,
  private val actorProvider: ActorProvider,
) : GetDeliveryNotesUseCase {

  override operator fun invoke(date: LocalDate): Result<List<DeliveryNote>> = runCatching {
    val actor = actorProvider.get()
    if (!actor.canListAllDeliveryNotes())
      throw SecurityException("Actor not authorized to list all delivery notes")

    repository.findByDate(date)
  }
}
