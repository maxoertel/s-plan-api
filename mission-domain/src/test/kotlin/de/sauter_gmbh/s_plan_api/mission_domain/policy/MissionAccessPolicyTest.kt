package de.sauter_gmbh.s_plan_api.mission_domain.policy

import de.sauter_gmbh.s_plan_api.domain_identity.Actor
import de.sauter_gmbh.s_plan_api.domain_identity.Role
import de.sauter_gmbh.s_plan_api.domain_identity.UserId
import de.sauter_gmbh.s_plan_api.domain_identity.VehicleId
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class MissionAccessPolicyTest {

  private val vehicleId = VehicleId(9013L)
  private val otherVehicleId = VehicleId(9999L)
  private val userId = UserId("auth0|user-1")

  @Test
  fun `should allow listing all missions when actor is admin user`() {
    val actor: Actor = Actor.User(userId, Role.ADMIN)
    assertTrue(actor.canListAllMissions())
  }

  @Test
  fun `should deny listing all missions when actor is viewer user`() {
    val actor: Actor = Actor.User(userId, Role.VIEWER)
    assertFalse(actor.canListAllMissions())
  }
}
