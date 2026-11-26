package de.sauter_gmbh.s_plan_api.location_infrastructure.datasource.database.mixingplant

import de.sauter_gmbh.s_plan_api.infra_db.condition.ConditionalOnAppMode
import de.sauter_gmbh.s_plan_api.location_infrastructure.datasource.database.SiteDataSource
import de.sauter_gmbh.s_plan_api.location_infrastructure.datasource.entity.SiteEntity
import de.sauter_gmbh.s_plan_api.location_infrastructure.datasource.entity.SiteKeyEntity
import de.sauter_gmbh.s_plan_api.location_infrastructure.datasource.entity.SitePositionEntity
import de.sauter_gmbh.s_plan_api.shared_core.mode.AppMode
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Component

private const val NOT_IMPLEMENTED_MESSAGE =
  "Site data source is not implemented for mixing plant database system"

@Component
@ConditionalOnAppMode(AppMode.S_WORK)
internal class MixingPlantSiteDataSource(private val jdbcTemplate: JdbcTemplate) : SiteDataSource {

  override fun findAll(): List<SiteEntity> = throw NotImplementedError(NOT_IMPLEMENTED_MESSAGE)

  override fun findBySiteKeys(siteKeys: Collection<SiteKeyEntity>): List<SiteEntity> =
    throw NotImplementedError(NOT_IMPLEMENTED_MESSAGE)

  override fun updatePosition(siteKey: SiteKeyEntity, position: SitePositionEntity): Int =
    throw NotImplementedError(NOT_IMPLEMENTED_MESSAGE)
}
