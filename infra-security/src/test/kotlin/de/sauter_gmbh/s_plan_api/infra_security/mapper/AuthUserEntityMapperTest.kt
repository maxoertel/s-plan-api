package de.sauter_gmbh.s_plan_api.infra_security.mapper

import de.sauter_gmbh.s_plan_api.domain_identity.Role
import de.sauter_gmbh.s_plan_api.domain_identity.UserId
import de.sauter_gmbh.s_plan_api.infra_security.datasource.database.entity.AuthUserEntity
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class AuthUserEntityMapperTest {

  @Test
  fun `should map entity to admin user when role key is admin`() {
    // given:
    val entity = AuthUserEntity(userId = "auth0|abc", role = "admin")

    // when:
    val result = entity.toModel()

    // then:
    assertEquals(UserId("auth0|abc"), result.userId)
    assertEquals(Role.ADMIN, result.role)
  }

  @Test
  fun `should map entity to viewer user when role key is viewer`() {
    // given:
    val entity = AuthUserEntity(userId = "auth0|xyz", role = "viewer")

    // when:
    val result = entity.toModel()

    // then:
    assertEquals(UserId("auth0|xyz"), result.userId)
    assertEquals(Role.VIEWER, result.role)
  }

  @Test
  fun `should throw IllegalArgumentException when role key is unknown`() {
    // given:
    val entity = AuthUserEntity(userId = "auth0|abc", role = "superuser")

    // when / then:
    val exception = assertFailsWith<IllegalArgumentException> { entity.toModel() }
    assertEquals("Unknown role: superuser", exception.message)
  }
}
