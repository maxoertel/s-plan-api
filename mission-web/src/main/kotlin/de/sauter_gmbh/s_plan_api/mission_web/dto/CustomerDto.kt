package de.sauter_gmbh.s_plan_api.mission_web.dto

import com.fasterxml.jackson.annotation.JsonProperty

internal data class CustomerDto(
  @JsonProperty("nr") val nr: String?,
  @JsonProperty("match_code") val matchCode: String?,
  @JsonProperty("name_1") val name1: String?,
  @JsonProperty("name_2") val name2: String?,
  @JsonProperty("name_3") val name3: String?,
  @JsonProperty("name_4") val name4: String?,
  @JsonProperty("street") val street: String?,
  @JsonProperty("house_nr") val houseNr: String?,
  @JsonProperty("country") val country: String?,
  @JsonProperty("zip") val zip: String?,
  @JsonProperty("city") val city: String?,
  @JsonProperty("phone") val phone: String?,
  @JsonProperty("fax") val fax: String?,
  @JsonProperty("mobile") val mobile: String?,
  @JsonProperty("email_1") val email1: String?,
  @JsonProperty("email_2") val email2: String?,
  @JsonProperty("note") val note: String?,
)
