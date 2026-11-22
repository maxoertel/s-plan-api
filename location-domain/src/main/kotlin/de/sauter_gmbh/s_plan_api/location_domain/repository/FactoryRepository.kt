package de.sauter_gmbh.s_plan_api.location_domain.repository

import de.sauter_gmbh.s_plan_api.location_domain.model.Factory

interface FactoryRepository {
  fun findAll(): List<Factory>
}
