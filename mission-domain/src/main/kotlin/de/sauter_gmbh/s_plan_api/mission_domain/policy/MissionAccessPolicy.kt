package de.sauter_gmbh.s_plan_api.mission_domain.policy

import de.sauter_gmbh.s_plan_api.domain_identity.Actor
import de.sauter_gmbh.s_plan_api.domain_identity.Role

fun Actor.canListAllMissions(): Boolean =
  when (this) {
    is Actor.User -> role == Role.ADMIN
  }
