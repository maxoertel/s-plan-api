package de.sauter_gmbh.s_plan_api.infra_db.parser

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class LegacyCoordinateParserTest {

  @Test
  fun `should parse latitude with 2 integer digits`() {
    // when: / then:
    assertEquals(47.696007, parseLegacyCoordinate("47696007", CoordinateKind.LATITUDE))
    assertEquals(51.34365, parseLegacyCoordinate("5134365", CoordinateKind.LATITUDE))
    assertEquals(
      48.137158309687266,
      parseLegacyCoordinate("48137158309687266", CoordinateKind.LATITUDE),
    )
  }

  @Test
  fun `should parse longitude with 1 integer digit when first digit is 5-9`() {
    // when: / then:
    assertEquals(8.3950996, parseLegacyCoordinate("83950996", CoordinateKind.LONGITUDE))
    assertEquals(
      8.273390250094257,
      parseLegacyCoordinate("8273390250094257", CoordinateKind.LONGITUDE),
    )
    assertEquals(8.3764495, parseLegacyCoordinate("83764495", CoordinateKind.LONGITUDE))
  }

  @Test
  fun `should parse longitude with 2 integer digits when first digit is 1`() {
    // when: / then:
    assertEquals(13.3691264, parseLegacyCoordinate("133691264", CoordinateKind.LONGITUDE))
    assertEquals(11.6908806, parseLegacyCoordinate("116908806", CoordinateKind.LONGITUDE))
    assertEquals(10.0048043, parseLegacyCoordinate("100048043", CoordinateKind.LONGITUDE))
    assertEquals(
      11.715987656328743,
      parseLegacyCoordinate("11715987656328743", CoordinateKind.LONGITUDE),
    )
  }

  @Test
  fun `should pass through values that already contain a decimal point`() {
    // when: / then:
    assertEquals(47.696007, parseLegacyCoordinate("47.696007", CoordinateKind.LATITUDE))
    assertEquals(8.3950996, parseLegacyCoordinate("8.3950996", CoordinateKind.LONGITUDE))
  }

  @Test
  fun `should return null for blank or empty input`() {
    // when: / then:
    assertNull(parseLegacyCoordinate(null, CoordinateKind.LATITUDE))
    assertNull(parseLegacyCoordinate("", CoordinateKind.LATITUDE))
    assertNull(parseLegacyCoordinate("   ", CoordinateKind.LATITUDE))
  }

  @Test
  fun `should return null for non-numeric input`() {
    // when: / then:
    assertNull(parseLegacyCoordinate("abc", CoordinateKind.LATITUDE))
  }

  @Test
  fun `should return null when there are not enough digits to form a decimal`() {
    // when: / then:
    assertNull(parseLegacyCoordinate("4", CoordinateKind.LATITUDE))
    assertNull(parseLegacyCoordinate("47", CoordinateKind.LATITUDE))
    assertNull(parseLegacyCoordinate("8", CoordinateKind.LONGITUDE))
  }

  @Test
  fun `should preserve negative sign`() {
    // when: / then:
    assertEquals(-47.696007, parseLegacyCoordinate("-47696007", CoordinateKind.LATITUDE))
  }
}
