package de.sauter_gmbh.s_plan_api.location_web.dto

import com.fasterxml.jackson.annotation.JsonProperty

internal data class UpdateSitePositionRequestDto(
  @JsonProperty("latitude") val latitude: Double,
  @JsonProperty("longitude") val longitude: Double,
  @JsonProperty("radius_in_meter") val radiusInMeter: Int,
)
