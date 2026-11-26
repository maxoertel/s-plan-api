package de.sauter_gmbh.s_plan_api.application_security.provider

import de.sauter_gmbh.s_plan_api.domain_identity.Actor

interface ActorProvider {
  fun get(): Actor
}
