package de.sauter_gmbh.s_plan_api.delivery_notes_domain.model

import java.time.LocalDate
import java.time.LocalTime
import kotlin.test.Test
import kotlin.test.assertEquals

class DeliveryNoteIdTest {

  @Test
  fun `should join factory, delivery note number, date and time when building from parts`() {
    // when:
    val result =
      DeliveryNoteId.from(
        factoryNr = 100L,
        deliveryNoteNr = 55,
        date = LocalDate.of(2026, 4, 18),
        time = LocalTime.of(8, 30, 0),
      )

    // then:
    assertEquals(DeliveryNoteId("100_55_20260418_083000"), result)
  }

  @Test
  fun `should pad single digit date and time parts with zeros when building from parts`() {
    // when:
    val result =
      DeliveryNoteId.from(
        factoryNr = 7L,
        deliveryNoteNr = 3,
        date = LocalDate.of(2026, 1, 5),
        time = LocalTime.of(6, 4, 9),
      )

    // then:
    assertEquals(DeliveryNoteId("7_3_20260105_060409"), result)
  }
}
