package de.sauter_gmbh.s_plan_api.user_management_infrastructure.datasource.database

import de.sauter_gmbh.s_plan_api.user_management_infrastructure.datasource.database.entity.UserEntity

internal interface UserDataSource {
  fun findAll(): List<UserEntity>

  fun findById(id: String): UserEntity?

  fun insert(
    id: String,
    salutation: String?,
    title: String?,
    firstName: String?,
    lastName: String?,
    authType: String?,
    authUser: String?,
    role: String?,
    createdBy: String,
    timestampMillis: Long
  )

  fun update(
    id: String,
    salutation: String?,
    title: String?,
    firstName: String?,
    lastName: String?,
    authType: String?,
    authUser: String?,
    role: String?,
    updatedBy: String,
    timestampMillis: Long
  ): Int

  fun delete(id: String): Int
}
