package de.sauter_gmbh.s_plan_api.domain_identity

sealed interface Actor {
  data class User(val userId: UserId, val role: Role) : Actor
}
