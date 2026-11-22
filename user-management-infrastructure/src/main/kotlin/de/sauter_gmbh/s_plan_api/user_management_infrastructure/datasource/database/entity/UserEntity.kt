package de.sauter_gmbh.s_plan_api.user_management_infrastructure.datasource.database.entity

internal data class UserEntity(
  val id: String,
  val salutation: String?,
  val title: String?,
  val firstName: String?,
  val lastName: String?,
  val authType: String?,
  val authUser: String?,
  val createdAtMillis: Long?,
  val createdBy: String?,
  val updatedAtMillis: Long?,
  val updatedBy: String?,
  val role: String?,
)
