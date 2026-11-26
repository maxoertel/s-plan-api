package de.sauter_gmbh.s_plan_api.location_domain.policy

import de.sauter_gmbh.s_plan_api.domain_identity.Actor
import de.sauter_gmbh.s_plan_api.domain_identity.Role
import de.sauter_gmbh.s_plan_api.domain_identity.UserId
import de.sauter_gmbh.s_plan_api.domain_identity.VehicleId
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class FactoryAccessPolicyTest {

  private val userId = UserId("auth0|user-1")
  private val vehicleId = VehicleId(9013L)

  @Test
  fun `should allow listing factories when actor is admin user`() {
    // given:
    val actor: Actor = Actor.User(userId, Role.ADMIN)

    // when:
    val result = actor.canListFactories()

    // then:
    assertTrue(result)
  }

  @Test
  fun `should deny listing factories when actor is viewer user`() {
    // given:
    val actor: Actor = Actor.User(userId, Role.VIEWER)

    // when:
    val result = actor.canListFactories()

    // then:
    assertFalse(result)
  }
}
