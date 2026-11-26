package de.sauter_gmbh.s_plan_api.mission_infrastructure.mapper

import de.sauter_gmbh.s_plan_api.domain_identity.VehicleId
import de.sauter_gmbh.s_plan_api.infra_db.parser.CoordinateKind
import de.sauter_gmbh.s_plan_api.infra_db.parser.parseLegacyCoordinate
import de.sauter_gmbh.s_plan_api.mission_domain.model.Customer
import de.sauter_gmbh.s_plan_api.mission_domain.model.DeliveryType
import de.sauter_gmbh.s_plan_api.mission_domain.model.FactorySummary
import de.sauter_gmbh.s_plan_api.mission_domain.model.Mission
import de.sauter_gmbh.s_plan_api.mission_domain.model.MissionId
import de.sauter_gmbh.s_plan_api.mission_domain.model.Signature
import de.sauter_gmbh.s_plan_api.mission_domain.model.SiteSummary
import de.sauter_gmbh.s_plan_api.mission_domain.model.Status
import de.sauter_gmbh.s_plan_api.mission_domain.model.UnsignedMissionSummary
import de.sauter_gmbh.s_plan_api.mission_infrastructure.datasource.entity.MissionEntity
import de.sauter_gmbh.s_plan_api.mission_infrastructure.datasource.entity.UnsignedMissionSummaryEntity

internal fun MissionEntity.toModel(): Mission =
  Mission(
    id = MissionId(id),
    orderNr = orderNr,
    loadNr = loadNr,
    deliveryNr = deliveryNr.toInt(),
    deliveryNrExternal = deliveryNrExternal?.toString(),
    date = date,
    orderType = orderType?.toString(),
    deliveryType = deliveryType?.toDeliveryType(),
    articleNr = articleNr,
    recipe = recipe,
    plannedQuantity = plannedQuantity?.toString(),
    actualQuantity = actualQuantity?.toString(),
    loadQuantity = loadQuantity?.toString(),
    note1 = note1,
    note2 = note2,
    vehicleId = vehicleId?.let { VehicleId(it) },
    vehicleLicensePlate = vehicleLicensePlate,
    driver = driver,
    driverNote = driverNote,
    driverStartedAt = driverStartedAtMillis,
    driverAcceptedAt = driverAcceptedAtMillis,
    time = time,
    dispatchTime = dispatchTime,
    deliveryTime = deliveryTime,
    nextTime = nextTime,
    factorySummary = toFactorySummary(),
    returnFactorySummary = toReturnFactorySummary(),
    siteSummary = toSiteSummary(),
    customer = toCustomer(),
    status = toStatus(),
    signature =
      Signature(
        driverSignatureSet = driverSignatureSet,
        driverSignatureTimestampMillis = driverSignatureTimestampMillis,
        customerSignatureSet = customerSignatureSet,
        customerSignatureTimestampMillis = customerSignatureTimestampMillis,
      ),
    waterCorrectionUnloading = waterCorrectionUnloading?.toFloat(),
    superPlasticizerUnloading = superPlasticizerUnloading?.toFloat(),
    deliveryNotePdfExists = deliveryNotePdfExists,
  )

private fun Int.toDeliveryType(): DeliveryType? =
  when (this) {
    0 -> DeliveryType.DELIVERY
    1 -> DeliveryType.PICKUP
    else -> null
  }

private fun MissionEntity.toFactorySummary(): FactorySummary =
  FactorySummary(
    id = factoryId.toLong(),
    name = factorySummaryName ?: factoryName,
    latitude = parseLegacyCoordinate(factorySummaryLatitude, CoordinateKind.LATITUDE),
    longitude = parseLegacyCoordinate(factorySummaryLongitude, CoordinateKind.LONGITUDE),
    radiusInMeter = factorySummaryRadiusInMeter,
    street = factorySummaryStreet,
    houseNr = factorySummaryHouseNr,
    city = factorySummaryCity,
    zip = factorySummaryZip,
    country = factorySummaryCountry,
    phone = factorySummaryPhone,
  )

private fun MissionEntity.toReturnFactorySummary(): FactorySummary? =
  returnFactoryId?.let {
    FactorySummary(
      id = it.toLong(),
      name = returnFactorySummaryName,
      latitude = parseLegacyCoordinate(returnFactorySummaryLatitude, CoordinateKind.LATITUDE),
      longitude = parseLegacyCoordinate(returnFactorySummaryLongitude, CoordinateKind.LONGITUDE),
      radiusInMeter = returnFactorySummaryRadiusInMeter,
      street = returnFactorySummaryStreet,
      houseNr = returnFactorySummaryHouseNr,
      city = returnFactorySummaryCity,
      zip = returnFactorySummaryZip,
      country = returnFactorySummaryCountry,
      phone = returnFactorySummaryPhone,
    )
  }

private fun MissionEntity.toSiteSummary(): SiteSummary? =
  siteNr?.let {
    val joinedDescription =
      listOfNotNull(
          siteSummaryBezeichnung1,
          siteSummaryBezeichnung2,
          siteSummaryBezeichnung3,
          siteSummaryBezeichnung4,
        )
        .filter { it.isNotBlank() }
        .joinToString(" ")
        .ifBlank { null }
    SiteSummary(
      id = it,
      name = joinedDescription ?: siteDesc1,
      latitude = parseLegacyCoordinate(siteSummaryLatitude, CoordinateKind.LATITUDE),
      longitude = parseLegacyCoordinate(siteSummaryLongitude, CoordinateKind.LONGITUDE),
      radiusInMeter = siteSummaryRadiusInMeter,
      description1 = siteSummaryBezeichnung1,
      description2 = siteSummaryBezeichnung2,
      description3 = siteSummaryBezeichnung3,
      description4 = siteSummaryBezeichnung4,
      matchCode = siteSummaryMatchCode,
      street = siteSummaryStreet,
      houseNr = siteSummaryHouseNr,
      city = siteSummaryCity,
      zip = siteSummaryZip,
      country = siteSummaryCountry,
      phone = siteSummaryPhone,
      deliveryNoteInfo = siteSummaryDeliveryNoteInfo,
    )
  }

private fun MissionEntity.toCustomer(): Customer =
  Customer(
    nr = clientNr?.toString(),
    matchCode = clientMatchCode,
    name1 = clientName1,
    name2 = clientName2,
    name3 = clientName3,
    name4 = clientName4,
    street = clientStreet,
    houseNr = clientHouseNr,
    country = clientCountry,
    zip = clientZip,
    city = clientCity,
    phone = clientPhone,
    fax = clientFax,
    mobile = clientMobile,
    email1 = clientEmail1,
    email2 = clientEmail2,
    note = clientNote,
  )

private fun MissionEntity.toStatus(): Status =
  Status(
    id = statusId,
    label = null,
    color = null,
    loadingInProgressTimestamp = statusLoadingInProgressTimestamp,
    loadingCompletedTimestamp = statusLoadingCompletedTimestamp,
    enRouteToSiteTimestamp = statusEnRouteToSiteTimestamp,
    onSiteTimestamp = statusOnSiteTimestamp,
    unloadingStartedTimestamp = statusUnloadingStartedTimestamp,
    unloadingCompletedTimestamp = statusUnloadingCompletedTimestamp,
    returningTimestamp = statusReturningTimestamp,
    returnedTimestamp = statusReturnedTimestamp,
  )

internal fun UnsignedMissionSummaryEntity.toModel(): UnsignedMissionSummary =
  UnsignedMissionSummary(
    id = MissionId(id),
    driverSignatureSet = driverSignatureSet,
    customerSignatureSet = customerSignatureSet,
    customerSignatureCollective = customerSignatureCollective,
    orderNr = orderNr,
    loadNr = loadNr,
    deliveryNr = deliveryNr.toInt(),
    deliveryNrExternal = deliveryNrExternal?.toString(),
    date = date,
    articleNr = articleNr,
    recipe = recipe,
    plannedQuantity = plannedQuantity?.toString(),
    actualQuantity = actualQuantity?.toString(),
    loadQuantity = loadQuantity?.toString(),
    waterCorrectionUnloading = waterCorrectionUnloading?.toFloat(),
    status =
      Status(
        id = statusId,
        label = null,
        color = null,
        loadingInProgressTimestamp = statusLoadingInProgressTimestamp,
        loadingCompletedTimestamp = statusLoadingCompletedTimestamp,
        enRouteToSiteTimestamp = statusEnRouteToSiteTimestamp,
        onSiteTimestamp = statusOnSiteTimestamp,
        unloadingStartedTimestamp = statusUnloadingStartedTimestamp,
        unloadingCompletedTimestamp = statusUnloadingCompletedTimestamp,
        returningTimestamp = statusReturningTimestamp,
        returnedTimestamp = statusReturnedTimestamp,
      ),
  )
