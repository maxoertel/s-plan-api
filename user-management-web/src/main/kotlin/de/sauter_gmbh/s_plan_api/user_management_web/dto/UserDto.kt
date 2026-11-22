package de.sauter_gmbh.s_plan_api.user_management_web.dto

import com.fasterxml.jackson.annotation.JsonProperty

internal data class UserDto(
  @JsonProperty("id") val id: String,
  @JsonProperty("salutation") val salutation: String?,
  @JsonProperty("title") val title: String?,
  @JsonProperty("first_name") val firstName: String?,
  @JsonProperty("last_name") val lastName: String?,
  @JsonProperty("auth_type") val authType: String?,
  @JsonProperty("auth_user") val authUser: String?,
  @JsonProperty("role") val role: String?,
  @JsonProperty("created_at") val createdAt: Long?,
  @JsonProperty("created_by") val createdBy: String?,
  @JsonProperty("updated_at") val updatedAt: Long?,
  @JsonProperty("updated_by") val updatedBy: String?,
)
