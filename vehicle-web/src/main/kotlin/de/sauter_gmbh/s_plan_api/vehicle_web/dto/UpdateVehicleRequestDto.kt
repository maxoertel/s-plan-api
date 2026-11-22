package de.sauter_gmbh.s_plan_api.vehicle_web.dto

import com.fasterxml.jackson.annotation.JsonProperty

internal data class UpdateVehicleRequestDto(
  @JsonProperty("license_plate") val licensePlate: String? = null,
  @JsonProperty("alias") val alias: String? = null,
  @JsonProperty("vehicle_type") val vehicleType: Int? = null,
  @JsonProperty("load_capacity") val loadCapacity: Double? = null,
)
