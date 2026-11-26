package de.sauter_gmbh.s_plan_api.delivery_notes_web.mapper

import de.sauter_gmbh.s_plan_api.delivery_notes_domain.model.Customer
import de.sauter_gmbh.s_plan_api.delivery_notes_domain.model.DeliveryNote
import de.sauter_gmbh.s_plan_api.delivery_notes_domain.model.DeliveryNotePdf
import de.sauter_gmbh.s_plan_api.delivery_notes_domain.model.DeliveryNotePdfKind
import de.sauter_gmbh.s_plan_api.delivery_notes_domain.model.Signature
import de.sauter_gmbh.s_plan_api.delivery_notes_domain.model.Site
import de.sauter_gmbh.s_plan_api.delivery_notes_domain.model.Status
import de.sauter_gmbh.s_plan_api.delivery_notes_domain.model.StatusTime
import de.sauter_gmbh.s_plan_api.delivery_notes_domain.model.StatusUpdate
import de.sauter_gmbh.s_plan_api.delivery_notes_web.dto.CustomerDto
import de.sauter_gmbh.s_plan_api.delivery_notes_web.dto.DeliveryNoteDto
import de.sauter_gmbh.s_plan_api.delivery_notes_web.dto.DeliveryNotePdfDto
import de.sauter_gmbh.s_plan_api.delivery_notes_web.dto.SignatureDto
import de.sauter_gmbh.s_plan_api.delivery_notes_web.dto.SiteDto
import de.sauter_gmbh.s_plan_api.delivery_notes_web.dto.StatusDto
import de.sauter_gmbh.s_plan_api.delivery_notes_web.dto.UpdateDeliveryNoteStatusRequestDto
import java.time.LocalTime
import java.time.format.DateTimeParseException

internal fun DeliveryNote.toDto(): DeliveryNoteDto =
  DeliveryNoteDto(
    id = id.value,
    factoryNr = factoryNr,
    date = date,
    time = time,
    deliveryNoteNr = deliveryNoteNr,
    deliveryNoteNrExternal = deliveryNoteNrExternal,
    deliveryNoteCircle = deliveryNoteCircle,
    printType = printType,
    orderType = orderType,
    deliveryDateTime = deliveryDateTime,
    orderNr = orderNr,
    loadNr = loadNr,
    batchNr = batchNr,
    plannedQuantity = plannedQuantity,
    actualQuantity = actualQuantity,
    loadQuantity = loadQuantity,
    returnQuantity = returnQuantity,
    customer = customer.toDto(),
    site = site.toDto(),
    vehicleId = vehicleId?.value,
    recipe = recipe,
    articleNr = articleNr,
    description = description,
    status = status.toDto(),
    signature = signature.toDto(),
    createdAt = createdAt,
    updatedAt = updatedAt,
    waterCorrectionUnloading = waterCorrectionUnloading,
    superPlasticizerUnloading = superPlasticizerUnloading,
    waitingTimeMinutes = waitingTimeMinutes,
    driverAcceptedAt = driverAcceptedAt,
  )

internal fun Customer.toDto(): CustomerDto =
  CustomerDto(
    nr = nr,
    matchCode = matchCode,
    name1 = name1,
    name2 = name2,
    name3 = name3,
    name4 = name4,
    street = street,
    houseNr = houseNr,
    country = country,
    zip = zip,
    city = city,
    phone = phone,
    mobile = mobile,
    email1 = email1,
    email2 = email2,
    note = note,
    blockCode = blockCode,
  )

internal fun Site.toDto(): SiteDto =
  SiteDto(
    nr = nr,
    matchCode = matchCode,
    description1 = description1,
    description2 = description2,
    description3 = description3,
    description4 = description4,
    street = street,
    houseNr = houseNr,
    country = country,
    zip = zip,
    city = city,
    contact = contact,
    note = note,
    phone = phone,
    mobile = mobile,
    latitude = latitude,
    longitude = longitude,
  )

internal fun Status.toDto(): StatusDto =
  StatusDto(
    loadingInProgressTimestamp = loadingInProgressTimestamp,
    loadingCompletedTimestamp = loadingCompletedTimestamp,
    enRouteToSiteTimestamp = enRouteToSiteTimestamp,
    onSiteTimestamp = onSiteTimestamp,
    unloadingStartedTimestamp = unloadingStartedTimestamp,
    unloadingCompletedTimestamp = unloadingCompletedTimestamp,
    returningTimestamp = returningTimestamp,
    returnedTimestamp = returnedTimestamp,
  )

internal fun Signature.toDto(): SignatureDto =
  SignatureDto(
    deviceId = deviceId,
    status = status,
    driverSignatureSet = driverSignatureSet,
    driverSignaturePlaceholder = driverSignaturePlaceholder,
    driverSignatureTimestampMillis = driverSignatureTimestampMillis,
    customerSignatureSet = customerSignatureSet,
    customerSignatureTimestampMillis = customerSignatureTimestampMillis,
    customerSignaturePlaceholder = customerSignaturePlaceholder,
    customerSignatureCollective = customerSignatureCollective,
  )

internal fun DeliveryNotePdf.toDto(): DeliveryNotePdfDto =
  DeliveryNotePdfDto(
    id = id.value,
    factoryNr = factoryNr,
    date = date.toString(),
    time = time.toString(),
    deliveryNoteNr = deliveryNoteNr,
    deliveryNoteCircle = deliveryNoteCircle,
    creationDate = creationDate.toString(),
    creationTime = creationTime.toString(),
    storageDate = storageDate.toString(),
    storageTime = storageTime.toString(),
    kind =
      when (kind) {
        DeliveryNotePdfKind.FINAL -> "final"
        DeliveryNotePdfKind.PRE_PRINT -> "pre_print"
      },
    waterCorrectionUnloading = waterCorrectionUnloading,
  )

internal fun UpdateDeliveryNoteStatusRequestDto.toModel(): StatusUpdate =
  StatusUpdate(
    times =
      buildMap<StatusTime, LocalTime> {
        putTime(StatusTime.LOADING_IN_PROGRESS, loadingInProgressTime)
        putTime(StatusTime.LOADING_COMPLETED, loadingCompletedTime)
        putTime(StatusTime.EN_ROUTE_TO_SITE, enRouteToSiteTime)
        putTime(StatusTime.ON_SITE, onSiteTime)
        putTime(StatusTime.UNLOADING_STARTED, unloadingStartedTime)
        putTime(StatusTime.UNLOADING_COMPLETED, unloadingCompletedTime)
        putTime(StatusTime.RETURNING, returningTime)
        putTime(StatusTime.RETURNED, returnedTime)
      }
  )

// A blank value is treated like an absent one: clearing a stored timestamp is not offered.
private fun MutableMap<StatusTime, LocalTime>.putTime(statusTime: StatusTime, value: String?) {
  val rawTime = value?.trim()?.takeIf { it.isNotEmpty() } ?: return
  put(statusTime, parseTime(statusTime, rawTime))
}

private fun parseTime(statusTime: StatusTime, value: String): LocalTime =
  try {
    LocalTime.parse(value)
  } catch (e: DateTimeParseException) {
    throw IllegalArgumentException("Invalid time for $statusTime: $value", e)
  }
