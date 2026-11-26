package de.sauter_gmbh.s_plan_api.vehicle_infrastructure.mapper

import de.sauter_gmbh.s_plan_api.domain_identity.VehicleId
import de.sauter_gmbh.s_plan_api.vehicle_domain.model.Vehicle
import de.sauter_gmbh.s_plan_api.vehicle_infrastructure.datasource.entity.VehicleEntity
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class VehicleEntityMapperTest {

  @Test
  fun `should map all fields when all values are present`() {
    // given:
    val entity =
      VehicleEntity(
        id = 1L,
        factoryId = 100L,
        factoryName = "Werk Berlin",
        licensePlate = "B-AB 1234",
        latitude = "52.520008",
        longitude = "13.404954",
        trackingId = "ortung-1",
        alias = "Truck Alpha",
        driverFirstName = "Max",
        driverLastName = "Mustermann",
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
    val result = entity.toModel()

    // then:
    val expected =
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
    assertEquals(expected, result)
  }

  @Test
  fun `should map nullable fields as null when values are absent`() {
    // given:
    val entity =
      VehicleEntity(
        id = 2L,
        factoryId = null,
        factoryName = null,
        licensePlate = null,
        latitude = null,
        longitude = null,
        trackingId = null,
        alias = null,
        driverFirstName = null,
        driverLastName = null,
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
    val result = entity.toModel()

    // then:
    assertNull(result.latitude)
    assertNull(result.longitude)
    assertNull(result.driverName)
  }

  @Test
  fun `should return null latitude when latitude is not a valid number`() {
    // given:
    val entity =
      VehicleEntity(
        id = 3L,
        factoryId = null,
        factoryName = null,
        licensePlate = null,
        latitude = "not-a-number",
        longitude = "13.404954",
        trackingId = null,
        alias = null,
        driverFirstName = null,
        driverLastName = null,
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
    val result = entity.toModel()

    // then:
    assertNull(result.latitude)
    assertEquals(13.404954, result.longitude)
  }

  @Test
  fun `should combine first and last name into driverName`() {
    // given:
    val entity =
      VehicleEntity(
        id = 4L,
        factoryId = null,
        factoryName = null,
        licensePlate = null,
        latitude = null,
        longitude = null,
        trackingId = null,
        alias = null,
        driverFirstName = "Max",
        driverLastName = "Mustermann",
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
    val result = entity.toModel()

    // then:
    assertEquals("Max Mustermann", result.driverName)
  }

  @Test
  fun `should use only last name when first name is null`() {
    // given:
    val entity =
      VehicleEntity(
        id = 5L,
        factoryId = null,
        factoryName = null,
        licensePlate = null,
        latitude = null,
        longitude = null,
        trackingId = null,
        alias = null,
        driverFirstName = null,
        driverLastName = "Mustermann",
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
    val result = entity.toModel()

    // then:
    assertEquals("Mustermann", result.driverName)
  }

  @Test
  fun `should use only first name when last name is null`() {
    // given:
    val entity =
      VehicleEntity(
        id = 6L,
        factoryId = null,
        factoryName = null,
        licensePlate = null,
        latitude = null,
        longitude = null,
        trackingId = null,
        alias = null,
        driverFirstName = "Max",
        driverLastName = null,
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
    val result = entity.toModel()

    // then:
    assertEquals("Max", result.driverName)
  }

  @Test
  fun `should return null driverName when both names are blank`() {
    // given:
    val entity =
      VehicleEntity(
        id = 7L,
        factoryId = null,
        factoryName = null,
        licensePlate = null,
        latitude = null,
        longitude = null,
        trackingId = null,
        alias = null,
        driverFirstName = "  ",
        driverLastName = "  ",
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
    val result = entity.toModel()

    // then:
    assertNull(result.driverName)
  }
}
