package de.sauter_gmbh.s_plan_api.user_management_domain.repository

import de.sauter_gmbh.s_plan_api.domain_identity.UserId
import de.sauter_gmbh.s_plan_api.user_management_domain.model.NewUser
import de.sauter_gmbh.s_plan_api.user_management_domain.model.User

interface UserRepository {
  fun findAll(): List<User>

  fun findById(id: UserId): User?

  fun create(user: NewUser, createdBy: String): User

  fun update(user: User, updatedBy: String): User

  fun delete(id: UserId)
}
