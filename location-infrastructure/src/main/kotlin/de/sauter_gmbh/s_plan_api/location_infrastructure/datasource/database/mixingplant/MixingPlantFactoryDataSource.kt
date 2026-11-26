package de.sauter_gmbh.s_plan_api.location_infrastructure.datasource.database.mixingplant

import de.sauter_gmbh.s_plan_api.infra_db.condition.ConditionalOnAppMode
import de.sauter_gmbh.s_plan_api.location_infrastructure.datasource.database.FactoryDataSource
import de.sauter_gmbh.s_plan_api.location_infrastructure.datasource.entity.FactoryEntity
import de.sauter_gmbh.s_plan_api.shared_core.mode.AppMode
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Component

private const val NOT_IMPLEMENTED_MESSAGE =
  "Factory data source is not implemented for mixing plant database system"

@Component
@ConditionalOnAppMode(AppMode.S_WORK)
internal class MixingPlantFactoryDataSource(private val jdbcTemplate: JdbcTemplate) :
  FactoryDataSource {

  override fun findAll(): List<FactoryEntity> = throw NotImplementedError(NOT_IMPLEMENTED_MESSAGE)
}
