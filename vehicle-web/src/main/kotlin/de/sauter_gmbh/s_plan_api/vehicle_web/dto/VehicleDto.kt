package de.sauter_gmbh.s_plan_api.vehicle_web.dto

import com.fasterxml.jackson.annotation.JsonProperty

internal data class VehicleDto(
  @JsonProperty("id") val id: Long,
  @JsonProperty("factory_id") val factoryId: Long?,
  @JsonProperty("factory_name") val factoryName: String?,
  @JsonProperty("license_plate") val licensePlate: String?,
  @JsonProperty("latitude") val latitude: Double?,
  @JsonProperty("longitude") val longitude: Double?,
  @JsonProperty("tracking_id") val trackingId: String?,
  @JsonProperty("alias") val alias: String?,
  @JsonProperty("driver_name") val driverName: String?,
  @JsonProperty("last_signal_timestamp") val lastSignalTimestamp: Long?,
  @JsonProperty("last_status_id") val lastStatusId: Int?,
  @JsonProperty("vehicle_type") val vehicleType: Int?,
  @JsonProperty("fleet_group_nr") val fleetGroupNr: Int?,
  @JsonProperty("fleet_group_name") val fleetGroupName: String?,
  @JsonProperty("load_capacity") val loadCapacity: Double?,
  @JsonProperty("created_at") val createdAt: Long?,
  @JsonProperty("updated_at") val updatedAt: Long?,
)
