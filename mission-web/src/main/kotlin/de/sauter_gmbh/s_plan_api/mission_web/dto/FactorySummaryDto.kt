package de.sauter_gmbh.s_plan_api.mission_web.dto

import com.fasterxml.jackson.annotation.JsonProperty

internal data class FactorySummaryDto(
  @JsonProperty("id") val id: Long,
  @JsonProperty("name") val name: String?,
  @JsonProperty("latitude") val latitude: Double?,
  @JsonProperty("longitude") val longitude: Double?,
  @JsonProperty("radius_in_meter") val radiusInMeter: Int?,
  @JsonProperty("street") val street: String?,
  @JsonProperty("house_nr") val houseNr: String?,
  @JsonProperty("city") val city: String?,
  @JsonProperty("zip") val zip: String?,
  @JsonProperty("country") val country: String?,
  @JsonProperty("phone") val phone: String?,
)
