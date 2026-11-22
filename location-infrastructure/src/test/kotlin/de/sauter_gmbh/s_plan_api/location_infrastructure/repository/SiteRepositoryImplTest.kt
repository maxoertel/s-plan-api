package de.sauter_gmbh.s_plan_api.location_infrastructure.repository

import de.sauter_gmbh.s_plan_api.domain_identity.SiteKey
import de.sauter_gmbh.s_plan_api.location_domain.model.SitePosition
import de.sauter_gmbh.s_plan_api.location_infrastructure.datasource.database.SiteDataSource
import de.sauter_gmbh.s_plan_api.location_infrastructure.datasource.entity.SiteEntity
import de.sauter_gmbh.s_plan_api.location_infrastructure.datasource.entity.SiteKeyEntity
import de.sauter_gmbh.s_plan_api.location_infrastructure.datasource.entity.SitePositionEntity
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class SiteRepositoryImplTest {

  private val dataSource = mockk<SiteDataSource>()

  private val sut by lazy { SiteRepositoryImpl(dataSource) }

  private fun siteEntity(siteNr: Long, customerNr: Long = 1L) =
    SiteEntity(
      customerNr = customerNr,
      siteNr = siteNr,
      matchCode = null,
      description1 = "Bau",
      description2 = null,
      description3 = null,
      description4 = null,
      street = "Strasse",
      houseNumber = "1",
      zip = "10115",
      city = "Berlin",
      country = "DE",
      phone = null,
      mobile = null,
      fax = null,
      contact = null,
      note = null,
      internalInfo = null,
      deliveryNoteInfo = null,
      latitude = "52.5",
      longitude = "13.4",
      radiusInMeter = 50,
      factoryNr = 3L,
      externalId = null,
      isActive = true,
      isCompleted = false,
      isDeleted = false,
      releaseFrom = null,
      releaseUntil = null,
      firstDelivery = null,
      lastDelivery = null,
      createdAt = null,
      updatedAt = null,
    )

  @Test
  fun `should map all sites returned by dataSource`() {
    // given:
    every { dataSource.findAll() } returns listOf(siteEntity(5L))

    // when:
    val result = sut.findAll()

    // then:
    assertEquals(1, result.size)
    assertEquals(5L, result[0].siteNr)
    assertEquals(52.5, result[0].latitude)
    assertEquals(3L, result[0].factoryNr)
    assertEquals(false, result[0].isCompleted)
    assertEquals(false, result[0].isDeleted)
    verify(exactly = 1) { dataSource.findAll() }
  }

  @Test
  fun `should map sites returned for the given site keys`() {
    // given:
    val siteKeys =
      setOf(SiteKey(customerNr = 1L, siteNr = 5L), SiteKey(customerNr = 1L, siteNr = 7L))
    val entityKeys = listOf(SiteKeyEntity(1L, 5L), SiteKeyEntity(1L, 7L))
    every { dataSource.findBySiteKeys(entityKeys) } returns listOf(siteEntity(5L), siteEntity(7L))

    // when:
    val result = sut.findBySiteKeys(siteKeys)

    // then:
    assertEquals(listOf(5L, 7L), result.map { it.siteNr })
    verify(exactly = 1) { dataSource.findBySiteKeys(entityKeys) }
  }

  @Test
  fun `should pass the customer nr along with the site nr to the dataSource`() {
    // given: the same site nr under two different customers
    val siteKeys =
      setOf(SiteKey(customerNr = 1L, siteNr = 5L), SiteKey(customerNr = 2L, siteNr = 5L))
    val entityKeys = listOf(SiteKeyEntity(1L, 5L), SiteKeyEntity(2L, 5L))
    every { dataSource.findBySiteKeys(entityKeys) } returns
      listOf(siteEntity(siteNr = 5L, customerNr = 1L), siteEntity(siteNr = 5L, customerNr = 2L))

    // when:
    val result = sut.findBySiteKeys(siteKeys)

    // then:
    assertEquals(listOf(1L to 5L, 2L to 5L), result.map { it.customerNr to it.siteNr })
    verify(exactly = 1) { dataSource.findBySiteKeys(entityKeys) }
  }

  @Test
  fun `should return empty list when no site keys are given`() {
    // when:
    val result = sut.findBySiteKeys(emptySet())

    // then:
    assertEquals(emptyList(), result)
    verify(exactly = 0) { dataSource.findBySiteKeys(any()) }
  }

  @Test
  fun `should update position and return the reloaded site`() {
    // given:
    val key = SiteKeyEntity(customerNr = 2L, siteNr = 5L)
    val position = SitePositionEntity(latitude = "48.1", longitude = "9.2", radiusInMeter = 80)
    every { dataSource.updatePosition(key, position) } returns 1
    every { dataSource.findBySiteKeys(listOf(key)) } returns listOf(siteEntity(5L, customerNr = 2L))

    // when:
    val result =
      sut.updatePosition(
        SiteKey(customerNr = 2L, siteNr = 5L),
        SitePosition(latitude = 48.1, longitude = 9.2, radiusInMeter = 80),
      )

    // then:
    assertEquals(5L, result?.siteNr)
    assertEquals(2L, result?.customerNr)
    verify(exactly = 1) { dataSource.updatePosition(key, position) }
  }

  @Test
  fun `should return null when no site row was updated`() {
    // given:
    every { dataSource.updatePosition(any(), any()) } returns 0

    // when:
    val result =
      sut.updatePosition(
        SiteKey(customerNr = 2L, siteNr = 5L),
        SitePosition(latitude = 48.1, longitude = 9.2, radiusInMeter = 80),
      )

    // then:
    assertNull(result)
    verify(exactly = 0) { dataSource.findBySiteKeys(any()) }
  }
}
