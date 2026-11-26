package de.sauter_gmbh.s_plan_api.vehicle_web.mapper

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class TextPatchMapperTest {

  private val stored = "Tablet LKW 7"

  @Test
  fun `should keep the stored text when the patch value is null`() {
    // given:
    val patchValue: String? = null

    // when:
    val result = patchValue.patchText(stored)

    // then:
    assertEquals(stored, result)
  }

  @Test
  fun `should use the trimmed patch value when it has text`() {
    // given:
    val patchValue = "  Tablet LKW 8 "

    // when:
    val result = patchValue.patchText(stored)

    // then:
    assertEquals("Tablet LKW 8", result)
  }

  @Test
  fun `should clear the text when the patch value is blank`() {
    // given:
    val patchValue = "   "

    // when:
    val result = patchValue.patchText(stored)

    // then:
    assertNull(result)
  }
}
