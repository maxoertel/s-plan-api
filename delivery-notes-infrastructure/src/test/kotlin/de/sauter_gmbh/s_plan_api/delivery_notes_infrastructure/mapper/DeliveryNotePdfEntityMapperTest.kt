package de.sauter_gmbh.s_plan_api.delivery_notes_infrastructure.mapper

import de.sauter_gmbh.s_plan_api.delivery_notes_domain.model.DeliveryNotePdfId
import de.sauter_gmbh.s_plan_api.delivery_notes_domain.model.DeliveryNotePdfKind
import de.sauter_gmbh.s_plan_api.delivery_notes_infrastructure.datasource.entity.DeliveryNotePdfEntity
import de.sauter_gmbh.s_plan_api.delivery_notes_infrastructure.parser.DeliveryNotePdfIdParser
import java.time.LocalDate
import java.time.LocalTime
import kotlin.test.Test
import kotlin.test.assertEquals

class DeliveryNotePdfEntityMapperTest {

  private val idParser = DeliveryNotePdfIdParser()

  @Test
  fun `should map entity to domain and format composite id`() {
    // given:
    val entity =
      DeliveryNotePdfEntity(
        factoryNr = 100L,
        date = LocalDate.of(2026, 4, 18),
        time = LocalTime.of(8, 30, 0),
        deliveryNoteNr = 55,
        deliveryNoteCircle = 1,
        creationDate = LocalDate.of(2026, 4, 18),
        creationTime = LocalTime.of(8, 31, 0),
        storageDate = LocalDate.of(2026, 4, 18),
        storageTime = LocalTime.of(8, 32, 0),
        pdfBytes = null,
      )

    // when:
    val result = entity.toModel(idParser)

    // then:
    assertEquals(
      DeliveryNotePdfId("100_20260418_083000_55_1_20260418_083100000000_20260418_083200000000"),
      result.id,
    )
    assertEquals(100L, result.factoryNr)
    assertEquals(LocalDate.of(2026, 4, 18), result.date)
    assertEquals(LocalTime.of(8, 30, 0), result.time)
    assertEquals(55, result.deliveryNoteNr)
    assertEquals(1, result.deliveryNoteCircle)
    assertEquals(LocalDate.of(2026, 4, 18), result.creationDate)
    assertEquals(LocalTime.of(8, 31, 0), result.creationTime)
    assertEquals(LocalDate.of(2026, 4, 18), result.storageDate)
    assertEquals(LocalTime.of(8, 32, 0), result.storageTime)
  }

  @Test
  fun `should ignore pdf bytes when present on entity`() {
    // given:
    val entity =
      DeliveryNotePdfEntity(
        factoryNr = 200L,
        date = LocalDate.of(2026, 4, 18),
        time = LocalTime.of(9, 15, 30),
        deliveryNoteNr = 77,
        deliveryNoteCircle = 2,
        creationDate = LocalDate.of(2026, 4, 18),
        creationTime = LocalTime.of(9, 16, 0),
        storageDate = LocalDate.of(2026, 4, 18),
        storageTime = LocalTime.of(9, 17, 0),
        pdfBytes = byteArrayOf(1, 2, 3),
      )

    // when:
    val result = entity.toModel(idParser)

    // then:
    assertEquals(200L, result.factoryNr)
    assertEquals(77, result.deliveryNoteNr)
    assertEquals(2, result.deliveryNoteCircle)
    assertEquals(LocalTime.of(9, 15, 30), result.time)
  }

  @Test
  fun `should produce id that round-trips through parser`() {
    // given:
    val entity =
      DeliveryNotePdfEntity(
        factoryNr = 100L,
        date = LocalDate.of(2026, 4, 18),
        time = LocalTime.of(8, 30, 0),
        deliveryNoteNr = 55,
        deliveryNoteCircle = 1,
        creationDate = LocalDate.of(2026, 4, 18),
        creationTime = LocalTime.of(8, 31, 0),
        storageDate = LocalDate.of(2026, 4, 18),
        storageTime = LocalTime.of(8, 32, 0),
        pdfBytes = null,
      )

    // when:
    val result = entity.toModel(idParser)
    val parsedBack = idParser.parse(result.id)

    // then:
    assertEquals(entity.factoryNr, parsedBack.factoryNr)
    assertEquals(entity.date, parsedBack.date)
    assertEquals(entity.time, parsedBack.time)
    assertEquals(entity.deliveryNoteNr, parsedBack.deliveryNoteNr)
    assertEquals(entity.deliveryNoteCircle, parsedBack.deliveryNoteCircle)
    assertEquals(entity.creationDate, parsedBack.creationDate)
    assertEquals(entity.creationTime, parsedBack.creationTime)
    assertEquals(entity.storageDate, parsedBack.storageDate)
    assertEquals(entity.storageTime, parsedBack.storageTime)
  }

  @Test
  fun `should map kind to final when data source is s-plan`() {
    // given:
    val entity = sampleEntity(dataSource = "s-track")

    // when:
    val result = entity.toModel(idParser)

    // then:
    assertEquals(DeliveryNotePdfKind.FINAL, result.kind)
  }

  @Test
  fun `should map kind to final when data source is s-plan collective`() {
    // given:
    val entity = sampleEntity(dataSource = "s-track-sammel")

    // when:
    val result = entity.toModel(idParser)

    // then:
    assertEquals(DeliveryNotePdfKind.FINAL, result.kind)
  }

  @Test
  fun `should map kind to pre print when data source is another system`() {
    // given:
    val entity = sampleEntity(dataSource = "s-plan")

    // when:
    val result = entity.toModel(idParser)

    // then:
    assertEquals(DeliveryNotePdfKind.PRE_PRINT, result.kind)
  }

  @Test
  fun `should map kind to pre print when data source is null`() {
    // given:
    val entity = sampleEntity(dataSource = null)

    // when:
    val result = entity.toModel(idParser)

    // then:
    assertEquals(DeliveryNotePdfKind.PRE_PRINT, result.kind)
  }

  private fun sampleEntity(dataSource: String?) =
    DeliveryNotePdfEntity(
      factoryNr = 100L,
      date = LocalDate.of(2026, 4, 18),
      time = LocalTime.of(8, 30, 0),
      deliveryNoteNr = 55,
      deliveryNoteCircle = 1,
      creationDate = LocalDate.of(2026, 4, 18),
      creationTime = LocalTime.of(8, 31, 0),
      storageDate = LocalDate.of(2026, 4, 18),
      storageTime = LocalTime.of(8, 32, 0),
      pdfBytes = null,
      dataSource = dataSource,
    )
}
