package de.sauter_gmbh.s_plan_api.delivery_notes_domain.model

data class Status(
  val loadingInProgressTimestamp: String?,
  val loadingCompletedTimestamp: String?,
  val enRouteToSiteTimestamp: String?,
  val onSiteTimestamp: String?,
  val unloadingStartedTimestamp: String?,
  val unloadingCompletedTimestamp: String?,
  val returningTimestamp: String?,
  val returnedTimestamp: String?,
)
