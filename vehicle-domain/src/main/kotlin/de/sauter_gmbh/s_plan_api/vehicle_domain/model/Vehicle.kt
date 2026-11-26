package de.sauter_gmbh.s_plan_api.vehicle_domain.model

import de.sauter_gmbh.s_plan_api.domain_identity.VehicleId

data class Vehicle(
  val id: VehicleId,
  val factoryId: Long?,
  val factoryName: String?,
  val licensePlate: String?,
  val latitude: Double?,
  val longitude: Double?,
  val trackingId: String?,
  val alias: String?,
  val driverName: String?,
  val lastSignalTimestamp: Long?,
  val lastStatusId: Int?,
  val vehicleType: Int?,
  val fleetGroupNr: Int?,
  val fleetGroupName: String?,
  val loadCapacity: Double?,
  val createdAt: Long?,
  val updatedAt: Long?,
)
