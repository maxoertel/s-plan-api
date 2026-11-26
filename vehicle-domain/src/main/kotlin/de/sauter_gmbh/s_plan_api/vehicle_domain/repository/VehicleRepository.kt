package de.sauter_gmbh.s_plan_api.vehicle_domain.repository

import de.sauter_gmbh.s_plan_api.domain_identity.VehicleId
import de.sauter_gmbh.s_plan_api.vehicle_domain.model.Vehicle

interface VehicleRepository {
  fun findAll(): List<Vehicle>

  fun findById(id: VehicleId): Vehicle?

  fun update(vehicle: Vehicle, updatedBy: String): Vehicle
}
