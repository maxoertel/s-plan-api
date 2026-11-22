package de.sauter_gmbh.s_plan_api.location_infrastructure.datasource.database

import de.sauter_gmbh.s_plan_api.location_infrastructure.datasource.entity.FactoryEntity

internal interface FactoryDataSource {
  fun findAll(): List<FactoryEntity>
}
