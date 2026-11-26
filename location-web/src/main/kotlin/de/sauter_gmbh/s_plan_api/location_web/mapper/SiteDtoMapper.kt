package de.sauter_gmbh.s_plan_api.location_web.mapper

import de.sauter_gmbh.s_plan_api.location_domain.model.Site
import de.sauter_gmbh.s_plan_api.location_web.dto.SiteDto

internal fun Site.toDto(): SiteDto =
  SiteDto(
    customerNr = customerNr,
    siteNr = siteNr,
    matchCode = matchCode,
    description1 = description1,
    description2 = description2,
    description3 = description3,
    description4 = description4,
    street = street,
    houseNumber = houseNumber,
    zip = zip,
    city = city,
    country = country,
    phone = phone,
    mobile = mobile,
    fax = fax,
    contact = contact,
    note = note,
    internalInfo = internalInfo,
    deliveryNoteInfo = deliveryNoteInfo,
    latitude = latitude,
    longitude = longitude,
    radiusInMeter = radiusInMeter,
    factoryNr = factoryNr,
    externalId = externalId,
    isActive = isActive,
    isCompleted = isCompleted,
    isDeleted = isDeleted,
    releaseFrom = releaseFrom,
    releaseUntil = releaseUntil,
    firstDelivery = firstDelivery,
    lastDelivery = lastDelivery,
    createdAt = createdAt,
    updatedAt = updatedAt,
  )
