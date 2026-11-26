package de.sauter_gmbh.s_plan_api.vehicle_domain.policy

import de.sauter_gmbh.s_plan_api.domain_identity.Actor
import de.sauter_gmbh.s_plan_api.domain_identity.Role

fun Actor.canListAllVehicles(): Boolean =
  when (this) {
    is Actor.User -> role == Role.ADMIN
  }

fun Actor.canUpdateVehicle(): Boolean =
  when (this) {
    is Actor.User -> role == Role.ADMIN
  }
