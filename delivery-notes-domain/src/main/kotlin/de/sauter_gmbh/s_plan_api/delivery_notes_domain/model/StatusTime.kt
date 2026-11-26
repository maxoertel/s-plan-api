package de.sauter_gmbh.s_plan_api.delivery_notes_domain.model

/** The steps of a delivery note's status timeline. */
enum class StatusTime {
  LOADING_IN_PROGRESS,
  LOADING_COMPLETED,
  EN_ROUTE_TO_SITE,
  ON_SITE,
  UNLOADING_STARTED,
  UNLOADING_COMPLETED,
  RETURNING,
  RETURNED,
}
