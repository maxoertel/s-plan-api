package de.sauter_gmbh.s_plan_api.mission_domain.model

enum class DeliveryType {
  /**
   * Material is supplied (delivered) to the customer site. Maps to legacy `lieferart` value 0
   * (Zufuhr).
   */
  DELIVERY,

  /**
   * Material is picked up from the customer site. Maps to legacy `lieferart` value 1 (Abholung).
   */
  PICKUP,
}
