package de.sauter_gmbh.s_plan_api.delivery_notes_infrastructure.mapper

import de.sauter_gmbh.s_plan_api.delivery_notes_domain.model.StatusTime
import de.sauter_gmbh.s_plan_api.delivery_notes_domain.model.StatusUpdate
import java.time.LocalTime

private val COLUMN_NAMES =
  mapOf(
    StatusTime.LOADING_IN_PROGRESS to "fzstatusbeladung",
    StatusTime.LOADING_COMPLETED to "fzstatusbeladungende",
    StatusTime.EN_ROUTE_TO_SITE to "fzstatushinfahrt",
    StatusTime.ON_SITE to "fzstatusbaustelle",
    StatusTime.UNLOADING_STARTED to "fzstatusentleerung",
    StatusTime.UNLOADING_COMPLETED to "fzstatusentleerungende",
    StatusTime.RETURNING to "fzstatusrueckfahrt",
    StatusTime.RETURNED to "fzstatusaufwerk",
  )

internal fun StatusUpdate.toColumnTimes(): Map<String, LocalTime> =
  times.entries.associate { (statusTime, time) -> COLUMN_NAMES.getValue(statusTime) to time }
