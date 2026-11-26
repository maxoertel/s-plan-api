package de.sauter_gmbh.s_plan_api.mission_infrastructure.repository

import de.sauter_gmbh.s_plan_api.domain_identity.SiteKey
import de.sauter_gmbh.s_plan_api.domain_identity.VehicleId
import de.sauter_gmbh.s_plan_api.mission_domain.model.MissionId
import de.sauter_gmbh.s_plan_api.mission_infrastructure.datasource.database.MissionDataSource
import de.sauter_gmbh.s_plan_api.mission_infrastructure.datasource.entity.MissionEntity
import de.sauter_gmbh.s_plan_api.mission_infrastructure.datasource.entity.SiteKeyEntity
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import java.time.Clock
import java.time.Duration
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZoneOffset
import kotlin.test.Test
import kotlin.test.assertEquals

class MissionRepositoryImplTest {

  private val dataSource = mockk<MissionDataSource>()
  private val sut = MissionRepositoryImpl(dataSource)

  @Test
  fun `should return mapped missions for findByDate`() {
    // given:
    val date = LocalDate.of(2026, 4, 16)
    val entity = minimalEntity(id = "100_200_1_20260416", vehicleId = 9013L)
    every { dataSource.findByDate(date) } returns listOf(entity)

    // when:
    val result = sut.findByDate(date)

    // then:
    assertEquals(1, result.size)
    assertEquals(MissionId("100_200_1_20260416"), result[0].id)
    assertEquals(VehicleId(9013), result[0].vehicleId)
    verify(exactly = 1) { dataSource.findByDate(date) }
  }

  @Test
  fun `should return empty list for findByDate when dataSource returns no entities`() {
    // given:
    val date = LocalDate.of(2026, 4, 16)
    every { dataSource.findByDate(date) } returns emptyList()

    // when:
    val result = sut.findByDate(date)

    // then:
    assertEquals(emptyList(), result)
  }

  @Test
  fun `should map site keys returned by dataSource for findSiteKeysWithMissionsOnDate`() {
    // given: the same site nr belongs to two different customers
    val date = LocalDate.of(2026, 5, 6)
    every { dataSource.findSiteKeysOnDate(date) } returns
      setOf(
        SiteKeyEntity(customerNr = 1L, siteNr = 5L),
        SiteKeyEntity(customerNr = 2L, siteNr = 5L)
      )

    // when:
    val result = sut.findSiteKeysWithMissionsOnDate(date)

    // then:
    assertEquals(
      setOf(SiteKey(customerNr = 1L, siteNr = 5L), SiteKey(customerNr = 2L, siteNr = 5L)),
      result,
    )
    verify(exactly = 1) { dataSource.findSiteKeysOnDate(date) }
  }

  private fun minimalEntity(id: String, vehicleId: Long?) =
    MissionEntity(
      id = id,
      factoryId = 100,
      orderNr = 200,
      loadNr = 1,
      deliveryNr = 1L,
      date = LocalDate.of(2026, 4, 16),
      orderType = null,
      deliveryType = null,
      factoryName = null,
      articleNr = null,
      recipe = null,
      plannedQuantity = null,
      actualQuantity = null,
      loadQuantity = null,
      note1 = null,
      note2 = null,
      vehicleId = vehicleId,
      deliveryNrExternal = null,
      statusId = null,
      time = null,
      dispatchTime = null,
      deliveryTime = null,
      nextTime = null,
      driver = null,
      siteNr = null,
      siteDesc1 = null,
      clientNr = null,
      clientMatchCode = null,
      clientName1 = null,
      clientName2 = null,
      clientName3 = null,
      clientName4 = null,
      clientStreet = null,
      clientHouseNr = null,
      clientCountry = null,
      clientZip = null,
      clientCity = null,
      clientPhone = null,
      clientFax = null,
      clientMobile = null,
      clientEmail1 = null,
      clientEmail2 = null,
      clientNote = null,
      statusLoadingInProgressTimestamp = null,
      statusLoadingCompletedTimestamp = null,
      statusEnRouteToSiteTimestamp = null,
      statusOnSiteTimestamp = null,
      statusUnloadingStartedTimestamp = null,
      statusUnloadingCompletedTimestamp = null,
      statusReturningTimestamp = null,
      statusReturnedTimestamp = null,
    )

  private class MutableClock(private var instant: Instant) : Clock() {
    fun advance(duration: Duration) {
      instant = instant.plus(duration)
    }

    override fun instant(): Instant = instant

    override fun getZone(): ZoneId = ZoneOffset.UTC

    override fun withZone(zone: ZoneId): Clock = this
  }

  companion object {
    private const val ADDRESS = "hauptstr. 1, 10115 berlin, de"
    private const val SECOND_ADDRESS = "nebenstr. 1, 10115 berlin, de"
  }
}
