package de.sauter_gmbh.s_plan_api.config

import com.zaxxer.hikari.HikariDataSource
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import org.springframework.boot.jdbc.autoconfigure.DataSourceProperties

class DataSourceConfigurationTest {

  private val sut = DataSourceConfiguration()

  @Test
  fun `main data source should set statement, lock and socket timeouts`() {
    // when:
    val dataSource = sut.mainDataSource(properties())

    // then:
    val hikari = assertIs<HikariDataSource>(dataSource)
    assertEquals(
      "-c statement_timeout=45000 -c lock_timeout=5000",
      hikari.dataSourceProperties.getProperty("options"),
    )
    assertEquals("60", hikari.dataSourceProperties.getProperty("socketTimeout"))
  }

  private fun properties() =
    DataSourceProperties().apply {
      url = "jdbc:postgresql://localhost:5432/test"
      username = "user"
      password = "pass"
      driverClassName = "org.postgresql.Driver"
    }
}
