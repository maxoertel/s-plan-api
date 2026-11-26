package de.sauter_gmbh.s_plan_api.mission_web.mapper

import de.sauter_gmbh.s_plan_api.mission_domain.model.Customer
import de.sauter_gmbh.s_plan_api.mission_domain.model.FactorySummary
import de.sauter_gmbh.s_plan_api.mission_domain.model.Mission
import de.sauter_gmbh.s_plan_api.mission_domain.model.Signature
import de.sauter_gmbh.s_plan_api.mission_domain.model.SiteSummary
import de.sauter_gmbh.s_plan_api.mission_domain.model.Status
import de.sauter_gmbh.s_plan_api.mission_domain.model.UnsignedMissionSummary
import de.sauter_gmbh.s_plan_api.mission_web.dto.CustomerDto
import de.sauter_gmbh.s_plan_api.mission_web.dto.FactorySummaryDto
import de.sauter_gmbh.s_plan_api.mission_web.dto.MissionDto
import de.sauter_gmbh.s_plan_api.mission_web.dto.SignatureDto
import de.sauter_gmbh.s_plan_api.mission_web.dto.SiteSummaryDto
import de.sauter_gmbh.s_plan_api.mission_web.dto.StatusDto
import de.sauter_gmbh.s_plan_api.mission_web.dto.UnsignedMissionSummaryDto

internal fun Mission.toDto(): MissionDto =
  MissionDto(
    id = id.value,
    orderNr = orderNr,
    loadNr = loadNr,
    deliveryNr = deliveryNr,
    deliveryNrExternal = deliveryNrExternal,
    date = date.toString(),
    orderType = orderType,
    deliveryType = deliveryType,
    articleNr = articleNr,
    recipe = recipe,
    plannedQuantity = plannedQuantity,
    actualQuantity = actualQuantity,
    loadQuantity = loadQuantity,
    note1 = note1,
    note2 = note2,
    vehicleId = vehicleId?.value?.toString(),
    vehicleLicensePlate = vehicleLicensePlate,
    driver = driver,
    driverNote = driverNote,
    driverStartedAt = driverStartedAt,
    driverAcceptedAt = driverAcceptedAt,
    time = time,
    dispatchTime = dispatchTime,
    deliveryTime = deliveryTime,
    nextTime = nextTime,
    factorySummary = factorySummary.toDto(),
    returnFactorySummary = returnFactorySummary?.toDto(),
    siteSummary = siteSummary?.toDto(),
    customer = customer.toDto(),
    status = status.toDto(),
    signature = signature.toDto(),
    waterCorrectionUnloading = waterCorrectionUnloading,
    superPlasticizerUnloading = superPlasticizerUnloading,
    unsignedPreviousMissions = unsignedPreviousMissions.map { it.toDto() },
    deliveryNotePdfExists = deliveryNotePdfExists,
  )

internal fun UnsignedMissionSummary.toDto(): UnsignedMissionSummaryDto =
  UnsignedMissionSummaryDto(
    id = id.value,
    driverSignatureSet = driverSignatureSet,
    customerSignatureSet = customerSignatureSet,
    customerSignatureCollective = customerSignatureCollective,
    orderNr = orderNr,
    loadNr = loadNr,
    deliveryNr = deliveryNr,
    deliveryNrExternal = deliveryNrExternal,
    date = date.toString(),
    articleNr = articleNr,
    recipe = recipe,
    plannedQuantity = plannedQuantity,
    actualQuantity = actualQuantity,
    loadQuantity = loadQuantity,
    waterCorrectionUnloading = waterCorrectionUnloading,
    status = status.toDto(),
  )

internal fun FactorySummary.toDto(): FactorySummaryDto =
  FactorySummaryDto(
    id = id,
    name = name,
    latitude = latitude,
    longitude = longitude,
    radiusInMeter = radiusInMeter,
    street = street,
    houseNr = houseNr,
    city = city,
    zip = zip,
    country = country,
    phone = phone,
  )

internal fun SiteSummary.toDto(): SiteSummaryDto =
  SiteSummaryDto(
    id = id,
    name = name,
    latitude = latitude,
    longitude = longitude,
    radiusInMeter = radiusInMeter,
    description1 = description1,
    description2 = description2,
    description3 = description3,
    description4 = description4,
    matchCode = matchCode,
    street = street,
    houseNr = houseNr,
    city = city,
    zip = zip,
    country = country,
    phone = phone,
    deliveryNoteInfo = deliveryNoteInfo,
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
    fax = fax,
    mobile = mobile,
    email1 = email1,
    email2 = email2,
    note = note,
  )

internal fun Status.toDto(): StatusDto =
  StatusDto(
    id = id,
    label = label,
    color = color,
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
    driverSignatureSet = driverSignatureSet,
    driverSignatureTimestampMillis = driverSignatureTimestampMillis,
    customerSignatureSet = customerSignatureSet,
    customerSignatureTimestampMillis = customerSignatureTimestampMillis,
  )
