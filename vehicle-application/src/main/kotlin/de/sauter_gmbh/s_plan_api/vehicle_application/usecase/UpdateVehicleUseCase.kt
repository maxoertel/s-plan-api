package de.sauter_gmbh.s_plan_api.vehicle_application.usecase

import de.sauter_gmbh.s_plan_api.application_security.provider.ActorProvider
import de.sauter_gmbh.s_plan_api.domain_identity.Actor
import de.sauter_gmbh.s_plan_api.domain_identity.VehicleId
import de.sauter_gmbh.s_plan_api.vehicle_domain.model.Vehicle
import de.sauter_gmbh.s_plan_api.vehicle_domain.policy.canUpdateVehicle
import de.sauter_gmbh.s_plan_api.vehicle_domain.repository.VehicleRepository
import org.springframework.stereotype.Service

interface UpdateVehicleUseCase {
  operator fun invoke(id: VehicleId, patch: (Vehicle) -> Vehicle): Result<Vehicle>
}

@Service
internal class UpdateVehicleUseCaseImpl(
  private val vehicleRepository: VehicleRepository,
  private val actorProvider: ActorProvider,
) : UpdateVehicleUseCase {

  override operator fun invoke(id: VehicleId, patch: (Vehicle) -> Vehicle): Result<Vehicle> =
    runCatching {
      val actor = actorProvider.get()
      if (!actor.canUpdateVehicle())
        throw SecurityException("Actor not authorized to update vehicles")

      val updatedBy =
        (actor as? Actor.User)?.userId?.value
          ?: throw SecurityException("Actor not authorized to update vehicles")

      val existing =
        vehicleRepository.findById(id)
          ?: throw NoSuchElementException("Vehicle not found: ${id.value}")

      vehicleRepository.update(patch(existing).copy(id = existing.id), updatedBy = updatedBy)
    }
}
