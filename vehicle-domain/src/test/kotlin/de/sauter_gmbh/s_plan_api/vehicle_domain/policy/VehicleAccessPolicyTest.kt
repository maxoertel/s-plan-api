package de.sauter_gmbh.s_plan_api.vehicle_domain.policy

import de.sauter_gmbh.s_plan_api.domain_identity.Actor
import de.sauter_gmbh.s_plan_api.domain_identity.Role
import de.sauter_gmbh.s_plan_api.domain_identity.UserId
import de.sauter_gmbh.s_plan_api.domain_identity.VehicleId
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class VehicleAccessPolicyTest {

  private val userId = UserId("auth0|user-1")
  private val vehicleId = VehicleId(9013L)
  private val otherVehicleId = VehicleId(9999L)

  @Test
  fun `should allow listing all vehicles when actor is admin user`() {
    // given:
    val actor: Actor = Actor.User(userId, Role.ADMIN)

    // when:
    val result = actor.canListAllVehicles()

    // then:
    assertTrue(result)
  }

  @Test
  fun `should deny listing all vehicles when actor is viewer user`() {
    // given:
    val actor: Actor = Actor.User(userId, Role.VIEWER)

    // when:
    val result = actor.canListAllVehicles()

    // then:
    assertFalse(result)
  }

  @Test
  fun `should allow updating vehicle when actor is admin user`() {
    // given:
    val actor: Actor = Actor.User(userId, Role.ADMIN)

    // when:
    val result = actor.canUpdateVehicle()

    // then:
    assertTrue(result)
  }

  @Test
  fun `should deny updating vehicle when actor is viewer user`() {
    // given:
    val actor: Actor = Actor.User(userId, Role.VIEWER)

    // when:
    val result = actor.canUpdateVehicle()

    // then:
    assertFalse(result)
  }
}
