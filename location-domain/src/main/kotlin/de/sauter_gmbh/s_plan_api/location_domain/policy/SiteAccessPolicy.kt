package de.sauter_gmbh.s_plan_api.location_domain.policy

import de.sauter_gmbh.s_plan_api.domain_identity.Actor
import de.sauter_gmbh.s_plan_api.domain_identity.Role

fun Actor.canListSites(): Boolean =
  when (this) {
    is Actor.User -> role == Role.ADMIN
  }

fun Actor.canUpdateSitePosition(): Boolean =
  when (this) {
    is Actor.User -> role == Role.ADMIN
  }
