package de.sauter_gmbh.s_plan_api.mission_infrastructure.datasource.entity

import java.time.LocalDate

internal data class UnsignedMissionSummaryEntity(
  val id: String,
  val orderNr: Int,
  val loadNr: Int,
  val deliveryNr: Long,
  val deliveryNrExternal: Long?,
  val date: LocalDate,
  val articleNr: String?,
  val recipe: String?,
  val plannedQuantity: Double?,
  val actualQuantity: Double?,
  val loadQuantity: Double?,
  val driverSignatureSet: Boolean,
  val customerSignatureSet: Boolean,
  val customerSignatureCollective: Boolean,
  val waterCorrectionUnloading: Double?,
  val statusId: Int?,
  val statusLoadingInProgressTimestamp: String?,
  val statusLoadingCompletedTimestamp: String?,
  val statusEnRouteToSiteTimestamp: String?,
  val statusOnSiteTimestamp: String?,
  val statusUnloadingStartedTimestamp: String?,
  val statusUnloadingCompletedTimestamp: String?,
  val statusReturningTimestamp: String?,
  val statusReturnedTimestamp: String?,
)
