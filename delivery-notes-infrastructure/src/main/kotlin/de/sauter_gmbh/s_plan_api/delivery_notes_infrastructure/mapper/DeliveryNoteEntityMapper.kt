package de.sauter_gmbh.s_plan_api.delivery_notes_infrastructure.mapper

import de.sauter_gmbh.s_plan_api.delivery_notes_domain.model.Customer
import de.sauter_gmbh.s_plan_api.delivery_notes_domain.model.DeliveryNote
import de.sauter_gmbh.s_plan_api.delivery_notes_domain.model.DeliveryNoteId
import de.sauter_gmbh.s_plan_api.delivery_notes_domain.model.Signature
import de.sauter_gmbh.s_plan_api.delivery_notes_domain.model.Site
import de.sauter_gmbh.s_plan_api.delivery_notes_domain.model.Status
import de.sauter_gmbh.s_plan_api.delivery_notes_infrastructure.datasource.entity.DeliveryNoteEntity
import de.sauter_gmbh.s_plan_api.domain_identity.VehicleId
import de.sauter_gmbh.s_plan_api.infra_db.parser.CoordinateKind
import de.sauter_gmbh.s_plan_api.infra_db.parser.parseLegacyCoordinate

internal fun DeliveryNoteEntity.toModel(): DeliveryNote =
  DeliveryNote(
    id = DeliveryNoteId(id),
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
    customer =
      Customer(
        nr = customerNr,
        matchCode = customerMatchCode,
        name1 = customerName1,
        name2 = customerName2,
        name3 = customerName3,
        name4 = customerName4,
        street = customerStreet,
        houseNr = customerHouseNr,
        country = customerCountry,
        zip = customerZip,
        city = customerCity,
        phone = customerPhone,
        mobile = customerMobile,
        email1 = customerEmail1,
        email2 = customerEmail2,
        note = customerNote,
        blockCode = customerBlockCode,
      ),
    site =
      Site(
        nr = siteNr,
        matchCode = siteMatchCode,
        description1 = siteDescription1,
        description2 = siteDescription2,
        description3 = siteDescription3,
        description4 = siteDescription4,
        street = siteStreet,
        houseNr = siteHouseNr,
        country = siteCountry,
        zip = siteZip,
        city = siteCity,
        contact = siteContact,
        note = siteNote,
        phone = sitePhone,
        mobile = siteMobile,
        latitude = parseLegacyCoordinate(siteLatitude, CoordinateKind.LATITUDE),
        longitude = parseLegacyCoordinate(siteLongitude, CoordinateKind.LONGITUDE),
      ),
    vehicleId = vehicleId?.let { VehicleId(it) },
    recipe = recipe,
    articleNr = articleNr,
    description = description,
    status =
      Status(
        loadingInProgressTimestamp = statusLoadingInProgressTimestamp,
        loadingCompletedTimestamp = statusLoadingCompletedTimestamp,
        enRouteToSiteTimestamp = statusEnRouteToSiteTimestamp,
        onSiteTimestamp = statusOnSiteTimestamp,
        unloadingStartedTimestamp = statusUnloadingStartedTimestamp,
        unloadingCompletedTimestamp = statusUnloadingCompletedTimestamp,
        returningTimestamp = statusReturningTimestamp,
        returnedTimestamp = statusReturnedTimestamp,
      ),
    signature =
      Signature(
        deviceId = signatureDeviceId,
        status = signatureStatus,
        driverSignatureSet = driverSignatureSet,
        driverSignaturePlaceholder = driverSignaturePlaceholder,
        driverSignatureTimestampMillis = driverSignatureTimestampMillis,
        customerSignatureSet = customerSignatureSet,
        customerSignatureTimestampMillis = customerSignatureTimestampMillis,
        customerSignaturePlaceholder = customerSignaturePlaceholder,
        customerSignatureCollective = customerSignatureCollective,
      ),
    createdAt = createdAt,
    updatedAt = updatedAt,
    waterCorrectionUnloading = waterCorrectionUnloading?.toFloat(),
    superPlasticizerUnloading = superPlasticizerUnloading?.toFloat(),
    waitingTimeMinutes = waitingTimeMinutes?.toFloat(),
    driverAcceptedAt = driverAcceptedAtMillis,
  )
