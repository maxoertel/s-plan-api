package de.sauter_gmbh.s_plan_api.user_management_domain.policy

import de.sauter_gmbh.s_plan_api.domain_identity.Actor
import de.sauter_gmbh.s_plan_api.domain_identity.Role
import de.sauter_gmbh.s_plan_api.domain_identity.UserId
import de.sauter_gmbh.s_plan_api.domain_identity.VehicleId
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class UserAccessPolicyTest {

  private val userId = UserId("auth0|user-1")
  private val vehicleId = VehicleId(9013L)

  @Test
  fun `should allow listing users when actor is admin user`() {
    val actor: Actor = Actor.User(userId, Role.ADMIN)
    assertTrue(actor.canListUsers())
  }

  @Test
  fun `should deny listing users when actor is viewer user`() {
    val actor: Actor = Actor.User(userId, Role.VIEWER)
    assertFalse(actor.canListUsers())
  }

  @Test
  fun `should allow creating user when actor is admin user`() {
    val actor: Actor = Actor.User(userId, Role.ADMIN)
    assertTrue(actor.canCreateUser())
  }

  @Test
  fun `should deny creating user when actor is viewer user`() {
    val actor: Actor = Actor.User(userId, Role.VIEWER)
    assertFalse(actor.canCreateUser())
  }

  @Test
  fun `should allow updating user when actor is admin user`() {
    val actor: Actor = Actor.User(userId, Role.ADMIN)
    assertTrue(actor.canUpdateUser())
  }

  @Test
  fun `should deny updating user when actor is viewer user`() {
    val actor: Actor = Actor.User(userId, Role.VIEWER)
    assertFalse(actor.canUpdateUser())
  }

  @Test
  fun `should allow deleting user when actor is admin user`() {
    val actor: Actor = Actor.User(userId, Role.ADMIN)
    assertTrue(actor.canDeleteUser())
  }

  @Test
  fun `should deny deleting user when actor is viewer user`() {
    val actor: Actor = Actor.User(userId, Role.VIEWER)
    assertFalse(actor.canDeleteUser())
  }
}
