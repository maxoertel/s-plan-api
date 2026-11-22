package de.sauter_gmbh.s_plan_api.mission_domain.model

import java.time.LocalDate

data class UnsignedMissionSummary(
  val id: MissionId,
  val driverSignatureSet: Boolean,
  val customerSignatureSet: Boolean,
  val customerSignatureCollective: Boolean,
  val orderNr: Int,
  val loadNr: Int,
  val deliveryNr: Int,
  val deliveryNrExternal: String?,
  val date: LocalDate,
  val articleNr: String?,
  val recipe: String?,
  val plannedQuantity: String?,
  val actualQuantity: String?,
  val loadQuantity: String?,
  val waterCorrectionUnloading: Float?,
  val status: Status,
)
