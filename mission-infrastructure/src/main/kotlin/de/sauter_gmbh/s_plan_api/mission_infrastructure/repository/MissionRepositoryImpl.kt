package de.sauter_gmbh.s_plan_api.mission_infrastructure.repository

import de.sauter_gmbh.s_plan_api.domain_identity.SiteKey
import de.sauter_gmbh.s_plan_api.mission_domain.model.Mission
import de.sauter_gmbh.s_plan_api.mission_domain.repository.MissionRepository
import de.sauter_gmbh.s_plan_api.mission_infrastructure.datasource.database.MissionDataSource
import de.sauter_gmbh.s_plan_api.mission_infrastructure.mapper.toModel
import java.time.LocalDate
import org.springframework.stereotype.Repository

@Repository
internal class MissionRepositoryImpl(private val dataSource: MissionDataSource) :
  MissionRepository {

  override fun findByDate(date: LocalDate): List<Mission> =
    dataSource.findByDate(date).map { it.toModel() }

  override fun findSiteKeysWithMissionsOnDate(date: LocalDate): Set<SiteKey> =
    dataSource.findSiteKeysOnDate(date).map { it.toModel() }.toSet()
}
