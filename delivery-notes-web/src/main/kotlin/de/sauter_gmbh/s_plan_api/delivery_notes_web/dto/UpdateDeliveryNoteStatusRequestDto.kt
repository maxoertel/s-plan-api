package de.sauter_gmbh.s_plan_api.delivery_notes_web.dto

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Times of day (`HH:mm`) for the steps of a delivery note's status timeline. A field that is absent
 * or null keeps its stored value; the endpoint cannot clear a time that is already set.
 */
internal data class UpdateDeliveryNoteStatusRequestDto(
  @JsonProperty("loading_in_progress_time") val loadingInProgressTime: String? = null,
  @JsonProperty("loading_completed_time") val loadingCompletedTime: String? = null,
  @JsonProperty("en_route_to_site_time") val enRouteToSiteTime: String? = null,
  @JsonProperty("on_site_time") val onSiteTime: String? = null,
  @JsonProperty("unloading_started_time") val unloadingStartedTime: String? = null,
  @JsonProperty("unloading_completed_time") val unloadingCompletedTime: String? = null,
  @JsonProperty("returning_time") val returningTime: String? = null,
  @JsonProperty("returned_time") val returnedTime: String? = null,
)
