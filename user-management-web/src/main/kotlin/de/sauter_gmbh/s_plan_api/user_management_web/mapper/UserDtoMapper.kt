package de.sauter_gmbh.s_plan_api.user_management_web.mapper

import de.sauter_gmbh.s_plan_api.domain_identity.UserId
import de.sauter_gmbh.s_plan_api.user_management_domain.model.NewUser
import de.sauter_gmbh.s_plan_api.user_management_domain.model.User
import de.sauter_gmbh.s_plan_api.user_management_web.dto.CreateUserRequestDto
import de.sauter_gmbh.s_plan_api.user_management_web.dto.UpdateUserRequestDto
import de.sauter_gmbh.s_plan_api.user_management_web.dto.UserDto

internal fun User.toDto(): UserDto =
  UserDto(
    id = id.value,
    salutation = salutation,
    title = title,
    firstName = firstName,
    lastName = lastName,
    authType = authType,
    authUser = authUser,
    role = role,
    createdAt = createdAt,
    createdBy = createdBy,
    updatedAt = updatedAt,
    updatedBy = updatedBy,
  )

internal fun CreateUserRequestDto.toModel(): NewUser =
  NewUser(
    id = UserId(id),
    salutation = salutation,
    title = title,
    firstName = firstName,
    lastName = lastName,
    authType = authType,
    authUser = authUser,
    role = role,
  )

// (null) fields keep their stored value -> so a field cannot be cleared through this patch.
internal fun UpdateUserRequestDto.toPatch(): (User) -> User = { user ->
  user.copy(
    salutation = salutation ?: user.salutation,
    title = title ?: user.title,
    firstName = firstName ?: user.firstName,
    lastName = lastName ?: user.lastName,
    authType = authType ?: user.authType,
    authUser = authUser ?: user.authUser,
    role = role ?: user.role,
  )
}
