package de.sauter_gmbh.s_plan_api.user_management_domain.model

import de.sauter_gmbh.s_plan_api.domain_identity.UserId

data class User(
  val id: UserId,
  val salutation: String?,
  val title: String?,
  val firstName: String?,
  val lastName: String?,
  val authType: String?,
  val authUser: String?,
  val role: String?,
  val createdAt: Long?,
  val createdBy: String?,
  val updatedAt: Long?,
  val updatedBy: String?,
)
