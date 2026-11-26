package de.sauter_gmbh.s_plan_api.delivery_notes_domain.model

import java.time.LocalTime

/**
 * The times an administrator corrects on a delivery note's status timeline.
 *
 * Only the steps contained in [times] are written; a step that is absent keeps its stored value.
 */
data class StatusUpdate(val times: Map<StatusTime, LocalTime>)
