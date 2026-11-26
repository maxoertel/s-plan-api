package de.sauter_gmbh.s_plan_api.location_infrastructure.datasource.database

import de.sauter_gmbh.s_plan_api.location_infrastructure.datasource.entity.SiteEntity
import de.sauter_gmbh.s_plan_api.location_infrastructure.datasource.entity.SiteKeyEntity
import de.sauter_gmbh.s_plan_api.location_infrastructure.datasource.entity.SitePositionEntity

internal interface SiteDataSource {
  fun findAll(): List<SiteEntity>

  fun findBySiteKeys(siteKeys: Collection<SiteKeyEntity>): List<SiteEntity>

  fun updatePosition(siteKey: SiteKeyEntity, position: SitePositionEntity): Int
}
