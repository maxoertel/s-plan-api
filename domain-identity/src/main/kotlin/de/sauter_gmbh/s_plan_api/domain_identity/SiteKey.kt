package de.sauter_gmbh.s_plan_api.domain_identity

/**
 * Identifies a site. Site numbers are only unique per customer in the legacy schema, so a site is
 * addressed by the [customerNr]/[siteNr] pair and never by [siteNr] alone.
 */
data class SiteKey(val customerNr: Long, val siteNr: Long)
