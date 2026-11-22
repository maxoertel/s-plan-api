package de.sauter_gmbh.s_plan_api.location_infrastructure.repository

import de.sauter_gmbh.s_plan_api.location_domain.model.Factory
import de.sauter_gmbh.s_plan_api.location_domain.repository.FactoryRepository
import de.sauter_gmbh.s_plan_api.location_infrastructure.datasource.database.FactoryDataSource
import de.sauter_gmbh.s_plan_api.location_infrastructure.mapper.toModel
import org.springframework.stereotype.Repository

@Repository
internal class FactoryRepositoryImpl(
  private val dataSource: FactoryDataSource,
) : FactoryRepository {

  override fun findAll(): List<Factory> = dataSource.findAll().map { it.toModel() }
}
