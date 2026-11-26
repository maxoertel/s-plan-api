package de.sauter_gmbh.s_plan_api.vehicle_web.mapper

import de.sauter_gmbh.s_plan_api.vehicle_domain.model.Vehicle
import de.sauter_gmbh.s_plan_api.vehicle_web.dto.UpdateVehicleRequestDto
import de.sauter_gmbh.s_plan_api.vehicle_web.dto.VehicleDto

internal fun Vehicle.toDto(): VehicleDto =
  VehicleDto(
    id = id.value,
    factoryId = factoryId,
    factoryName = factoryName,
    licensePlate = licensePlate,
    latitude = latitude,
    longitude = longitude,
    trackingId = trackingId,
    alias = alias,
    driverName = driverName,
    lastSignalTimestamp = lastSignalTimestamp,
    lastStatusId = lastStatusId,
    vehicleType = vehicleType,
    fleetGroupNr = fleetGroupNr,
    fleetGroupName = fleetGroupName,
    loadCapacity = loadCapacity,
    createdAt = createdAt,
    updatedAt = updatedAt,
  )

internal fun UpdateVehicleRequestDto.toPatch(): (Vehicle) -> Vehicle = { vehicle ->
  vehicle.copy(
    licensePlate = licensePlate.patchText(vehicle.licensePlate),
    alias = alias.patchText(vehicle.alias),
    vehicleType = vehicleType ?: vehicle.vehicleType,
    loadCapacity = loadCapacity ?: vehicle.loadCapacity,
  )
}
