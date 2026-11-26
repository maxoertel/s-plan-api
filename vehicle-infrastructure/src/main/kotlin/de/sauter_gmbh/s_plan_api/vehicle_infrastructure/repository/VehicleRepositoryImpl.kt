package de.sauter_gmbh.s_plan_api.vehicle_infrastructure.repository

import de.sauter_gmbh.s_plan_api.domain_identity.VehicleId
import de.sauter_gmbh.s_plan_api.vehicle_domain.model.Vehicle
import de.sauter_gmbh.s_plan_api.vehicle_domain.repository.VehicleRepository
import de.sauter_gmbh.s_plan_api.vehicle_infrastructure.datasource.database.VehicleDataSource
import de.sauter_gmbh.s_plan_api.vehicle_infrastructure.mapper.toModel
import org.springframework.stereotype.Repository

@Repository
internal class VehicleRepositoryImpl(
  private val dataSource: VehicleDataSource,
) : VehicleRepository {

  override fun findAll(): List<Vehicle> = dataSource.findAll().map { it.toModel() }

  override fun findById(id: VehicleId): Vehicle? = dataSource.findById(id.value)?.toModel()

  override fun update(vehicle: Vehicle, updatedBy: String): Vehicle {
    val id = vehicle.id
    val rows =
      dataSource.update(
        id = id.value,
        licensePlate = vehicle.licensePlate,
        alias = vehicle.alias,
        vehicleType = vehicle.vehicleType,
        loadCapacity = vehicle.loadCapacity,
        updatedBy = updatedBy,
        timestampMillis = System.currentTimeMillis(),
      )
    if (rows != 1) throw NoSuchElementException("Vehicle not found: $id")
    return dataSource.findById(id.value)?.toModel()
      ?: throw NoSuchElementException("Vehicle not found: $id")
  }
}
