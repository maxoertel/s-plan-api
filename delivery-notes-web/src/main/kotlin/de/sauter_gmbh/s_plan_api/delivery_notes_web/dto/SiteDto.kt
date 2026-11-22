package de.sauter_gmbh.s_plan_api.delivery_notes_web.dto

import com.fasterxml.jackson.annotation.JsonProperty

internal data class SiteDto(
  @JsonProperty("nr") val nr: Long?,
  @JsonProperty("match_code") val matchCode: String?,
  @JsonProperty("description_1") val description1: String?,
  @JsonProperty("description_2") val description2: String?,
  @JsonProperty("description_3") val description3: String?,
  @JsonProperty("description_4") val description4: String?,
  @JsonProperty("street") val street: String?,
  @JsonProperty("house_nr") val houseNr: String?,
  @JsonProperty("country") val country: String?,
  @JsonProperty("zip") val zip: String?,
  @JsonProperty("city") val city: String?,
  @JsonProperty("contact") val contact: String?,
  @JsonProperty("note") val note: String?,
  @JsonProperty("phone") val phone: String?,
  @JsonProperty("mobile") val mobile: String?,
  @JsonProperty("latitude") val latitude: Double?,
  @JsonProperty("longitude") val longitude: Double?,
)
