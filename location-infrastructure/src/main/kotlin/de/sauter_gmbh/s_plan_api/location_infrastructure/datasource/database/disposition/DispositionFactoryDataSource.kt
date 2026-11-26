package de.sauter_gmbh.s_plan_api.location_infrastructure.datasource.database.disposition

import de.sauter_gmbh.s_plan_api.infra_db.condition.ConditionalOnAppMode
import de.sauter_gmbh.s_plan_api.location_infrastructure.datasource.database.FactoryDataSource
import de.sauter_gmbh.s_plan_api.location_infrastructure.datasource.entity.FactoryEntity
import de.sauter_gmbh.s_plan_api.shared_core.mode.AppMode
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Component

@Component
@ConditionalOnAppMode(AppMode.S_PLAN)
internal class DispositionFactoryDataSource(private val jdbcTemplate: JdbcTemplate) :
  FactoryDataSource {

  private val rowMapper = RowMapper { rs, _ ->
    FactoryEntity(
      id = rs.getLong("werksnummer"),
      matchCode = rs.getString("werksmatchcode"),
      name = rs.getString("werksname"),
      street = rs.getString("werkstrasse"),
      houseNumber = rs.getString("werkhausnummer"),
      zip = rs.getString("werkplz"),
      city = rs.getString("werkort"),
      country = rs.getString("werkland"),
      phone = rs.getString("werkstelefon"),
      latitude = rs.getString("werklatitude"),
      longitude = rs.getString("werklongitude"),
      radiusInMeter = rs.getObject("radiusumwerkinmeter") as? Int,
    )
  }

  private val selectClause =
    """
    SELECT werksnummer, werksmatchcode, werksname, werkstrasse, werkhausnummer, werkplz, werkort,
           werkland, werkstelefon, werklatitude, werklongitude, radiusumwerkinmeter
    FROM werke
    """
      .trimIndent()

  override fun findAll(): List<FactoryEntity> = jdbcTemplate.query(selectClause, rowMapper)
}
