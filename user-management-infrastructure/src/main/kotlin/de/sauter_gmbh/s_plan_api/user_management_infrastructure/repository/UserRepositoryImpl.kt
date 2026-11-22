package de.sauter_gmbh.s_plan_api.user_management_infrastructure.repository

import de.sauter_gmbh.s_plan_api.domain_identity.UserId
import de.sauter_gmbh.s_plan_api.user_management_domain.model.NewUser
import de.sauter_gmbh.s_plan_api.user_management_domain.model.User
import de.sauter_gmbh.s_plan_api.user_management_domain.repository.UserRepository
import de.sauter_gmbh.s_plan_api.user_management_infrastructure.datasource.database.UserDataSource
import de.sauter_gmbh.s_plan_api.user_management_infrastructure.mapper.toModel
import org.springframework.stereotype.Repository

@Repository
internal class UserRepositoryImpl(
  private val dataSource: UserDataSource,
) : UserRepository {

  override fun findAll(): List<User> = dataSource.findAll().map { it.toModel() }

  override fun findById(id: UserId): User? = dataSource.findById(id.value)?.toModel()

  override fun create(user: NewUser, createdBy: String): User {
    dataSource.insert(
      id = user.id.value,
      salutation = user.salutation,
      title = user.title,
      firstName = user.firstName,
      lastName = user.lastName,
      authType = user.authType,
      authUser = user.authUser,
      role = user.role,
      createdBy = createdBy,
      timestampMillis = System.currentTimeMillis(),
    )

    val createdEntity =
      dataSource.findById(user.id.value) ?: error("User not found after insert: ${user.id.value}")

    return createdEntity.toModel()
  }

  override fun update(user: User, updatedBy: String): User {
    val rows =
      dataSource.update(
        id = user.id.value,
        salutation = user.salutation,
        title = user.title,
        firstName = user.firstName,
        lastName = user.lastName,
        authType = user.authType,
        authUser = user.authUser,
        role = user.role,
        updatedBy = updatedBy,
        timestampMillis = System.currentTimeMillis(),
      )
    if (rows != 1) throw NoSuchElementException("User not found: ${user.id.value}")

    val updatedEntity =
      dataSource.findById(user.id.value) ?: error("User not found after update: ${user.id.value}")

    return updatedEntity.toModel()
  }

  override fun delete(id: UserId) {
    val rows = dataSource.delete(id.value)
    if (rows != 1) throw NoSuchElementException("User not found: ${id.value}")
  }
}
