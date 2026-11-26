package de.sauter_gmbh.s_plan_api.delivery_notes_infrastructure.parser

import de.sauter_gmbh.s_plan_api.delivery_notes_domain.model.DeliveryNotePdfId
import java.time.LocalDate
import java.time.LocalTime
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class DeliveryNotePdfIdParserTest {

  private val sut by lazy { DeliveryNotePdfIdParser() }

  @Test
  fun `should parse valid pdf id`() {
    // given:
    val raw = "100_20260418_083000_55_1_20260418_083100_20260418_083200"

    // when:
    val result = sut.parse(DeliveryNotePdfId(raw))

    // then:
    assertEquals(
      ParsedDeliveryNotePdfId(
        factoryNr = 100L,
        date = LocalDate.of(2026, 4, 18),
        time = LocalTime.of(8, 30, 0),
        deliveryNoteNr = 55,
        deliveryNoteCircle = 1,
        creationDate = LocalDate.of(2026, 4, 18),
        creationTime = LocalTime.of(8, 31, 0),
        storageDate = LocalDate.of(2026, 4, 18),
        storageTime = LocalTime.of(8, 32, 0),
      ),
      result,
    )
  }

  @Test
  fun `should round-trip via format and parse`() {
    // given:
    val formatted =
      sut.format(
        factoryNr = 200L,
        date = LocalDate.of(2026, 4, 18),
        time = LocalTime.of(9, 15, 30),
        deliveryNoteNr = 77,
        deliveryNoteCircle = 2,
        creationDate = LocalDate.of(2026, 4, 18),
        creationTime = LocalTime.of(9, 16, 0),
        storageDate = LocalDate.of(2026, 4, 18),
        storageTime = LocalTime.of(9, 17, 0),
      )

    // when:
    val parsed = sut.parse(formatted)

    // then:
    assertEquals(200L, parsed.factoryNr)
    assertEquals(LocalTime.of(9, 15, 30), parsed.time)
    assertEquals(77, parsed.deliveryNoteNr)
    assertEquals(2, parsed.deliveryNoteCircle)
  }

  @Test
  fun `should format creation and storage time with microseconds and parse them back`() {
    // given:
    val creationTime = LocalTime.of(16, 30, 44, 962_000_000)
    val storageTime = LocalTime.of(16, 30, 44, 962_001_000)

    // when:
    val formatted =
      sut.format(
        factoryNr = 1L,
        date = LocalDate.of(2026, 9, 8),
        time = LocalTime.of(16, 30),
        deliveryNoteNr = 526623,
        deliveryNoteCircle = 1,
        creationDate = LocalDate.of(2026, 9, 8),
        creationTime = creationTime,
        storageDate = LocalDate.of(2026, 9, 8),
        storageTime = storageTime,
      )
    val parsed = sut.parse(formatted)

    // then:
    assertEquals(
      "1_20260908_163000_526623_1_20260908_163044962000_20260908_163044962001",
      formatted.value,
    )
    assertEquals(creationTime, parsed.creationTime)
    assertEquals(storageTime, parsed.storageTime)
  }

  @Test
  fun `should parse legacy id with whole-second creation and storage time`() {
    // given:
    val raw = "1_20260908_163000_526623_1_20260908_163044_20260908_163045"

    // when:
    val parsed = sut.parse(DeliveryNotePdfId(raw))

    // then:
    assertEquals(LocalTime.of(16, 30, 44), parsed.creationTime)
    assertEquals(LocalTime.of(16, 30, 45), parsed.storageTime)
  }

  @Test
  fun `should throw when creation time segment is neither 6 nor 12 digits`() {
    // given:
    val raw = "1_20260908_163000_526623_1_20260908_163044962_20260908_163044"

    // when/then:
    assertFailsWith<IllegalArgumentException> { sut.parse(DeliveryNotePdfId(raw)) }
  }

  @Test
  fun `should throw when id has wrong number of parts`() {
    // given:
    val raw = "100_20260418_083000_55_1_20260418_083100_20260418"

    // when/then:
    assertFailsWith<IllegalArgumentException> { sut.parse(DeliveryNotePdfId(raw)) }
  }

  @Test
  fun `should throw when date segment is not 8 digits`() {
    // given:
    val raw = "100_2026041_083000_55_1_20260418_083100_20260418_083200"

    // when/then:
    assertFailsWith<IllegalArgumentException> { sut.parse(DeliveryNotePdfId(raw)) }
  }

  @Test
  fun `should throw when time segment is not 6 digits`() {
    // given:
    val raw = "100_20260418_08300_55_1_20260418_083100_20260418_083200"

    // when/then:
    assertFailsWith<IllegalArgumentException> { sut.parse(DeliveryNotePdfId(raw)) }
  }

  @Test
  fun `should throw when factoryNr is not a number`() {
    // given:
    val raw = "abc_20260418_083000_55_1_20260418_083100_20260418_083200"

    // when/then:
    assertFailsWith<NumberFormatException> { sut.parse(DeliveryNotePdfId(raw)) }
  }
}
