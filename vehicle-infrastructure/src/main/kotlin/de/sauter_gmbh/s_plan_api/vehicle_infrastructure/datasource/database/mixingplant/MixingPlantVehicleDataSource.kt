package de.sauter_gmbh.s_plan_api.vehicle_infrastructure.datasource.database.mixingplant

import de.sauter_gmbh.s_plan_api.infra_db.condition.ConditionalOnAppMode
import de.sauter_gmbh.s_plan_api.shared_core.mode.AppMode
import de.sauter_gmbh.s_plan_api.vehicle_infrastructure.datasource.database.VehicleDataSource
import de.sauter_gmbh.s_plan_api.vehicle_infrastructure.datasource.entity.VehicleEntity
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Component

private const val NOT_IMPLEMENTED_MESSAGE =
  "Vehicle data source is not implemented for mixing plant database system"

@Component
@ConditionalOnAppMode(AppMode.S_WORK)
internal class MixingPlantVehicleDataSource(private val jdbcTemplate: JdbcTemplate) :
  VehicleDataSource {

  override fun findAll(): List<VehicleEntity> = throw NotImplementedError(NOT_IMPLEMENTED_MESSAGE)

  override fun findById(id: Long): VehicleEntity? =
    throw NotImplementedError(NOT_IMPLEMENTED_MESSAGE)

  override fun update(
    id: Long,
    licensePlate: String?,
    alias: String?,
    vehicleType: Int?,
    loadCapacity: Double?,
    updatedBy: String,
    timestampMillis: Long
  ): Int = throw NotImplementedError(NOT_IMPLEMENTED_MESSAGE)
}
