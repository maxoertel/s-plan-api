package de.sauter_gmbh.s_plan_api.vehicle_infrastructure.datasource.entity

internal data class VehicleEntity(
  val id: Long,
  val factoryId: Long?,
  val factoryName: String?,
  val licensePlate: String?,
  val latitude: String?,
  val longitude: String?,
  val trackingId: String?,
  val alias: String?,
  val driverFirstName: String?,
  val driverLastName: String?,
  val lastSignalTimestamp: Long?,
  val lastStatusId: Int?,
  val vehicleType: Int?,
  val fleetGroupNr: Int?,
  val fleetGroupName: String?,
  val loadCapacity: Double?,
  val createdAt: Long?,
  val updatedAt: Long?,
)
