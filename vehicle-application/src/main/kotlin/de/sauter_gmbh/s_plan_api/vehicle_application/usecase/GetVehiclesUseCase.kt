package de.sauter_gmbh.s_plan_api.vehicle_application.usecase

import de.sauter_gmbh.s_plan_api.application_security.provider.ActorProvider
import de.sauter_gmbh.s_plan_api.vehicle_domain.model.Vehicle
import de.sauter_gmbh.s_plan_api.vehicle_domain.policy.canListAllVehicles
import de.sauter_gmbh.s_plan_api.vehicle_domain.repository.VehicleRepository
import org.springframework.stereotype.Service

interface GetVehiclesUseCase {
  operator fun invoke(): Result<List<Vehicle>>
}

@Service
internal class GetVehiclesUseCaseImpl(
  private val repository: VehicleRepository,
  private val actorProvider: ActorProvider,
) : GetVehiclesUseCase {

  override operator fun invoke(): Result<List<Vehicle>> = runCatching {
    val actor = actorProvider.get()
    if (!actor.canListAllVehicles())
      throw SecurityException("Actor not authorized to list all vehicles")

    repository.findAll()
  }
}
