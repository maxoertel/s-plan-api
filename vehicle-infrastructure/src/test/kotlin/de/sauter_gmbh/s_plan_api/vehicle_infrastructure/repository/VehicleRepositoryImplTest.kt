package de.sauter_gmbh.s_plan_api.vehicle_infrastructure.repository

import de.sauter_gmbh.s_plan_api.domain_identity.VehicleId
import de.sauter_gmbh.s_plan_api.vehicle_infrastructure.datasource.database.VehicleDataSource
import de.sauter_gmbh.s_plan_api.vehicle_infrastructure.datasource.entity.VehicleEntity
import de.sauter_gmbh.s_plan_api.vehicle_infrastructure.mapper.toModel
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class VehicleRepositoryImplTest {

  private val dataSource = mockk<VehicleDataSource>()

  private val sut by lazy { VehicleRepositoryImpl(dataSource) }

  @Test
  fun `should return mapped vehicles when dataSource returns entities`() {
    // given:
    val entities =
      listOf(
        VehicleEntity(
          id = 1L,
          factoryId = 100L,
          factoryName = "Werk Berlin",
          licensePlate = "B-AB 1234",
          latitude = "52.520008",
          longitude = "13.404954",
          trackingId = "ortung-1",
          alias = null,
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
        ),
      )
    every { dataSource.findAll() } returns entities

    // when:
    val result = sut.findAll()

    // then:
    assertEquals(1, result.size)
    assertEquals(VehicleId(1L), result[0].id)
    assertEquals("Max Mustermann", result[0].driverName)
    verify(exactly = 1) { dataSource.findAll() }
  }

  @Test
  fun `should return empty list when dataSource returns no entities`() {
    // given:
    every { dataSource.findAll() } returns emptyList()

    // when:
    val result = sut.findAll()

    // then:
    assertEquals(emptyList(), result)
    verify(exactly = 1) { dataSource.findAll() }
  }

  @Test
  fun `should return mapped vehicle when dataSource finds entity by id`() {
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
        alias = null,
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
    every { dataSource.findById(1L) } returns entity

    // when:
    val result = sut.findById(VehicleId(1L))

    // then:
    assertEquals(VehicleId(1L), result?.id)
    assertEquals("Max Mustermann", result?.driverName)
    verify(exactly = 1) { dataSource.findById(1L) }
  }

  @Test
  fun `should return null when dataSource finds no entity by id`() {
    // given:
    every { dataSource.findById(99L) } returns null

    // when:
    val result = sut.findById(VehicleId(99L))

    // then:
    assertNull(result)
    verify(exactly = 1) { dataSource.findById(99L) }
  }

  @Test
  fun `should update editable vehicle fields and return reloaded vehicle`() {
    // given:
    val entity =
      VehicleEntity(
        id = 1L,
        factoryId = 100L,
        factoryName = "Werk Berlin",
        licensePlate = "M-XY 99",
        latitude = null,
        longitude = null,
        trackingId = "ortung-1",
        alias = "Truck Alpha",
        driverFirstName = null,
        driverLastName = null,
        lastSignalTimestamp = null,
        lastStatusId = null,
        vehicleType = 9,
        fleetGroupNr = null,
        fleetGroupName = null,
        loadCapacity = 8.0,
        createdAt = null,
        updatedAt = null,
      )
    every {
      dataSource.update(
        id = 1L,
        licensePlate = "M-XY 99",
        alias = "Truck Alpha",
        vehicleType = 9,
        loadCapacity = 8.0,
        updatedBy = "admin1",
        timestampMillis = any(),
      )
    } returns 1
    every { dataSource.findById(1L) } returns entity
    val vehicle = entity.toModel()

    // when:
    val result = sut.update(vehicle, updatedBy = "admin1")

    // then:
    assertEquals(vehicle, result)
  }

  @Test
  fun `should throw NoSuchElementException when vehicle update affects no rows`() {
    // given:
    every { dataSource.update(any(), any(), any(), any(), any(), any(), any()) } returns 0
    every { dataSource.findById(99L) } returns null

    // when / then:
    assertFailsWith<NoSuchElementException> {
      sut.update(
        VehicleEntity(
            id = 99L,
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
          .toModel(),
        updatedBy = "admin1",
      )
    }
  }
}
