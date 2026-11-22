package de.sauter_gmbh.s_plan_api.delivery_notes_infrastructure.mapper

import de.sauter_gmbh.s_plan_api.delivery_notes_domain.model.DeliveryNoteId
import de.sauter_gmbh.s_plan_api.delivery_notes_infrastructure.datasource.entity.DeliveryNoteEntity
import de.sauter_gmbh.s_plan_api.domain_identity.VehicleId
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class DeliveryNoteEntityMapperTest {

  @Test
  fun `should map all fields correctly when all values are present`() {
    // given:
    val entity = fullEntity()

    // when:
    val result = entity.toModel()

    // then:
    assertEquals(DeliveryNoteId("100_55_20260401_083000"), result.id)
    assertEquals(100L, result.factoryNr)
    assertEquals("2026-04-01", result.date)
    assertEquals("08:30:00", result.time)
    assertEquals(55, result.deliveryNoteNr)
    assertEquals(VehicleId(9013L), result.vehicleId)
    assertEquals(300L, result.customer.nr)
    assertEquals("Kunde", result.customer.name1)
    assertEquals("a@b.de", result.customer.email1)
    assertEquals(500L, result.site.nr)
    assertEquals("Baustelle", result.site.description1)
    assertEquals("DE", result.site.country)
    assertEquals(47.696007, result.site.latitude)
    assertEquals(9.123456, result.site.longitude)
    assertEquals("ts1", result.status.loadingInProgressTimestamp)
    assertEquals("ts8", result.status.returnedTimestamp)
    assertEquals("dev-1", result.signature.deviceId)
    assertEquals("signed", result.signature.status)
    assertEquals(true, result.signature.driverSignatureSet)
    assertEquals(true, result.signature.driverSignaturePlaceholder)
    assertEquals(1_700_000_000_000L, result.signature.driverSignatureTimestampMillis)
    assertEquals(true, result.signature.customerSignatureSet)
    assertEquals(1_700_000_001_000L, result.signature.customerSignatureTimestampMillis)
    assertEquals(true, result.signature.customerSignaturePlaceholder)
    assertEquals(true, result.signature.customerSignatureCollective)
    assertEquals(12.5f, result.waterCorrectionUnloading)
    assertEquals(3.5f, result.superPlasticizerUnloading)
    assertEquals(15f, result.waitingTimeMinutes)
    assertEquals(1_700_000_002_000L, result.driverAcceptedAt)
  }

  @Test
  fun `should map nested customer and site as null-filled when values absent`() {
    // given:
    val entity = minimalEntity()

    // when:
    val result = entity.toModel()

    // then:
    assertNull(result.vehicleId)
    assertNull(result.customer.nr)
    assertNull(result.customer.name1)
    assertNull(result.site.nr)
    assertNull(result.site.description1)
    assertNull(result.site.latitude)
    assertNull(result.site.longitude)
    assertNull(result.status.loadingInProgressTimestamp)
    assertNull(result.status.returnedTimestamp)
    assertNull(result.signature.deviceId)
    assertNull(result.signature.status)
    assertNull(result.waterCorrectionUnloading)
    assertNull(result.superPlasticizerUnloading)
    assertNull(result.waitingTimeMinutes)
    assertNull(result.driverAcceptedAt)
  }

  private fun fullEntity() =
    DeliveryNoteEntity(
      id = "100_55_20260401_083000",
      factoryNr = 100L,
      date = "2026-04-01",
      time = "08:30:00",
      deliveryNoteNr = 55,
      deliveryNoteNrExternal = 99,
      deliveryNoteCircle = 1,
      printType = "2 Seiten",
      orderType = 2,
      deliveryDateTime = "2026-04-01T09:00:00",
      orderNr = 200,
      loadNr = 1,
      batchNr = 7,
      plannedQuantity = 10.5,
      actualQuantity = 9.5,
      loadQuantity = 10.0,
      returnQuantity = 0.5,
      customerNr = 300L,
      customerMatchCode = "CLI",
      customerName1 = "Kunde",
      customerName2 = "GmbH",
      customerName3 = null,
      customerName4 = null,
      customerStreet = "Kundenstr.",
      customerHouseNr = "5",
      customerCountry = "DE",
      customerZip = "10115",
      customerCity = "Berlin",
      customerPhone = "030-456",
      customerMobile = "0171",
      customerEmail1 = "a@b.de",
      customerEmail2 = null,
      customerNote = "note",
      customerBlockCode = 0,
      siteNr = 500L,
      siteMatchCode = "BAU",
      siteDescription1 = "Baustelle",
      siteDescription2 = null,
      siteDescription3 = null,
      siteDescription4 = null,
      siteStreet = "Baustr.",
      siteHouseNr = "7",
      siteCountry = "DE",
      siteZip = "10117",
      siteCity = "Berlin",
      siteContact = "John",
      siteNote = "careful",
      sitePhone = "030-789",
      siteMobile = "0172",
      siteLatitude = "47696007",
      siteLongitude = "9.123456",
      vehicleId = 9013L,
      recipe = "R-1",
      articleNr = "ART-1",
      description = "Beton",
      statusLoadingInProgressTimestamp = "ts1",
      statusLoadingCompletedTimestamp = "ts2",
      statusEnRouteToSiteTimestamp = "ts3",
      statusOnSiteTimestamp = "ts4",
      statusUnloadingStartedTimestamp = "ts5",
      statusUnloadingCompletedTimestamp = "ts6",
      statusReturningTimestamp = "ts7",
      statusReturnedTimestamp = "ts8",
      signatureDeviceId = "dev-1",
      signatureStatus = "signed",
      driverSignatureSet = true,
      driverSignaturePlaceholder = true,
      driverSignatureTimestampMillis = 1_700_000_000_000L,
      customerSignatureSet = true,
      customerSignatureTimestampMillis = 1_700_000_001_000L,
      customerSignaturePlaceholder = true,
      customerSignatureCollective = true,
      createdAt = "2026-04-01T08:00:00",
      updatedAt = "2026-04-01T09:00:00",
      waterCorrectionUnloading = 12.5,
      superPlasticizerUnloading = 3.5,
      waitingTimeMinutes = 15.0,
      driverAcceptedAtMillis = 1_700_000_002_000L,
    )

  private fun minimalEntity() =
    DeliveryNoteEntity(
      id = "100_55_20260401_083000",
      factoryNr = 100L,
      date = "2026-04-01",
      time = "08:30:00",
      deliveryNoteNr = 55,
      deliveryNoteNrExternal = null,
      deliveryNoteCircle = null,
      printType = null,
      orderType = null,
      deliveryDateTime = null,
      orderNr = null,
      loadNr = null,
      batchNr = null,
      plannedQuantity = null,
      actualQuantity = null,
      loadQuantity = null,
      returnQuantity = null,
      customerNr = null,
      customerMatchCode = null,
      customerName1 = null,
      customerName2 = null,
      customerName3 = null,
      customerName4 = null,
      customerStreet = null,
      customerHouseNr = null,
      customerCountry = null,
      customerZip = null,
      customerCity = null,
      customerPhone = null,
      customerMobile = null,
      customerEmail1 = null,
      customerEmail2 = null,
      customerNote = null,
      customerBlockCode = null,
      siteNr = null,
      siteMatchCode = null,
      siteDescription1 = null,
      siteDescription2 = null,
      siteDescription3 = null,
      siteDescription4 = null,
      siteStreet = null,
      siteHouseNr = null,
      siteCountry = null,
      siteZip = null,
      siteCity = null,
      siteContact = null,
      siteNote = null,
      sitePhone = null,
      siteMobile = null,
      vehicleId = null,
      recipe = null,
      articleNr = null,
      description = null,
      statusLoadingInProgressTimestamp = null,
      statusLoadingCompletedTimestamp = null,
      statusEnRouteToSiteTimestamp = null,
      statusOnSiteTimestamp = null,
      statusUnloadingStartedTimestamp = null,
      statusUnloadingCompletedTimestamp = null,
      statusReturningTimestamp = null,
      statusReturnedTimestamp = null,
      signatureDeviceId = null,
      signatureStatus = null,
      createdAt = null,
      updatedAt = null,
    )
}
