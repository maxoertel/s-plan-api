package de.sauter_gmbh.s_plan_api.mission_domain.repository

import de.sauter_gmbh.s_plan_api.domain_identity.SiteKey
import de.sauter_gmbh.s_plan_api.mission_domain.model.Mission
import java.time.LocalDate

interface MissionRepository {
  /** All missions on [date], regardless of vehicle. */
  fun findByDate(date: LocalDate): List<Mission>

  /** Keys of the sites that have at least one mission on [date]. */
  fun findSiteKeysWithMissionsOnDate(date: LocalDate): Set<SiteKey>
}
