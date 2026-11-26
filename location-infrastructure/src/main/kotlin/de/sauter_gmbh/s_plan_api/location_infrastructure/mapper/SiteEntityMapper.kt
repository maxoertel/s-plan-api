package de.sauter_gmbh.s_plan_api.location_infrastructure.mapper

import de.sauter_gmbh.s_plan_api.domain_identity.SiteKey
import de.sauter_gmbh.s_plan_api.infra_db.parser.CoordinateKind
import de.sauter_gmbh.s_plan_api.infra_db.parser.parseLegacyCoordinate
import de.sauter_gmbh.s_plan_api.location_domain.model.Site
import de.sauter_gmbh.s_plan_api.location_infrastructure.datasource.entity.SiteEntity
import de.sauter_gmbh.s_plan_api.location_infrastructure.datasource.entity.SiteKeyEntity

internal fun SiteKey.toEntity(): SiteKeyEntity =
  SiteKeyEntity(customerNr = customerNr, siteNr = siteNr)

internal fun SiteEntity.toModel(): Site =
  Site(
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
    latitude = parseLegacyCoordinate(latitude, CoordinateKind.LATITUDE),
    longitude = parseLegacyCoordinate(longitude, CoordinateKind.LONGITUDE),
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
