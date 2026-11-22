package de.sauter_gmbh.s_plan_api.mission_web.mapper

import de.sauter_gmbh.s_plan_api.domain_identity.VehicleId
import de.sauter_gmbh.s_plan_api.mission_domain.model.Customer
import de.sauter_gmbh.s_plan_api.mission_domain.model.FactorySummary
import de.sauter_gmbh.s_plan_api.mission_domain.model.Mission
import de.sauter_gmbh.s_plan_api.mission_domain.model.MissionId
import de.sauter_gmbh.s_plan_api.mission_domain.model.Signature
import de.sauter_gmbh.s_plan_api.mission_domain.model.SiteSummary
import de.sauter_gmbh.s_plan_api.mission_domain.model.Status
import de.sauter_gmbh.s_plan_api.mission_domain.model.UnsignedMissionSummary
import java.time.LocalDate
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class MissionDtoMapperTest {

  @Test
  fun `should map all fields correctly`() {
    // given:
    val mission =
      testMission(
        factorySummary =
          FactorySummary(
            id = 100L,
            name = "Werk",
            latitude = 52.5,
            longitude = 13.4,
            radiusInMeter = 75,
            street = "Werkstr.",
            houseNr = "10",
            city = "Berlin",
            zip = "10117",
            country = "DE",
            phone = "030-123",
          ),
        siteSummary =
          SiteSummary(
            id = 5L,
            name = "Bau 1",
            latitude = 52.6,
            longitude = 13.5,
            radiusInMeter = 30,
            description1 = "Bau 1",
            description2 = null,
            description3 = null,
            description4 = null,
            matchCode = "MC-1",
            street = "Bauweg",
            houseNr = "7",
            city = "Berlin",
            zip = "10119",
            country = "DE",
            phone = "030-789",
            deliveryNoteInfo = "Zufahrt über Tor 2",
          ),
        customer =
          blankCustomer()
            .copy(
              nr = "300",
              name1 = "Name1",
              name2 = "Name2",
              name3 = "Name3",
              name4 = "Name4",
              email1 = "a@b.de",
              email2 = "c@d.de",
            ),
        status = blankStatus().copy(id = 1, label = "loading", color = "#FF0000"),
        signature =
          Signature(
            driverSignatureSet = true,
            driverSignatureTimestampMillis = 1_700_000_000_000L,
            customerSignatureSet = false,
            customerSignatureTimestampMillis = null,
          ),
      )

    // when:
    val result = mission.toDto()

    // then:
    assertEquals("100_200_1_20260416", result.id)
    assertEquals("2026-04-16", result.date)
    assertEquals(100L, result.factorySummary.id)
    assertEquals("Werk", result.factorySummary.name)
    assertEquals(75, result.factorySummary.radiusInMeter)
    assertEquals("Werkstr.", result.factorySummary.street)
    assertEquals("10", result.factorySummary.houseNr)
    assertEquals("Berlin", result.factorySummary.city)
    assertEquals("10117", result.factorySummary.zip)
    assertEquals("DE", result.factorySummary.country)
    assertEquals("030-123", result.factorySummary.phone)

    assertNotNull(result.siteSummary)
    assertEquals(5L, result.siteSummary?.id)
    assertEquals("Bau 1", result.siteSummary?.name)
    assertEquals(30, result.siteSummary?.radiusInMeter)
    assertEquals("Bau 1", result.siteSummary?.description1)
    assertEquals("MC-1", result.siteSummary?.matchCode)
    assertEquals("Bauweg", result.siteSummary?.street)
    assertEquals("7", result.siteSummary?.houseNr)
    assertEquals("Berlin", result.siteSummary?.city)
    assertEquals("10119", result.siteSummary?.zip)
    assertEquals("DE", result.siteSummary?.country)
    assertEquals("030-789", result.siteSummary?.phone)
    assertEquals("Zufahrt über Tor 2", result.siteSummary?.deliveryNoteInfo)

    assertEquals("Name1", result.customer.name1)
    assertEquals("Name4", result.customer.name4)
    assertEquals("a@b.de", result.customer.email1)
    assertEquals("c@d.de", result.customer.email2)

    assertEquals(1, result.status.id)
    assertEquals("loading", result.status.label)
    assertEquals("#FF0000", result.status.color)

    assertEquals(true, result.signature.driverSignatureSet)
    assertEquals(1_700_000_000_000L, result.signature.driverSignatureTimestampMillis)
    assertEquals(false, result.signature.customerSignatureSet)
    assertNull(result.signature.customerSignatureTimestampMillis)
  }

  @Test
  fun `should map null siteSummary as null in dto`() {
    // given:
    val mission = testMission(siteSummary = null)

    // when:
    val result = mission.toDto()

    // then:
    assertNull(result.siteSummary)
  }

  @Test
  fun `should stringify vehicleId when present`() {
    // given:
    val mission = testMission(vehicleId = VehicleId(9013))

    // when:
    val result = mission.toDto()

    // then:
    assertEquals("9013", result.vehicleId)
  }

  @Test
  fun `should map vehicleId as null when absent`() {
    // given:
    val mission = testMission(vehicleId = null)

    // when:
    val result = mission.toDto()

    // then:
    assertNull(result.vehicleId)
  }

  @Test
  fun `should map vehicleLicensePlate when present`() {
    // given:
    val mission = testMission(vehicleLicensePlate = "B-AB 1234")

    // when:
    val result = mission.toDto()

    // then:
    assertEquals("B-AB 1234", result.vehicleLicensePlate)
  }

  @Test
  fun `should map vehicleLicensePlate as null when absent`() {
    // given:
    val mission = testMission(vehicleLicensePlate = null)

    // when:
    val result = mission.toDto()

    // then:
    assertNull(result.vehicleLicensePlate)
  }

  @Test
  fun `should map unsignedPreviousMissions to dto list`() {
    // given:
    val mission =
      testMission(
        unsignedPreviousMissions =
          listOf(
            UnsignedMissionSummary(
              id = MissionId("100_200_1_20260416"),
              driverSignatureSet = false,
              customerSignatureSet = true,
              customerSignatureCollective = false,
              orderNr = 200,
              loadNr = 1,
              deliveryNr = 11,
              deliveryNrExternal = "42",
              date = LocalDate.of(2026, 4, 16),
              articleNr = "ART-1",
              recipe = "R-1",
              plannedQuantity = "10.0",
              actualQuantity = "9.5",
              loadQuantity = "10.0",
              waterCorrectionUnloading = 1.5f,
              status =
                blankStatus()
                  .copy(
                    unloadingStartedTimestamp = "2026-04-16 08:50:00.0",
                    unloadingCompletedTimestamp = "2026-04-16 09:13:00.0",
                  ),
            )
          )
      )

    // when:
    val result = mission.toDto()

    // then:
    assertEquals(1, result.unsignedPreviousMissions.size)
    val summary = result.unsignedPreviousMissions.first()
    assertEquals("100_200_1_20260416", summary.id)
    assertEquals(false, summary.driverSignatureSet)
    assertEquals(true, summary.customerSignatureSet)
    assertEquals(false, summary.customerSignatureCollective)
    assertEquals(1, summary.loadNr)
    assertEquals(11, summary.deliveryNr)
    assertEquals("42", summary.deliveryNrExternal)
    assertEquals("2026-04-16", summary.date)
    assertEquals(1.5f, summary.waterCorrectionUnloading)
    assertEquals("2026-04-16 08:50:00.0", summary.status.unloadingStartedTimestamp)
    assertEquals("2026-04-16 09:13:00.0", summary.status.unloadingCompletedTimestamp)
  }

  @Test
  fun `should map unsignedPreviousMissions as empty list by default`() {
    // given:
    val mission = testMission()

    // when:
    val result = mission.toDto()

    // then:
    assertTrue(result.unsignedPreviousMissions.isEmpty())
  }

  @Test
  fun `should map deliveryNotePdfExists true when set on mission`() {
    // given:
    val mission = testMission(deliveryNotePdfExists = true)

    // when:
    val result = mission.toDto()

    // then:
    assertEquals(true, result.deliveryNotePdfExists)
  }

  @Test
  fun `should map driver note and driver started at`() {
    // given:
    val mission = testMission(driverNote = "Use gate 2", driverStartedAt = 1_700_000_000_000L)

    // when:
    val result = mission.toDto()

    // then:
    assertEquals("Use gate 2", result.driverNote)
    assertEquals(1_700_000_000_000L, result.driverStartedAt)
  }

  @Test
  fun `should map driver accepted at`() {
    // given:
    val mission = testMission().copy(driverAcceptedAt = 1_700_000_100_000L)

    // when:
    val result = mission.toDto()

    // then:
    assertEquals(1_700_000_100_000L, result.driverAcceptedAt)
  }

  @Test
  fun `should map water correction and super plasticizer unloading`() {
    // given:
    val mission =
      testMission().copy(waterCorrectionUnloading = 1.5f, superPlasticizerUnloading = 2.5f)

    // when:
    val result = mission.toDto()

    // then:
    assertEquals(1.5f, result.waterCorrectionUnloading)
    assertEquals(2.5f, result.superPlasticizerUnloading)
  }

  @Test
  fun `should map deliveryNotePdfExists false by default`() {
    // given:
    val mission = testMission()

    // when:
    val result = mission.toDto()

    // then:
    assertEquals(false, result.deliveryNotePdfExists)
  }

  private fun testMission(
    factorySummary: FactorySummary = blankFactorySummary(id = 100L),
    siteSummary: SiteSummary? = null,
    customer: Customer = blankCustomer(),
    status: Status = blankStatus(),
    signature: Signature = Signature(),
    vehicleId: VehicleId? = null,
    vehicleLicensePlate: String? = null,
    unsignedPreviousMissions: List<UnsignedMissionSummary> = emptyList(),
    deliveryNotePdfExists: Boolean = false,
    driverNote: String? = null,
    driverStartedAt: Long? = null,
  ) =
    Mission(
      id = MissionId("100_200_1_20260416"),
      orderNr = 200,
      loadNr = 1,
      deliveryNr = 1,
      deliveryNrExternal = null,
      date = LocalDate.of(2026, 4, 16),
      orderType = null,
      deliveryType = null,
      articleNr = null,
      recipe = null,
      plannedQuantity = null,
      actualQuantity = null,
      loadQuantity = null,
      note1 = null,
      note2 = null,
      vehicleId = vehicleId,
      vehicleLicensePlate = vehicleLicensePlate,
      driver = null,
      driverNote = driverNote,
      driverStartedAt = driverStartedAt,
      time = null,
      dispatchTime = null,
      deliveryTime = null,
      nextTime = null,
      factorySummary = factorySummary,
      siteSummary = siteSummary,
      customer = customer,
      status = status,
      signature = signature,
      unsignedPreviousMissions = unsignedPreviousMissions,
      deliveryNotePdfExists = deliveryNotePdfExists,
    )

  private fun blankFactorySummary(id: Long) =
    FactorySummary(
      id = id,
      name = null,
      latitude = null,
      longitude = null,
      radiusInMeter = null,
      street = null,
      houseNr = null,
      city = null,
      zip = null,
      country = null,
      phone = null,
    )

  private fun blankCustomer() =
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
      fax = null,
      mobile = null,
      email1 = null,
      email2 = null,
      note = null,
    )

  private fun blankStatus() =
    Status(
      id = null,
      label = null,
      color = null,
      loadingInProgressTimestamp = null,
      loadingCompletedTimestamp = null,
      enRouteToSiteTimestamp = null,
      onSiteTimestamp = null,
      unloadingStartedTimestamp = null,
      unloadingCompletedTimestamp = null,
      returningTimestamp = null,
      returnedTimestamp = null,
    )
}
