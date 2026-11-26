package de.sauter_gmbh.s_plan_api.user_management_web.mapper

import de.sauter_gmbh.s_plan_api.domain_identity.UserId
import de.sauter_gmbh.s_plan_api.user_management_domain.model.User
import de.sauter_gmbh.s_plan_api.user_management_web.dto.CreateUserRequestDto
import de.sauter_gmbh.s_plan_api.user_management_web.dto.UpdateUserRequestDto
import kotlin.test.Test
import kotlin.test.assertEquals

class UserDtoMapperTest {

  private val userId = UserId("jdoe")

  @Test
  fun `should map all fields when mapping user to dto`() {
    // given:
    val user = testUser()

    // when:
    val dto = user.toDto()

    // then:
    assertEquals("jdoe", dto.id)
    assertEquals("Herr", dto.salutation)
    assertEquals("Dr.", dto.title)
    assertEquals("John", dto.firstName)
    assertEquals("Doe", dto.lastName)
    assertEquals("auth0", dto.authType)
    assertEquals("auth0|abc", dto.authUser)
    assertEquals("viewer", dto.role)
    assertEquals(1_717_000_000_000L, dto.createdAt)
    assertEquals("system", dto.createdBy)
    assertEquals(1_717_100_000_000L, dto.updatedAt)
    assertEquals("system", dto.updatedBy)
  }

  @Test
  fun `should map all fields when mapping create request to new user`() {
    // given:
    val request =
      CreateUserRequestDto(
        id = "jdoe",
        salutation = "Herr",
        title = "Dr.",
        firstName = "John",
        lastName = "Doe",
        authType = "auth0",
        authUser = "auth0|abc",
        role = "viewer",
      )

    // when:
    val newUser = request.toModel()

    // then:
    assertEquals(userId, newUser.id)
    assertEquals("Herr", newUser.salutation)
    assertEquals("Dr.", newUser.title)
    assertEquals("John", newUser.firstName)
    assertEquals("Doe", newUser.lastName)
    assertEquals("auth0", newUser.authType)
    assertEquals("auth0|abc", newUser.authUser)
    assertEquals("viewer", newUser.role)
  }

  @Test
  fun `should overwrite only provided fields when applying update patch`() {
    // given:
    val request = UpdateUserRequestDto(lastName = "Smith", role = "admin")

    // when:
    val patched = request.toPatch()(testUser())

    // then:
    assertEquals("Smith", patched.lastName)
    assertEquals("admin", patched.role)
    assertEquals("John", patched.firstName)
    assertEquals("Herr", patched.salutation)
    assertEquals("Dr.", patched.title)
    assertEquals("auth0", patched.authType)
    assertEquals("auth0|abc", patched.authUser)
  }

  @Test
  fun `should keep the stored user unchanged when update patch is empty`() {
    // given:
    val user = testUser()
    val request = UpdateUserRequestDto()

    // when:
    val patched = request.toPatch()(user)

    // then:
    assertEquals(user, patched)
  }

  @Test
  fun `should overwrite every field when update patch provides all of them`() {
    // given:
    val request =
      UpdateUserRequestDto(
        salutation = "Frau",
        title = "Prof.",
        firstName = "Jane",
        lastName = "Smith",
        authType = "ldap",
        authUser = "ldap|xyz",
        role = "admin",
      )

    // when:
    val patched = request.toPatch()(testUser())

    // then:
    assertEquals("Frau", patched.salutation)
    assertEquals("Prof.", patched.title)
    assertEquals("Jane", patched.firstName)
    assertEquals("Smith", patched.lastName)
    assertEquals("ldap", patched.authType)
    assertEquals("ldap|xyz", patched.authUser)
    assertEquals("admin", patched.role)
    assertEquals(userId, patched.id)
  }

  private fun testUser() =
    User(
      id = userId,
      salutation = "Herr",
      title = "Dr.",
      firstName = "John",
      lastName = "Doe",
      authType = "auth0",
      authUser = "auth0|abc",
      role = "viewer",
      createdAt = 1_717_000_000_000L,
      createdBy = "system",
      updatedAt = 1_717_100_000_000L,
      updatedBy = "system",
    )
}
