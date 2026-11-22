package de.sauter_gmbh.s_plan_api.location_web.dto

import com.fasterxml.jackson.annotation.JsonProperty

internal data class FactoryDto(
  @JsonProperty("id") val id: Long,
  @JsonProperty("match_code") val matchCode: String?,
  @JsonProperty("name") val name: String?,
  @JsonProperty("street") val street: String?,
  @JsonProperty("house_number") val houseNumber: String?,
  @JsonProperty("zip") val zip: String?,
  @JsonProperty("city") val city: String?,
  @JsonProperty("country") val country: String?,
  @JsonProperty("phone") val phone: String?,
  @JsonProperty("latitude") val latitude: Double?,
  @JsonProperty("longitude") val longitude: Double?,
  @JsonProperty("radius_in_meter") val radiusInMeter: Int?,
)
