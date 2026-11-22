package de.sauter_gmbh.s_plan_api.infra_security.datasource.database.entity

internal data class AuthUserEntity(
  val userId: String,
  val role: String,
)
