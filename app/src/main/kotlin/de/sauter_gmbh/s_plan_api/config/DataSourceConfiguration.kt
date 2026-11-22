package de.sauter_gmbh.s_plan_api.config

import com.zaxxer.hikari.HikariDataSource
import java.time.Duration
import javax.sql.DataSource
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.jdbc.autoconfigure.DataSourceProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.jdbc.core.JdbcTemplate

@Configuration(proxyBeanMethods = false)
class DataSourceConfiguration {

  @Bean
  @Primary
  @ConfigurationProperties("spring.datasource")
  fun mainDataSourceProperties(): DataSourceProperties = DataSourceProperties()

  @Bean
  @Primary
  fun mainDataSource(
    @Qualifier("mainDataSourceProperties") properties: DataSourceProperties
  ): DataSource =
    properties.initializeDataSourceBuilder().type(HikariDataSource::class.java).build().apply {
      addDataSourceProperty(
        "options",
        "-c statement_timeout=${STATEMENT_TIMEOUT.toMillis()} " +
          "-c lock_timeout=${LOCK_TIMEOUT.toMillis()}",
      )
      addDataSourceProperty("socketTimeout", SOCKET_TIMEOUT.toSeconds().toString())
    }

  @Bean
  @Primary
  fun jdbcTemplate(@Qualifier("mainDataSource") dataSource: DataSource): JdbcTemplate =
    JdbcTemplate(dataSource)

  private companion object {
    val STATEMENT_TIMEOUT: Duration = Duration.ofSeconds(45)
    val LOCK_TIMEOUT: Duration = Duration.ofSeconds(5)
    val SOCKET_TIMEOUT: Duration = Duration.ofSeconds(60)
  }
}
