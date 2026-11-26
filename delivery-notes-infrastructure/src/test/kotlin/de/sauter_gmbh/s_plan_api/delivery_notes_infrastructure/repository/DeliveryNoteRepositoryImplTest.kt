package de.sauter_gmbh.s_plan_api.delivery_notes_infrastructure.repository

import de.sauter_gmbh.s_plan_api.delivery_notes_domain.model.Customer
import de.sauter_gmbh.s_plan_api.delivery_notes_domain.model.DeliveryNote
import de.sauter_gmbh.s_plan_api.delivery_notes_domain.model.DeliveryNoteId
import de.sauter_gmbh.s_plan_api.delivery_notes_domain.model.Signature
import de.sauter_gmbh.s_plan_api.delivery_notes_domain.model.Site
import de.sauter_gmbh.s_plan_api.delivery_notes_domain.model.Status
import de.sauter_gmbh.s_plan_api.delivery_notes_domain.model.StatusTime
import de.sauter_gmbh.s_plan_api.delivery_notes_domain.model.StatusUpdate
import de.sauter_gmbh.s_plan_api.delivery_notes_infrastructure.datasource.database.DeliveryNoteDataSource
import de.sauter_gmbh.s_plan_api.delivery_notes_infrastructure.datasource.database.DeliveryNotePdfDataSource
import de.sauter_gmbh.s_plan_api.delivery_notes_infrastructure.datasource.entity.DeliveryNoteEntity
import de.sauter_gmbh.s_plan_api.delivery_notes_infrastructure.datasource.entity.DeliveryNotePdfEntity
import de.sauter_gmbh.s_plan_api.delivery_notes_infrastructure.parser.DeliveryNoteIdParser
import de.sauter_gmbh.s_plan_api.delivery_notes_infrastructure.parser.DeliveryNotePdfIdParser
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.slot
import io.mockk.verify
import java.time.LocalDate
import java.time.LocalTime
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertNull

class DeliveryNoteRepositoryImplTest {

  private val dataSource = mockk<DeliveryNoteDataSource>()
  private val pdfDataSource = mockk<DeliveryNotePdfDataSource>()
  private val deliveryNoteIdParser = DeliveryNoteIdParser()
  private val pdfIdParser = DeliveryNotePdfIdParser()

  private val sut by lazy {
    DeliveryNoteRepositoryImpl(
      dataSource,
      pdfDataSource,
      deliveryNoteIdParser,
      pdfIdParser,
    )
  }

  private val deliveryNoteId = "100_55_20260418_083000"
  private val date = LocalDate.of(2026, 4, 18)
  private val time = LocalTime.of(8, 30, 0)

  @Test
  fun `should delegate findByDate to dataSource and return empty list when no entities`() {
    // given:
    every { dataSource.findByDate(date) } returns emptyList()

    // when:
    val result = sut.findByDate(date)

    // then:
    assertContentEquals(emptyList(), result)
    verify(exactly = 1) { dataSource.findByDate(date) }
  }

  @Test
  fun `should map entity to model when findById finds the delivery note`() {
    // given:
    every { dataSource.findById(deliveryNoteId) } returns minimalEntity()

    // when:
    val result = sut.findById(DeliveryNoteId(deliveryNoteId))

    // then:
    assertEquals(DeliveryNoteId(deliveryNoteId), result?.id)
    assertEquals("2026-04-18 09:00:00.0", result?.status?.onSiteTimestamp)
  }

  @Test
  fun `should return null when findById finds no delivery note`() {
    // given:
    every { dataSource.findById(deliveryNoteId) } returns null

    // when:
    val result = sut.findById(DeliveryNoteId(deliveryNoteId))

    // then:
    assertNull(result)
  }

  @Test
  fun `should translate status times into database columns on updateStatusTimes`() {
    // given:
    val update =
      StatusUpdate(
        times =
          mapOf(
            StatusTime.ON_SITE to LocalTime.of(9, 15),
            StatusTime.RETURNED to LocalTime.of(11, 5),
          )
      )
    val columnTimes = slot<Map<String, LocalTime>>()
    every { dataSource.updateStatusTimes(deliveryNoteId, capture(columnTimes)) } just runs

    // when:
    sut.updateStatusTimes(DeliveryNoteId(deliveryNoteId), update)

    // then:
    assertEquals(
      mapOf("fzstatusbaustelle" to LocalTime.of(9, 15), "fzstatusaufwerk" to LocalTime.of(11, 5)),
      columnTimes.captured,
    )
  }

  @Test
  fun `should return driver signature jpeg from dataSource`() {
    // given:
    val jpeg = byteArrayOf(1, 2, 3)
    every { dataSource.findDriverSignatureJpeg(deliveryNoteId) } returns jpeg

    // when:
    val result = sut.findDriverSignatureJpeg(deliveryNoteId)

    // then:
    assertContentEquals(jpeg, result)
    verify(exactly = 1) { dataSource.findDriverSignatureJpeg(deliveryNoteId) }
  }

  @Test
  fun `should return null when driver signature jpeg is missing`() {
    // given:
    every { dataSource.findDriverSignatureJpeg(deliveryNoteId) } returns null

    // when:
    val result = sut.findDriverSignatureJpeg(deliveryNoteId)

    // then:
    assertNull(result)
  }

  private fun existingPdfEntity(bytes: ByteArray) =
    DeliveryNotePdfEntity(
      factoryNr = 100L,
      date = date,
      time = time,
      deliveryNoteNr = 55,
      deliveryNoteCircle = 1,
      creationDate = date,
      creationTime = LocalTime.of(8, 31),
      storageDate = date,
      storageTime = LocalTime.of(8, 32),
      pdfBytes = bytes,
    )

  private fun deliveryNote(driverSet: Boolean, customerSet: Boolean) =
    DeliveryNote(
      id = DeliveryNoteId(deliveryNoteId),
      factoryNr = 100L,
      date = "2026-04-18",
      time = "08:30:00",
      deliveryNoteNr = 55,
      deliveryNoteNrExternal = null,
      deliveryNoteCircle = 1,
      printType = "2 Seiten",
      orderType = null,
      deliveryDateTime = null,
      orderNr = 200,
      loadNr = 1,
      batchNr = null,
      plannedQuantity = null,
      actualQuantity = null,
      loadQuantity = null,
      returnQuantity = null,
      customer =
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
        ),
      site =
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
        ),
      vehicleId = null,
      recipe = null,
      articleNr = null,
      description = null,
      status =
        Status(
          loadingInProgressTimestamp = null,
          loadingCompletedTimestamp = null,
          enRouteToSiteTimestamp = null,
          onSiteTimestamp = null,
          unloadingStartedTimestamp = null,
          unloadingCompletedTimestamp = null,
          returningTimestamp = null,
          returnedTimestamp = null,
        ),
      signature =
        Signature(
          deviceId = null,
          status = null,
          driverSignatureSet = driverSet,
          customerSignatureSet = customerSet,
        ),
      createdAt = null,
      updatedAt = null,
    )

  private fun minimalEntity() =
    DeliveryNoteEntity(
      id = deliveryNoteId,
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
      statusOnSiteTimestamp = "2026-04-18 09:00:00.0",
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
