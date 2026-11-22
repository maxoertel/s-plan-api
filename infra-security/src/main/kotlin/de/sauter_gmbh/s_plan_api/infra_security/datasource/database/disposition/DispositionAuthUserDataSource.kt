package de.sauter_gmbh.s_plan_api.infra_security.datasource.database.disposition

import de.sauter_gmbh.s_plan_api.infra_db.condition.ConditionalOnAppMode
import de.sauter_gmbh.s_plan_api.infra_security.datasource.database.AuthUserDataSource
import de.sauter_gmbh.s_plan_api.infra_security.datasource.database.entity.AuthUserEntity
import de.sauter_gmbh.s_plan_api.shared_core.mode.AppMode
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Component

@Component
@ConditionalOnAppMode(AppMode.S_PLAN)
internal class DispositionAuthUserDataSource(private val jdbcTemplate: JdbcTemplate) :
  AuthUserDataSource {

  private val rowMapper = RowMapper { rs, _ ->
    AuthUserEntity(userId = rs.getString("benutzer"), role = rs.getString("rolle"))
  }

  override fun findAuthUserByAuth0Id(auth0Id: String): AuthUserEntity? {
    val users =
      jdbcTemplate.query(
        """
        SELECT benutzer, rolle
        FROM sauter_splan_benutzer
        WHERE authentifizierungstyp = 'auth0' AND authentifizierungsbenutzer = ?
        """
          .trimIndent(),
        rowMapper,
        auth0Id,
      )

    if (users.size > 1) {
      throw IllegalStateException("Multiple users found for auth0 id '$auth0Id'")
    }

    return users.firstOrNull()
  }
}
