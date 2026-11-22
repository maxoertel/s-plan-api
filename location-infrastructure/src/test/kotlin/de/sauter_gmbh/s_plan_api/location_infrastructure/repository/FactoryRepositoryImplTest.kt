package de.sauter_gmbh.s_plan_api.location_infrastructure.repository

import de.sauter_gmbh.s_plan_api.location_infrastructure.datasource.database.FactoryDataSource
import de.sauter_gmbh.s_plan_api.location_infrastructure.datasource.entity.FactoryEntity
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlin.test.Test
import kotlin.test.assertEquals

class FactoryRepositoryImplTest {

  private val dataSource = mockk<FactoryDataSource>()

  private val sut by lazy { FactoryRepositoryImpl(dataSource) }

  @Test
  fun `should map entities to factories`() {
    // given:
    val entity =
      FactoryEntity(
        id = 100L,
        matchCode = "F1",
        name = "Werk Berlin",
        street = "Strasse",
        houseNumber = "1",
        zip = "10115",
        city = "Berlin",
        country = "DE",
        phone = "030-123",
        latitude = "52.5",
        longitude = "13.4",
        radiusInMeter = 100,
      )
    every { dataSource.findAll() } returns listOf(entity)

    // when:
    val result = sut.findAll()

    // then:
    assertEquals(1, result.size)
    assertEquals(100L, result[0].id)
    assertEquals("Werk Berlin", result[0].name)
    assertEquals(52.5, result[0].latitude)
    assertEquals(13.4, result[0].longitude)
    assertEquals(100, result[0].radiusInMeter)
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
}
