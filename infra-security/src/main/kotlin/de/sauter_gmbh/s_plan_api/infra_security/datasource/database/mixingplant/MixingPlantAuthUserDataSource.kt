package de.sauter_gmbh.s_plan_api.infra_security.datasource.database.mixingplant

import de.sauter_gmbh.s_plan_api.infra_db.condition.ConditionalOnAppMode
import de.sauter_gmbh.s_plan_api.infra_security.datasource.database.AuthUserDataSource
import de.sauter_gmbh.s_plan_api.infra_security.datasource.database.entity.AuthUserEntity
import de.sauter_gmbh.s_plan_api.shared_core.mode.AppMode
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Component

private const val NOT_IMPLEMENTED_MESSAGE =
  "Auth user data source is not implemented for mixing plant database system"

@Component
@ConditionalOnAppMode(AppMode.S_WORK)
internal class MixingPlantAuthUserDataSource(private val jdbcTemplate: JdbcTemplate) :
  AuthUserDataSource {

  override fun findAuthUserByAuth0Id(auth0Id: String): AuthUserEntity? =
    throw NotImplementedError(NOT_IMPLEMENTED_MESSAGE)
}
