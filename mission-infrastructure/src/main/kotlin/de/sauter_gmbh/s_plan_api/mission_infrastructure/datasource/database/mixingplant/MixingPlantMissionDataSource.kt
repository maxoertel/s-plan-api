package de.sauter_gmbh.s_plan_api.mission_infrastructure.datasource.database.mixingplant

import de.sauter_gmbh.s_plan_api.infra_db.condition.ConditionalOnAppMode
import de.sauter_gmbh.s_plan_api.mission_infrastructure.datasource.database.MissionDataSource
import de.sauter_gmbh.s_plan_api.mission_infrastructure.datasource.entity.MissionEntity
import de.sauter_gmbh.s_plan_api.mission_infrastructure.datasource.entity.SiteKeyEntity
import de.sauter_gmbh.s_plan_api.shared_core.mode.AppMode
import java.time.LocalDate
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Component

private const val NOT_IMPLEMENTED_MESSAGE =
  "Mission data source is not implemented for mixing plant database system"

@Component
@ConditionalOnAppMode(AppMode.S_WORK)
internal class MixingPlantMissionDataSource(private val jdbcTemplate: JdbcTemplate) :
  MissionDataSource {

  override fun findByDate(date: LocalDate): List<MissionEntity> =
    throw NotImplementedError(NOT_IMPLEMENTED_MESSAGE)

  override fun findSiteKeysOnDate(date: LocalDate): Set<SiteKeyEntity> =
    throw NotImplementedError(NOT_IMPLEMENTED_MESSAGE)
}
