package de.sauter_gmbh.s_plan_api.location_domain.repository

import de.sauter_gmbh.s_plan_api.domain_identity.SiteKey
import de.sauter_gmbh.s_plan_api.location_domain.model.Site
import de.sauter_gmbh.s_plan_api.location_domain.model.SitePosition

interface SiteRepository {
  fun findAll(): List<Site>

  fun findBySiteKeys(siteKeys: Collection<SiteKey>): List<Site>

  fun updatePosition(siteKey: SiteKey, position: SitePosition): Site?
}
