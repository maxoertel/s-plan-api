package de.sauter_gmbh.s_plan_api.delivery_notes_domain.policy

import de.sauter_gmbh.s_plan_api.domain_identity.Actor
import de.sauter_gmbh.s_plan_api.domain_identity.Role

fun Actor.canListAllDeliveryNotes(): Boolean =
  when (this) {
    is Actor.User -> role == Role.ADMIN
  }

fun Actor.canViewDeliveryNoteSignature(): Boolean =
  when (this) {
    is Actor.User -> role == Role.ADMIN
  }

fun Actor.canViewDeliveryNotePdf(): Boolean =
  when (this) {
    is Actor.User -> role == Role.ADMIN
  }

fun Actor.canEditDeliveryNoteStatus(): Boolean =
  when (this) {
    is Actor.User -> role == Role.ADMIN
  }
