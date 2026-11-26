package de.sauter_gmbh.s_plan_api.delivery_notes_web.mapper

import de.sauter_gmbh.s_plan_api.delivery_notes_domain.model.Customer
import de.sauter_gmbh.s_plan_api.delivery_notes_domain.model.DeliveryNote
import de.sauter_gmbh.s_plan_api.delivery_notes_domain.model.DeliveryNoteId
import de.sauter_gmbh.s_plan_api.delivery_notes_domain.model.Signature
import de.sauter_gmbh.s_plan_api.delivery_notes_domain.model.Site
import de.sauter_gmbh.s_plan_api.delivery_notes_domain.model.Status
import de.sauter_gmbh.s_plan_api.delivery_notes_domain.model.StatusTime
import de.sauter_gmbh.s_plan_api.delivery_notes_web.dto.UpdateDeliveryNoteStatusRequestDto
import de.sauter_gmbh.s_plan_api.domain_identity.VehicleId
import java.time.LocalTime
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class DeliveryNoteDtoMapperTest {

  @Test
  fun `should map delivery note with nested customer site status and signature`() {
    // given:
    val note =
      DeliveryNote(
        id = DeliveryNoteId("100_55_20260418_083000"),
        factoryNr = 100L,
        date = "2026-04-18",
        time = "08:30:00",
        deliveryNoteNr = 55,
        deliveryNoteNrExternal = 99,
        deliveryNoteCircle = 1,
        printType = "2 Seiten",
        orderType = 2,
        deliveryDateTime = "2026-04-18T09:00:00",
        orderNr = 200,
        loadNr = 1,
        batchNr = 7,
        plannedQuantity = 10.5,
        actualQuantity = 9.5,
        loadQuantity = 10.0,
        returnQuantity = 0.5,
        customer =
          Customer(
            nr = 300L,
            matchCode = "CLI",
            name1 = "Kunde",
            name2 = "GmbH",
            name3 = null,
            name4 = null,
            street = "Str.",
            houseNr = "5",
            country = "DE",
            zip = "10115",
            city = "Berlin",
            phone = "030-1",
            mobile = "0171",
            email1 = "a@b.de",
            email2 = null,
            note = "n",
            blockCode = 0,
          ),
        site =
          Site(
            nr = 500L,
            matchCode = "BAU",
            description1 = "Baustelle",
            description2 = null,
            description3 = null,
            description4 = null,
            street = "Baustr.",
            houseNr = "7",
            country = "DE",
            zip = "10117",
            city = "Berlin",
            contact = "John",
            note = "careful",
            phone = "030-2",
            mobile = "0172",
            latitude = 47.696007,
            longitude = 9.123456,
          ),
        vehicleId = VehicleId(9013L),
        recipe = "R-1",
        articleNr = "ART-1",
        description = "Beton",
        status =
          Status(
            loadingInProgressTimestamp = "ts1",
            loadingCompletedTimestamp = "ts2",
            enRouteToSiteTimestamp = "ts3",
            onSiteTimestamp = "ts4",
            unloadingStartedTimestamp = "ts5",
            unloadingCompletedTimestamp = "ts6",
            returningTimestamp = "ts7",
            returnedTimestamp = "ts8",
          ),
        signature =
          Signature(
            deviceId = "dev-1",
            status = "signed",
            driverSignatureSet = true,
            driverSignaturePlaceholder = true,
            driverSignatureTimestampMillis = 1_700_000_000_000L,
            customerSignatureSet = true,
            customerSignatureTimestampMillis = 1_700_000_001_000L,
            customerSignaturePlaceholder = true,
            customerSignatureCollective = true,
          ),
        createdAt = "2026-04-18T08:00:00",
        updatedAt = "2026-04-18T09:00:00",
        waterCorrectionUnloading = 12.5f,
        superPlasticizerUnloading = 3.5f,
        waitingTimeMinutes = 15f,
        driverAcceptedAt = 1_700_000_002_000L,
      )

    // when:
    val result = note.toDto()

    // then:
    assertEquals("100_55_20260418_083000", result.id)
    assertEquals(100L, result.factoryNr)
    assertEquals(9013L, result.vehicleId)
    assertEquals(300L, result.customer.nr)
    assertEquals("Kunde", result.customer.name1)
    assertEquals("a@b.de", result.customer.email1)
    assertEquals(500L, result.site.nr)
    assertEquals("Baustelle", result.site.description1)
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
  fun `should map null vehicleId when absent`() {
    // given:
    val note =
      DeliveryNote(
        id = DeliveryNoteId("100_55_20260418_083000"),
        factoryNr = 100L,
        date = "2026-04-18",
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
        customer = emptyCustomer(),
        site = emptySite(),
        vehicleId = null,
        recipe = null,
        articleNr = null,
        description = null,
        status = emptyStatus(),
        signature = emptySignature(),
        createdAt = null,
        updatedAt = null,
      )

    // when:
    val result = note.toDto()

    // then:
    assertNull(result.vehicleId)
    assertNull(result.customer.nr)
    assertNull(result.site.nr)
    assertNull(result.status.loadingInProgressTimestamp)
    assertNull(result.signature.deviceId)
    assertNull(result.waterCorrectionUnloading)
    assertNull(result.superPlasticizerUnloading)
    assertNull(result.waitingTimeMinutes)
    assertNull(result.driverAcceptedAt)
  }

  private fun emptyCustomer() =
    Customer(
      nr = null,
      matchCode = null,
      name1 = null,
      name2 = null,
      name3 = null,
      name4 = null,
      street = null,
      houseNr = null,
      country = null,
      zip = null,
      city = null,
      phone = null,
      mobile = null,
      email1 = null,
      email2 = null,
      note = null,
      blockCode = null,
    )

  private fun emptySite() =
    Site(
      nr = null,
      matchCode = null,
      description1 = null,
      description2 = null,
      description3 = null,
      description4 = null,
      street = null,
      houseNr = null,
      country = null,
      zip = null,
      city = null,
      contact = null,
      note = null,
      phone = null,
      mobile = null,
    )

  private fun emptyStatus() =
    Status(
      loadingInProgressTimestamp = null,
      loadingCompletedTimestamp = null,
      enRouteToSiteTimestamp = null,
      onSiteTimestamp = null,
      unloadingStartedTimestamp = null,
      unloadingCompletedTimestamp = null,
      returningTimestamp = null,
      returnedTimestamp = null,
    )

  private fun emptySignature() =
    Signature(
      deviceId = null,
      status = null,
    )

  @Test
  fun `should map only the given times when mapping a status update request`() {
    // given:
    val request =
      UpdateDeliveryNoteStatusRequestDto(
        loadingInProgressTime = "08:35",
        onSiteTime = "09:15:30",
        returnedTime = "   ",
        returningTime = null,
      )

    // when:
    val result = request.toModel()

    // then:
    assertEquals(
      mapOf(
        StatusTime.LOADING_IN_PROGRESS to LocalTime.of(8, 35),
        StatusTime.ON_SITE to LocalTime.of(9, 15, 30),
      ),
      result.times,
    )
  }

  @Test
  fun `should map to an empty update when the status update request carries no time`() {
    // given:
    val request = UpdateDeliveryNoteStatusRequestDto()

    // when:
    val result = request.toModel()

    // then:
    assertEquals(emptyMap(), result.times)
  }

  @Test
  fun `should throw IllegalArgumentException when a status update time is unparseable`() {
    // given:
    val request = UpdateDeliveryNoteStatusRequestDto(onSiteTime = "25:99")

    // when + then:
    assertFailsWith<IllegalArgumentException> { request.toModel() }
  }
}
