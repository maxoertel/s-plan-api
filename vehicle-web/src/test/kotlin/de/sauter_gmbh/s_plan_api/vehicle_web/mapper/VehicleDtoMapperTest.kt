package de.sauter_gmbh.s_plan_api.vehicle_web.mapper

import de.sauter_gmbh.s_plan_api.domain_identity.VehicleId
import de.sauter_gmbh.s_plan_api.vehicle_domain.model.Vehicle
import de.sauter_gmbh.s_plan_api.vehicle_web.dto.UpdateVehicleRequestDto
import de.sauter_gmbh.s_plan_api.vehicle_web.dto.VehicleDto
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class VehicleDtoMapperTest {

  @Test
  fun `should map all fields when all values are present`() {
    // given:
    val vehicle =
      Vehicle(
        id = VehicleId(1L),
        factoryId = 100L,
        factoryName = "Werk Berlin",
        licensePlate = "B-AB 1234",
        latitude = 52.520008,
        longitude = 13.404954,
        trackingId = "ortung-1",
        alias = "Truck Alpha",
        driverName = "Max Mustermann",
        lastSignalTimestamp = 1000L,
        lastStatusId = 3,
        vehicleType = 2,
        fleetGroupNr = 5,
        fleetGroupName = "Gruppe A",
        loadCapacity = 3.5,
        createdAt = null,
        updatedAt = null,
      )

    // when:
    val result = vehicle.toDto()

    // then:
    val expected =
      VehicleDto(
        id = 1L,
        factoryId = 100L,
        factoryName = "Werk Berlin",
        licensePlate = "B-AB 1234",
        latitude = 52.520008,
        longitude = 13.404954,
        trackingId = "ortung-1",
        alias = "Truck Alpha",
        driverName = "Max Mustermann",
        lastSignalTimestamp = 1000L,
        lastStatusId = 3,
        vehicleType = 2,
        fleetGroupNr = 5,
        fleetGroupName = "Gruppe A",
        loadCapacity = 3.5,
        createdAt = null,
        updatedAt = null,
      )
    assertEquals(expected, result)
  }

  @Test
  fun `should map nullable fields as null when values are absent`() {
    // given:
    val vehicle =
      Vehicle(
        id = VehicleId(2L),
        factoryId = null,
        factoryName = null,
        licensePlate = null,
        latitude = null,
        longitude = null,
        trackingId = null,
        alias = null,
        driverName = null,
        lastSignalTimestamp = null,
        lastStatusId = null,
        vehicleType = null,
        fleetGroupNr = null,
        fleetGroupName = null,
        loadCapacity = null,
        createdAt = null,
        updatedAt = null,
      )

    // when:
    val result = vehicle.toDto()

    // then:
    val expected =
      VehicleDto(
        id = 2L,
        factoryId = null,
        factoryName = null,
        licensePlate = null,
        latitude = null,
        longitude = null,
        trackingId = null,
        alias = null,
        driverName = null,
        lastSignalTimestamp = null,
        lastStatusId = null,
        vehicleType = null,
        fleetGroupNr = null,
        fleetGroupName = null,
        loadCapacity = null,
        createdAt = null,
        updatedAt = null,
      )
    assertEquals(expected, result)
  }

  @Test
  fun `should overwrite provided fields and keep the rest when applying update patch`() {
    // given:
    val request = UpdateVehicleRequestDto(licensePlate = " M-XY 99 ", vehicleType = 9)

    // when:
    val patched = request.toPatch()(patchVehicle())

    // then:
    assertEquals("M-XY 99", patched.licensePlate)
    assertEquals(9, patched.vehicleType)
    assertEquals("Alpha", patched.alias)
    assertEquals(3.5, patched.loadCapacity)
  }

  @Test
  fun `should clear text fields when update patch provides blank values`() {
    // given:
    val request = UpdateVehicleRequestDto(licensePlate = "", alias = "  ")

    // when:
    val patched = request.toPatch()(patchVehicle())

    // then:
    assertNull(patched.licensePlate)
    assertNull(patched.alias)
  }

  @Test
  fun `should keep the stored vehicle unchanged when update patch is empty`() {
    // given:
    val vehicle = patchVehicle()

    // when:
    val patched = UpdateVehicleRequestDto().toPatch()(vehicle)

    // then:
    assertEquals(vehicle, patched)
  }

  private fun patchVehicle() =
    Vehicle(
      id = VehicleId(1L),
      factoryId = 100L,
      factoryName = "Werk",
      licensePlate = "B-AB 1234",
      latitude = null,
      longitude = null,
      trackingId = "t0",
      alias = "Alpha",
      driverName = null,
      lastSignalTimestamp = null,
      lastStatusId = null,
      vehicleType = 2,
      fleetGroupNr = null,
      fleetGroupName = null,
      loadCapacity = 3.5,
      createdAt = null,
      updatedAt = null,
    )
}
