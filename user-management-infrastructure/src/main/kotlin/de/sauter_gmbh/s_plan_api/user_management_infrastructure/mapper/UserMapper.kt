package de.sauter_gmbh.s_plan_api.user_management_infrastructure.mapper

import de.sauter_gmbh.s_plan_api.domain_identity.UserId
import de.sauter_gmbh.s_plan_api.user_management_domain.model.User
import de.sauter_gmbh.s_plan_api.user_management_infrastructure.datasource.database.entity.UserEntity

internal fun UserEntity.toModel(): User =
  User(
    id = UserId(id),
    salutation = salutation,
    title = title,
    firstName = firstName,
    lastName = lastName,
    authType = authType,
    authUser = authUser,
    role = role,
    createdAt = createdAtMillis,
    createdBy = createdBy,
    updatedAt = updatedAtMillis,
    updatedBy = updatedBy,
  )
