package de.sauter_gmbh.s_plan_api.vehicle_infrastructure.datasource.database

import de.sauter_gmbh.s_plan_api.vehicle_infrastructure.datasource.entity.VehicleEntity

internal interface VehicleDataSource {
  fun findAll(): List<VehicleEntity>

  fun findById(id: Long): VehicleEntity?

  fun update(
    id: Long,
    licensePlate: String?,
    alias: String?,
    vehicleType: Int?,
    loadCapacity: Double?,
    updatedBy: String,
    timestampMillis: Long
  ): Int
}
