package de.sauter_gmbh.s_plan_api.delivery_notes_application.usecase

import de.sauter_gmbh.s_plan_api.application_security.provider.ActorProvider
import de.sauter_gmbh.s_plan_api.delivery_notes_domain.policy.canViewDeliveryNoteSignature
import de.sauter_gmbh.s_plan_api.delivery_notes_domain.repository.DeliveryNoteRepository
import org.springframework.stereotype.Service

interface GetDeliveryNoteDriverSignatureUseCase {
  operator fun invoke(deliveryNoteId: String): Result<ByteArray>
}

@Service
internal class GetDeliveryNoteDriverSignatureUseCaseImpl(
  private val repository: DeliveryNoteRepository,
  private val actorProvider: ActorProvider,
) : GetDeliveryNoteDriverSignatureUseCase {

  override operator fun invoke(deliveryNoteId: String): Result<ByteArray> = runCatching {
    val actor = actorProvider.get()
    if (!actor.canViewDeliveryNoteSignature())
      throw SecurityException("Actor not authorized to view delivery note signature")

    repository.findDriverSignatureJpeg(deliveryNoteId)
      ?: throw NoSuchElementException(
        "Driver signature not found for delivery note $deliveryNoteId"
      )
  }
}
