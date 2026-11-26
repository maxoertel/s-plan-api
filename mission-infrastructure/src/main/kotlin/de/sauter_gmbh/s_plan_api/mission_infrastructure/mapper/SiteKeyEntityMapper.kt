package de.sauter_gmbh.s_plan_api.mission_infrastructure.mapper

import de.sauter_gmbh.s_plan_api.domain_identity.SiteKey
import de.sauter_gmbh.s_plan_api.mission_infrastructure.datasource.entity.SiteKeyEntity

internal fun SiteKeyEntity.toModel(): SiteKey = SiteKey(customerNr = customerNr, siteNr = siteNr)
