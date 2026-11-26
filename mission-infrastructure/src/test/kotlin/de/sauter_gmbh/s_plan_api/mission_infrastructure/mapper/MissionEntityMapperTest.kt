package de.sauter_gmbh.s_plan_api.mission_infrastructure.mapper

import de.sauter_gmbh.s_plan_api.domain_identity.VehicleId
import de.sauter_gmbh.s_plan_api.mission_domain.model.DeliveryType
import de.sauter_gmbh.s_plan_api.mission_domain.model.MissionId
import de.sauter_gmbh.s_plan_api.mission_domain.model.Signature
import de.sauter_gmbh.s_plan_api.mission_infrastructure.datasource.entity.MissionEntity
import de.sauter_gmbh.s_plan_api.mission_infrastructure.datasource.entity.UnsignedMissionSummaryEntity
import java.time.LocalDate
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class MissionEntityMapperTest {

  @Test
  fun `should map all fields correctly when all values are present`() {
    // given:
    val entity =
      MissionEntity(
        id = "100_200_1_20260416",
        factoryId = 100,
        orderNr = 200,
        loadNr = 1,
        deliveryNr = 5L,
        date = LocalDate.of(2026, 4, 16),
        orderType = 2,
        deliveryType = 0,
        factoryName = "Werk",
        articleNr = "ART-1",
        recipe = "R-1",
        plannedQuantity = 10.5,
        actualQuantity = 9.5,
        loadQuantity = 10.0,
        note1 = "n1",
        note2 = "n2",
        vehicleId = 9013L,
        vehicleLicensePlate = "B-AB 1234",
        deliveryNrExternal = 42L,
        statusId = 1,
        time = "08:00",
        dispatchTime = "07:30",
        deliveryTime = "09:00",
        nextTime = "09:30",
        driver = "Max",
        siteNr = 5L,
        siteDesc1 = "d1",
        clientNr = 300L,
        clientMatchCode = "CLI",
        clientName1 = "Name1",
        clientName2 = "Name2",
        clientName3 = "Name3",
        clientName4 = "Name4",
        clientStreet = "Kundenstr.",
        clientHouseNr = "5",
        clientCountry = "DE",
        clientZip = "10118",
        clientCity = "Berlin",
        clientPhone = "030-456",
        clientFax = "030-457",
        clientMobile = "0171-789",
        clientEmail1 = "a@b.de",
        clientEmail2 = "c@d.de",
        clientNote = "note",
        statusLoadingInProgressTimestamp = "ts1",
        statusLoadingCompletedTimestamp = "ts2",
        statusEnRouteToSiteTimestamp = "ts3",
        statusOnSiteTimestamp = "ts4",
        statusUnloadingStartedTimestamp = "ts5",
        statusUnloadingCompletedTimestamp = "ts6",
        statusReturningTimestamp = "ts7",
        statusReturnedTimestamp = "ts8",
        driverSignatureSet = true,
        driverSignatureTimestampMillis = 1_700_000_000_000L,
        customerSignatureSet = true,
        customerSignatureTimestampMillis = 1_700_000_001_000L,
        waterCorrectionUnloading = 2.5,
        superPlasticizerUnloading = 3.5,
        factorySummaryName = "Werk Berlin",
        factorySummaryLatitude = "52.5",
        factorySummaryLongitude = "13.4",
        factorySummaryRadiusInMeter = 80,
        factorySummaryStreet = "Werkstr.",
        factorySummaryHouseNr = "10",
        factorySummaryCity = "Berlin",
        factorySummaryZip = "10117",
        factorySummaryCountry = "DE",
        factorySummaryPhone = "030-123",
        siteSummaryBezeichnung1 = "Bau 1",
        siteSummaryBezeichnung2 = "Hauptbau",
        siteSummaryBezeichnung3 = null,
        siteSummaryBezeichnung4 = "Nord",
        siteSummaryMatchCode = "MC-1",
        siteSummaryLatitude = "52.6",
        siteSummaryLongitude = "13.5",
        siteSummaryRadiusInMeter = 25,
        siteSummaryStreet = "Bauweg",
        siteSummaryHouseNr = "7",
        siteSummaryCity = "Berlin",
        siteSummaryZip = "10119",
        siteSummaryCountry = "DE",
        siteSummaryPhone = "030-789",
        siteSummaryDeliveryNoteInfo = "Zufahrt über Tor 2",
      )

    // when:
    val result = entity.toModel()

    // then:
    assertEquals(MissionId("100_200_1_20260416"), result.id)
    assertEquals(5, result.deliveryNr)
    assertEquals("2", result.orderType)
    assertEquals(DeliveryType.DELIVERY, result.deliveryType)
    assertEquals("42", result.deliveryNrExternal)
    assertEquals("10.5", result.plannedQuantity)
    assertEquals("9.5", result.actualQuantity)
    assertEquals("10.0", result.loadQuantity)
    assertEquals(VehicleId(9013), result.vehicleId)
    assertEquals("B-AB 1234", result.vehicleLicensePlate)

    assertEquals(100L, result.factorySummary.id)
    assertEquals("Werk Berlin", result.factorySummary.name)
    assertEquals(52.5, result.factorySummary.latitude)
    assertEquals(13.4, result.factorySummary.longitude)
    assertEquals(80, result.factorySummary.radiusInMeter)
    assertEquals("Werkstr.", result.factorySummary.street)
    assertEquals("10", result.factorySummary.houseNr)
    assertEquals("Berlin", result.factorySummary.city)
    assertEquals("10117", result.factorySummary.zip)
    assertEquals("DE", result.factorySummary.country)
    assertEquals("030-123", result.factorySummary.phone)

    assertNotNull(result.siteSummary)
    assertEquals(5L, result.siteSummary?.id)
    assertEquals("Bau 1 Hauptbau Nord", result.siteSummary?.name)
    assertEquals(52.6, result.siteSummary?.latitude)
    assertEquals(13.5, result.siteSummary?.longitude)
    assertEquals(25, result.siteSummary?.radiusInMeter)
    assertEquals("Bau 1", result.siteSummary?.description1)
    assertEquals("Hauptbau", result.siteSummary?.description2)
    assertEquals("Nord", result.siteSummary?.description4)
    assertEquals("MC-1", result.siteSummary?.matchCode)
    assertEquals("Bauweg", result.siteSummary?.street)
    assertEquals("7", result.siteSummary?.houseNr)
    assertEquals("Berlin", result.siteSummary?.city)
    assertEquals("10119", result.siteSummary?.zip)
    assertEquals("DE", result.siteSummary?.country)
    assertEquals("030-789", result.siteSummary?.phone)
    assertEquals("Zufahrt über Tor 2", result.siteSummary?.deliveryNoteInfo)

    assertEquals("300", result.customer.nr)
    assertEquals("Name1", result.customer.name1)
    assertEquals("Name4", result.customer.name4)
    assertEquals("a@b.de", result.customer.email1)
    assertEquals("c@d.de", result.customer.email2)

    assertEquals(1, result.status.id)
    assertNull(result.status.label)
    assertNull(result.status.color)
    assertEquals("ts1", result.status.loadingInProgressTimestamp)
    assertEquals("ts8", result.status.returnedTimestamp)

    assertEquals(
      Signature(
        driverSignatureSet = true,
        driverSignatureTimestampMillis = 1_700_000_000_000L,
        customerSignatureSet = true,
        customerSignatureTimestampMillis = 1_700_000_001_000L,
      ),
      result.signature,
    )

    assertEquals(2.5f, result.waterCorrectionUnloading)
    assertEquals(3.5f, result.superPlasticizerUnloading)
  }

  @Test
  fun `should fall back to factoryName and siteDesc1 when summaries are not joined`() {
    // given:
    val entity =
      entityWithoutSignature()
        .copy(factoryName = "Werk Fallback", siteNr = 9L, siteDesc1 = "Bau Fallback")

    // when:
    val result = entity.toModel()

    // then:
    assertEquals("Werk Fallback", result.factorySummary.name)
    assertNotNull(result.siteSummary)
    assertEquals(9L, result.siteSummary?.id)
    assertEquals("Bau Fallback", result.siteSummary?.name)
  }

  @Test
  fun `should map siteSummary as null when siteNr is null`() {
    // given:
    val entity = entityWithoutSignature()

    // when:
    val result = entity.toModel()

    // then:
    assertNull(result.siteSummary)
  }

  @Test
  fun `should map nullable fields as null when values are absent`() {
    // given:
    val entity = entityWithoutSignature()

    // when:
    val result = entity.toModel()

    // then:
    assertNull(result.orderType)
    assertNull(result.deliveryType)
    assertNull(result.deliveryNrExternal)
    assertNull(result.vehicleId)
    assertNull(result.vehicleLicensePlate)
    assertEquals(100L, result.factorySummary.id)
    assertNull(result.factorySummary.name)
    assertNull(result.customer.nr)
    assertNull(result.status.id)
  }

  @Test
  fun `should map deliveryType DELIVERY when lieferart is 0`() {
    // given:
    val entity = entityWithoutSignature().copy(deliveryType = 0)

    // when:
    val result = entity.toModel()

    // then:
    assertEquals(DeliveryType.DELIVERY, result.deliveryType)
  }

  @Test
  fun `should map deliveryType PICKUP when lieferart is 1`() {
    // given:
    val entity = entityWithoutSignature().copy(deliveryType = 1)

    // when:
    val result = entity.toModel()

    // then:
    assertEquals(DeliveryType.PICKUP, result.deliveryType)
  }

  @Test
  fun `should map deliveryType to null when lieferart is unknown`() {
    // given:
    val entity = entityWithoutSignature().copy(deliveryType = 99)

    // when:
    val result = entity.toModel()

    // then:
    assertNull(result.deliveryType)
  }

  @Test
  fun `should default signature to unsigned when signature fields are absent`() {
    // given:
    val entity = entityWithoutSignature()

    // when:
    val result = entity.toModel()

    // then:
    assertFalse(result.signature.driverSignatureSet)
    assertNull(result.signature.driverSignatureTimestampMillis)
    assertFalse(result.signature.customerSignatureSet)
    assertNull(result.signature.customerSignatureTimestampMillis)
  }

  @Test
  fun `should map driver signature when only driver has signed`() {
    // given:
    val entity =
      entityWithoutSignature()
        .copy(driverSignatureSet = true, driverSignatureTimestampMillis = 1_700_000_000_000L)

    // when:
    val result = entity.toModel()

    // then:
    assertTrue(result.signature.driverSignatureSet)
    assertEquals(1_700_000_000_000L, result.signature.driverSignatureTimestampMillis)
    assertFalse(result.signature.customerSignatureSet)
    assertNull(result.signature.customerSignatureTimestampMillis)
  }

  @Test
  fun `should map driver note and driver started at`() {
    // given:
    val entity =
      entityWithoutSignature()
        .copy(driverNote = "Use gate 2", driverStartedAtMillis = 1_700_000_000_000L)

    // when:
    val result = entity.toModel()

    // then:
    assertEquals("Use gate 2", result.driverNote)
    assertEquals(1_700_000_000_000L, result.driverStartedAt)
  }

  @Test
  fun `should map driver accepted at`() {
    // given:
    val entity = entityWithoutSignature().copy(driverAcceptedAtMillis = 1_700_000_100_000L)

    // when:
    val result = entity.toModel()

    // then:
    assertEquals(1_700_000_100_000L, result.driverAcceptedAt)
  }

  @Test
  fun `should map deliveryNotePdfExists true when set on entity`() {
    // given:
    val entity = entityWithoutSignature().copy(deliveryNotePdfExists = true)

    // when:
    val result = entity.toModel()

    // then:
    assertTrue(result.deliveryNotePdfExists)
  }

  @Test
  fun `should default deliveryNotePdfExists to false when not set on entity`() {
    // given:
    val entity = entityWithoutSignature()

    // when:
    val result = entity.toModel()

    // then:
    assertFalse(result.deliveryNotePdfExists)
  }

  @Test
  fun `should map UnsignedMissionSummaryEntity to domain model with all fields`() {
    // given:
    val entity =
      UnsignedMissionSummaryEntity(
        id = "100_200_2_20260416",
        orderNr = 200,
        loadNr = 2,
        deliveryNr = 11L,
        deliveryNrExternal = 42L,
        date = LocalDate.of(2026, 4, 16),
        articleNr = "ART-1",
        recipe = "R-1",
        plannedQuantity = 10.5,
        actualQuantity = 9.5,
        loadQuantity = 10.0,
        driverSignatureSet = false,
        customerSignatureSet = true,
        customerSignatureCollective = true,
        waterCorrectionUnloading = 1.5,
        statusId = 7,
        statusLoadingInProgressTimestamp = "2026-04-16 08:00:00.0",
        statusLoadingCompletedTimestamp = "2026-04-16 08:10:00.0",
        statusEnRouteToSiteTimestamp = "2026-04-16 08:15:00.0",
        statusOnSiteTimestamp = "2026-04-16 08:45:00.0",
        statusUnloadingStartedTimestamp = "2026-04-16 08:50:00.0",
        statusUnloadingCompletedTimestamp = "2026-04-16 09:13:00.0",
        statusReturningTimestamp = "2026-04-16 09:15:00.0",
        statusReturnedTimestamp = "2026-04-16 09:45:00.0",
      )

    // when:
    val result = entity.toModel()

    // then:
    assertEquals(MissionId("100_200_2_20260416"), result.id)
    assertEquals(200, result.orderNr)
    assertEquals(2, result.loadNr)
    assertEquals(11, result.deliveryNr)
    assertEquals("42", result.deliveryNrExternal)
    assertEquals(LocalDate.of(2026, 4, 16), result.date)
    assertEquals("ART-1", result.articleNr)
    assertEquals("R-1", result.recipe)
    assertEquals("10.5", result.plannedQuantity)
    assertEquals("9.5", result.actualQuantity)
    assertEquals("10.0", result.loadQuantity)
    assertFalse(result.driverSignatureSet)
    assertTrue(result.customerSignatureSet)
    assertTrue(result.customerSignatureCollective)
    assertEquals(1.5f, result.waterCorrectionUnloading)
    assertEquals(7, result.status.id)
    assertEquals("2026-04-16 08:15:00.0", result.status.enRouteToSiteTimestamp)
    assertEquals("2026-04-16 08:45:00.0", result.status.onSiteTimestamp)
    assertEquals("2026-04-16 08:50:00.0", result.status.unloadingStartedTimestamp)
    assertEquals("2026-04-16 09:13:00.0", result.status.unloadingCompletedTimestamp)
    assertEquals("2026-04-16 09:15:00.0", result.status.returningTimestamp)
    assertEquals("2026-04-16 09:45:00.0", result.status.returnedTimestamp)
  }

  @Test
  fun `should map UnsignedMissionSummaryEntity nullable fields as null`() {
    // given:
    val entity =
      UnsignedMissionSummaryEntity(
        id = "100_200_2_20260416",
        orderNr = 200,
        loadNr = 2,
        deliveryNr = 11L,
        deliveryNrExternal = null,
        date = LocalDate.of(2026, 4, 16),
        articleNr = null,
        recipe = null,
        plannedQuantity = null,
        actualQuantity = null,
        loadQuantity = null,
        driverSignatureSet = false,
        customerSignatureSet = false,
        customerSignatureCollective = false,
        waterCorrectionUnloading = null,
        statusId = null,
        statusLoadingInProgressTimestamp = null,
        statusLoadingCompletedTimestamp = null,
        statusEnRouteToSiteTimestamp = null,
        statusOnSiteTimestamp = null,
        statusUnloadingStartedTimestamp = null,
        statusUnloadingCompletedTimestamp = null,
        statusReturningTimestamp = null,
        statusReturnedTimestamp = null,
      )

    // when:
    val result = entity.toModel()

    // then:
    assertNull(result.deliveryNrExternal)
    assertNull(result.articleNr)
    assertNull(result.recipe)
    assertNull(result.plannedQuantity)
    assertNull(result.actualQuantity)
    assertNull(result.loadQuantity)
    assertNull(result.waterCorrectionUnloading)
    assertNull(result.status.id)
    assertNull(result.status.unloadingStartedTimestamp)
    assertNull(result.status.unloadingCompletedTimestamp)
  }

  private fun entityWithoutSignature() =
    MissionEntity(
      id = "100_200_1_20260416",
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
      vehicleId = null,
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
}
