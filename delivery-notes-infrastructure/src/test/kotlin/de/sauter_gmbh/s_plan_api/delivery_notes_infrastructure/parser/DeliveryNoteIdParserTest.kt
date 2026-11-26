package de.sauter_gmbh.s_plan_api.delivery_notes_infrastructure.parser

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class DeliveryNoteIdParserTest {

  private val sut by lazy { DeliveryNoteIdParser() }

  @Test
  fun `should parse valid delivery note id`() {
    // given:
    val deliveryNoteId = "100_55_20260418_083000"

    // when:
    val result = sut.parse(deliveryNoteId)

    // then:
    assertEquals(
      ParsedDeliveryNoteId(
        factoryNr = 100L,
        deliveryNoteNr = 55,
        date = "20260418",
        time = "083000",
      ),
      result,
    )
  }

  @Test
  fun `should throw when id has too few parts`() {
    // given:
    val deliveryNoteId = "100_55_20260418"

    // when/then:
    assertFailsWith<IllegalArgumentException> { sut.parse(deliveryNoteId) }
  }

  @Test
  fun `should throw when id has too many parts`() {
    // given:
    val deliveryNoteId = "100_55_20260418_083000_extra"

    // when/then:
    assertFailsWith<IllegalArgumentException> { sut.parse(deliveryNoteId) }
  }

  @Test
  fun `should throw when date is not 8 digits`() {
    // given:
    val deliveryNoteId = "100_55_2026041_083000"

    // when/then:
    assertFailsWith<IllegalArgumentException> { sut.parse(deliveryNoteId) }
  }

  @Test
  fun `should throw when time is not 6 digits`() {
    // given:
    val deliveryNoteId = "100_55_20260418_08300"

    // when/then:
    assertFailsWith<IllegalArgumentException> { sut.parse(deliveryNoteId) }
  }

  @Test
  fun `should throw when factoryNr is not a number`() {
    // given:
    val deliveryNoteId = "abc_55_20260418_083000"

    // when/then:
    assertFailsWith<NumberFormatException> { sut.parse(deliveryNoteId) }
  }
}
