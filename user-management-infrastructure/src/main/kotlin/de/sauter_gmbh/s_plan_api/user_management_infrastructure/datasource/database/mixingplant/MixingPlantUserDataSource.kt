package de.sauter_gmbh.s_plan_api.user_management_infrastructure.datasource.database.mixingplant

import de.sauter_gmbh.s_plan_api.infra_db.condition.ConditionalOnAppMode
import de.sauter_gmbh.s_plan_api.shared_core.mode.AppMode
import de.sauter_gmbh.s_plan_api.user_management_infrastructure.datasource.database.UserDataSource
import de.sauter_gmbh.s_plan_api.user_management_infrastructure.datasource.database.entity.UserEntity
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Component

private const val NOT_IMPLEMENTED_MESSAGE =
  "User data source is not implemented for mixing plant database system"

@Component
@ConditionalOnAppMode(AppMode.S_WORK)
internal class MixingPlantUserDataSource(private val jdbcTemplate: JdbcTemplate) : UserDataSource {

  override fun findAll(): List<UserEntity> = throw NotImplementedError(NOT_IMPLEMENTED_MESSAGE)

  override fun findById(id: String): UserEntity? =
    throw NotImplementedError(NOT_IMPLEMENTED_MESSAGE)

  override fun insert(
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
  ) = throw NotImplementedError(NOT_IMPLEMENTED_MESSAGE)

  override fun update(
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
  ): Int = throw NotImplementedError(NOT_IMPLEMENTED_MESSAGE)

  override fun delete(id: String): Int = throw NotImplementedError(NOT_IMPLEMENTED_MESSAGE)
}
