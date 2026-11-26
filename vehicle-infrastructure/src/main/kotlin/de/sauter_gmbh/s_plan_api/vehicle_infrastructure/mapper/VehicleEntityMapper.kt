package de.sauter_gmbh.s_plan_api.vehicle_infrastructure.mapper

import de.sauter_gmbh.s_plan_api.domain_identity.VehicleId
import de.sauter_gmbh.s_plan_api.vehicle_domain.model.Vehicle
import de.sauter_gmbh.s_plan_api.vehicle_infrastructure.datasource.entity.VehicleEntity

internal fun VehicleEntity.toModel(): Vehicle =
  Vehicle(
    id = VehicleId(id),
    factoryId = factoryId,
    factoryName = factoryName,
    licensePlate = licensePlate,
    latitude = latitude?.toDoubleOrNull(),
    longitude = longitude?.toDoubleOrNull(),
    trackingId = trackingId,
    alias = alias,
    driverName =
      listOfNotNull(driverFirstName, driverLastName)
        .filter { it.isNotBlank() }
        .joinToString(" ")
        .ifBlank { null },
    lastSignalTimestamp = lastSignalTimestamp,
    lastStatusId = lastStatusId,
    vehicleType = vehicleType,
    fleetGroupNr = fleetGroupNr,
    fleetGroupName = fleetGroupName,
    loadCapacity = loadCapacity,
    createdAt = createdAt,
    updatedAt = updatedAt,
  )
