package de.sauter_gmbh.s_plan_api.mission_domain.model

data class Status(
  val id: Int?,
  val label: String?,
  val color: String?,
  val loadingInProgressTimestamp: String?,
  val loadingCompletedTimestamp: String?,
  val enRouteToSiteTimestamp: String?,
  val onSiteTimestamp: String?,
  val unloadingStartedTimestamp: String?,
  val unloadingCompletedTimestamp: String?,
  val returningTimestamp: String?,
  val returnedTimestamp: String?,
)
