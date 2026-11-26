package de.sauter_gmbh.s_plan_api.mission_web.dto

import com.fasterxml.jackson.annotation.JsonProperty

internal data class StatusDto(
  @JsonProperty("id") val id: Int?,
  @JsonProperty("label") val label: String?,
  @JsonProperty("color") val color: String?,
  @JsonProperty("loading_in_progress_timestamp") val loadingInProgressTimestamp: String?,
  @JsonProperty("loading_completed_timestamp") val loadingCompletedTimestamp: String?,
  @JsonProperty("en_route_to_site_timestamp") val enRouteToSiteTimestamp: String?,
  @JsonProperty("on_site_timestamp") val onSiteTimestamp: String?,
  @JsonProperty("unloading_started_timestamp") val unloadingStartedTimestamp: String?,
  @JsonProperty("unloading_completed_timestamp") val unloadingCompletedTimestamp: String?,
  @JsonProperty("returning_timestamp") val returningTimestamp: String?,
  @JsonProperty("returned_timestamp") val returnedTimestamp: String?,
)
