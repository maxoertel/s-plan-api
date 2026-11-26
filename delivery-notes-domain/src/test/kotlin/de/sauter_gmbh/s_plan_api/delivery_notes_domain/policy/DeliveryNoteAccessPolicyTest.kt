package de.sauter_gmbh.s_plan_api.delivery_notes_domain.policy

import de.sauter_gmbh.s_plan_api.domain_identity.Actor
import de.sauter_gmbh.s_plan_api.domain_identity.Role
import de.sauter_gmbh.s_plan_api.domain_identity.UserId
import de.sauter_gmbh.s_plan_api.domain_identity.VehicleId
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class DeliveryNoteAccessPolicyTest {

  private val userId = UserId("auth0|user-1")
  private val vehicleId = VehicleId(9013L)

  @Test
  fun `should allow listing all delivery notes when actor is admin user`() {
    // given:
    val actor: Actor = Actor.User(userId, Role.ADMIN)

    // when:
    val result = actor.canListAllDeliveryNotes()

    // then:
    assertTrue(result)
  }

  @Test
  fun `should deny listing all delivery notes when actor is viewer user`() {
    // given:
    val actor: Actor = Actor.User(userId, Role.VIEWER)

    // when:
    val result = actor.canListAllDeliveryNotes()

    // then:
    assertFalse(result)
  }

  @Test
  fun `should allow viewing delivery note signature when actor is admin user`() {
    // given:
    val actor: Actor = Actor.User(userId, Role.ADMIN)

    // when:
    val result = actor.canViewDeliveryNoteSignature()

    // then:
    assertTrue(result)
  }

  @Test
  fun `should deny viewing delivery note signature when actor is viewer user`() {
    // given:
    val actor: Actor = Actor.User(userId, Role.VIEWER)

    // when:
    val result = actor.canViewDeliveryNoteSignature()

    // then:
    assertFalse(result)
  }

  @Test
  fun `should allow viewing delivery note pdf when actor is admin user`() {
    // given:
    val actor: Actor = Actor.User(userId, Role.ADMIN)

    // when:
    val result = actor.canViewDeliveryNotePdf()

    // then:
    assertTrue(result)
  }

  @Test
  fun `should deny viewing delivery note pdf when actor is viewer user`() {
    // given:
    val actor: Actor = Actor.User(userId, Role.VIEWER)

    // when:
    val result = actor.canViewDeliveryNotePdf()

    // then:
    assertFalse(result)
  }

  @Test
  fun `should allow editing delivery note status when actor is admin user`() {
    // given:
    val actor: Actor = Actor.User(userId, Role.ADMIN)

    // when:
    val result = actor.canEditDeliveryNoteStatus()

    // then:
    assertTrue(result)
  }

  @Test
  fun `should deny editing delivery note status when actor is viewer user`() {
    // given:
    val actor: Actor = Actor.User(userId, Role.VIEWER)

    // when:
    val result = actor.canEditDeliveryNoteStatus()

    // then:
    assertFalse(result)
  }
}
