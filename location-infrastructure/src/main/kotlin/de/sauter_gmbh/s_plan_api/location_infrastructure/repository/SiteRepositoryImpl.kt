package de.sauter_gmbh.s_plan_api.location_infrastructure.repository

import de.sauter_gmbh.s_plan_api.domain_identity.SiteKey
import de.sauter_gmbh.s_plan_api.location_domain.model.Site
import de.sauter_gmbh.s_plan_api.location_domain.model.SitePosition
import de.sauter_gmbh.s_plan_api.location_domain.repository.SiteRepository
import de.sauter_gmbh.s_plan_api.location_infrastructure.datasource.database.SiteDataSource
import de.sauter_gmbh.s_plan_api.location_infrastructure.mapper.toEntity
import de.sauter_gmbh.s_plan_api.location_infrastructure.mapper.toModel
import org.springframework.stereotype.Repository

@Repository
internal class SiteRepositoryImpl(private val dataSource: SiteDataSource) : SiteRepository {

  override fun findAll(): List<Site> = dataSource.findAll().map { it.toModel() }

  override fun findBySiteKeys(siteKeys: Collection<SiteKey>): List<Site> {
    if (siteKeys.isEmpty()) return emptyList()
    return dataSource.findBySiteKeys(siteKeys.map { it.toEntity() }).map { it.toModel() }
  }

  override fun updatePosition(siteKey: SiteKey, position: SitePosition): Site? {
    val key = siteKey.toEntity()
    val updatedRows = dataSource.updatePosition(key, position.toEntity())
    if (updatedRows == 0) return null

    return dataSource.findBySiteKeys(listOf(key)).firstOrNull()?.toModel()
  }
}
