package de.sauter_gmbh.s_plan_api.user_management_infrastructure.repository

import de.sauter_gmbh.s_plan_api.domain_identity.UserId
import de.sauter_gmbh.s_plan_api.user_management_domain.model.NewUser
import de.sauter_gmbh.s_plan_api.user_management_domain.model.User
import de.sauter_gmbh.s_plan_api.user_management_infrastructure.datasource.database.UserDataSource
import de.sauter_gmbh.s_plan_api.user_management_infrastructure.datasource.database.entity.UserEntity
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class UserRepositoryImplTest {

  private val dataSource = mockk<UserDataSource>(relaxUnitFun = true)

  private val sut = UserRepositoryImpl(dataSource)

  private val userId = UserId("jdoe")

  @Test
  fun `should return mapped users when dataSource returns entities`() {
    // given:
    every { dataSource.findAll() } returns listOf(testEntity())

    // when:
    val result = sut.findAll()

    // then:
    assertEquals(1, result.size)
    val user = result[0]
    assertEquals(UserId("jdoe"), user.id)
    assertEquals("John", user.firstName)
    assertEquals("Doe", user.lastName)
    assertEquals("admin", user.role)
    assertEquals(1_717_000_000_000L, user.createdAt)
    assertEquals(1_717_100_000_000L, user.updatedAt)
    verify(exactly = 1) { dataSource.findAll() }
  }

  @Test
  fun `should return empty list when dataSource returns no entities`() {
    // given:
    every { dataSource.findAll() } returns emptyList()

    // when:
    val result = sut.findAll()

    // then:
    assertEquals(emptyList(), result)
  }

  @Test
  fun `should return mapped user when dataSource finds entity by id`() {
    // given:
    every { dataSource.findById("jdoe") } returns testEntity()

    // when:
    val result = sut.findById(userId)

    // then:
    assertEquals(UserId("jdoe"), result?.id)
    assertEquals("John", result?.firstName)
    verify(exactly = 1) { dataSource.findById("jdoe") }
  }

  @Test
  fun `should return null when dataSource finds no entity by id`() {
    // given:
    every { dataSource.findById("jdoe") } returns null

    // when:
    val result = sut.findById(userId)

    // then:
    assertNull(result)
  }

  @Test
  fun `should insert and return created user when creating`() {
    // given:
    every { dataSource.findById("jdoe") } returns testEntity()

    // when:
    val result = sut.create(testNewUser(), createdBy = "admin1")

    // then:
    assertEquals(UserId("jdoe"), result.id)
    assertEquals("John", result.firstName)
    verify(exactly = 1) {
      dataSource.insert(
        id = "jdoe",
        salutation = "Herr",
        title = "Dr.",
        firstName = "John",
        lastName = "Doe",
        authType = "auth0",
        authUser = "auth0|abc",
        role = "admin",
        createdBy = "admin1",
        timestampMillis = any(),
      )
    }
  }

  @Test
  fun `should update and return updated user when updating`() {
    // given:
    every {
      dataSource.update(any(), any(), any(), any(), any(), any(), any(), any(), any(), any())
    } returns 1
    every { dataSource.findById("jdoe") } returns testEntity()

    // when:
    val result = sut.update(testUser(), updatedBy = "admin1")

    // then:
    assertEquals(UserId("jdoe"), result.id)
    verify(exactly = 1) {
      dataSource.update(
        id = "jdoe",
        salutation = "Herr",
        title = "Dr.",
        firstName = "John",
        lastName = "Doe",
        authType = "auth0",
        authUser = "auth0|abc",
        role = "admin",
        updatedBy = "admin1",
        timestampMillis = any(),
      )
    }
  }

  @Test
  fun `should throw NoSuchElementException when updating a missing user`() {
    // given:
    every {
      dataSource.update(any(), any(), any(), any(), any(), any(), any(), any(), any(), any())
    } returns 0

    // when / then:
    assertFailsWith<NoSuchElementException> { sut.update(testUser(), updatedBy = "admin1") }
  }

  @Test
  fun `should delete user when dataSource removes one row`() {
    // given:
    every { dataSource.delete("jdoe") } returns 1

    // when:
    sut.delete(userId)

    // then:
    verify(exactly = 1) { dataSource.delete("jdoe") }
  }

  @Test
  fun `should throw NoSuchElementException when deleting a missing user`() {
    // given:
    every { dataSource.delete("jdoe") } returns 0

    // when / then:
    assertFailsWith<NoSuchElementException> { sut.delete(userId) }
  }

  private fun testEntity() =
    UserEntity(
      id = "jdoe",
      salutation = "Herr",
      title = "Dr.",
      firstName = "John",
      lastName = "Doe",
      authType = "auth0",
      authUser = "auth0|abc",
      createdAtMillis = 1_717_000_000_000L,
      createdBy = "system",
      updatedAtMillis = 1_717_100_000_000L,
      updatedBy = "system",
      role = "admin",
    )

  private fun testNewUser() =
    NewUser(
      id = userId,
      salutation = "Herr",
      title = "Dr.",
      firstName = "John",
      lastName = "Doe",
      authType = "auth0",
      authUser = "auth0|abc",
      role = "admin",
    )

  private fun testUser() =
    User(
      id = userId,
      salutation = "Herr",
      title = "Dr.",
      firstName = "John",
      lastName = "Doe",
      authType = "auth0",
      authUser = "auth0|abc",
      role = "admin",
      createdAt = 1_717_000_000_000L,
      createdBy = "system",
      updatedAt = 1_717_100_000_000L,
      updatedBy = "system",
    )
}
