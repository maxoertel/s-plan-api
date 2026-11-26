package de.sauter_gmbh.s_plan_api.user_management_web.dto

import com.fasterxml.jackson.annotation.JsonProperty

internal data class UpdateUserRequestDto(
  @JsonProperty("salutation") val salutation: String? = null,
  @JsonProperty("title") val title: String? = null,
  @JsonProperty("first_name") val firstName: String? = null,
  @JsonProperty("last_name") val lastName: String? = null,
  @JsonProperty("auth_type") val authType: String? = null,
  @JsonProperty("auth_user") val authUser: String? = null,
  @JsonProperty("role") val role: String? = null,
)
