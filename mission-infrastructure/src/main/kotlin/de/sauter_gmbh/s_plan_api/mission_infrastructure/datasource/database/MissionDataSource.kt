package de.sauter_gmbh.s_plan_api.mission_infrastructure.datasource.database

import de.sauter_gmbh.s_plan_api.mission_infrastructure.datasource.entity.MissionEntity
import de.sauter_gmbh.s_plan_api.mission_infrastructure.datasource.entity.SiteKeyEntity
import java.time.LocalDate

internal interface MissionDataSource {
  fun findByDate(date: LocalDate): List<MissionEntity>

  fun findSiteKeysOnDate(date: LocalDate): Set<SiteKeyEntity>
}
