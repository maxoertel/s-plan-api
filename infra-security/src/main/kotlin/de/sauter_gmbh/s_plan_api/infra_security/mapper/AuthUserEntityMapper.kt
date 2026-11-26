package de.sauter_gmbh.s_plan_api.infra_security.mapper

import de.sauter_gmbh.s_plan_api.domain_identity.Actor
import de.sauter_gmbh.s_plan_api.domain_identity.Role
import de.sauter_gmbh.s_plan_api.domain_identity.UserId
import de.sauter_gmbh.s_plan_api.infra_security.datasource.database.entity.AuthUserEntity

internal fun AuthUserEntity.toModel(): Actor.User =
  Actor.User(
    userId = UserId(userId),
    role =
      Role.entries.firstOrNull { it.key == role }
        ?: throw IllegalArgumentException("Unknown role: $role")
  )
