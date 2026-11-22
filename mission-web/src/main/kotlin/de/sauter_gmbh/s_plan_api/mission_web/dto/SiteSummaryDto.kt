package de.sauter_gmbh.s_plan_api.mission_web.dto

import com.fasterxml.jackson.annotation.JsonProperty

internal data class SiteSummaryDto(
  @JsonProperty("id") val id: Long,
  @JsonProperty("name") val name: String?,
  @JsonProperty("latitude") val latitude: Double?,
  @JsonProperty("longitude") val longitude: Double?,
  @JsonProperty("radius_in_meter") val radiusInMeter: Int?,
  @JsonProperty("description1") val description1: String?,
  @JsonProperty("description2") val description2: String?,
  @JsonProperty("description3") val description3: String?,
  @JsonProperty("description4") val description4: String?,
  @JsonProperty("match_code") val matchCode: String?,
  @JsonProperty("street") val street: String?,
  @JsonProperty("house_nr") val houseNr: String?,
  @JsonProperty("city") val city: String?,
  @JsonProperty("zip") val zip: String?,
  @JsonProperty("country") val country: String?,
  @JsonProperty("phone") val phone: String?,
  @JsonProperty("delivery_note_info") val deliveryNoteInfo: String?,
)
