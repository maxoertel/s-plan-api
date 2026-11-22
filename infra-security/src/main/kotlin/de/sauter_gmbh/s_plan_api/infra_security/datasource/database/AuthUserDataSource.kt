package de.sauter_gmbh.s_plan_api.infra_security.datasource.database

import de.sauter_gmbh.s_plan_api.infra_security.datasource.database.entity.AuthUserEntity

internal interface AuthUserDataSource {
  fun findAuthUserByAuth0Id(auth0Id: String): AuthUserEntity?
}
