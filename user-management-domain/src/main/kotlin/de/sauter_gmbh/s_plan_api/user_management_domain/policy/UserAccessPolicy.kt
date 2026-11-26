package de.sauter_gmbh.s_plan_api.user_management_domain.policy

import de.sauter_gmbh.s_plan_api.domain_identity.Actor
import de.sauter_gmbh.s_plan_api.domain_identity.Role

fun Actor.canListUsers(): Boolean =
  when (this) {
    is Actor.User -> role == Role.ADMIN
  }

fun Actor.canCreateUser(): Boolean =
  when (this) {
    is Actor.User -> role == Role.ADMIN
  }

fun Actor.canUpdateUser(): Boolean =
  when (this) {
    is Actor.User -> role == Role.ADMIN
  }

fun Actor.canDeleteUser(): Boolean =
  when (this) {
    is Actor.User -> role == Role.ADMIN
  }
