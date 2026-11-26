package de.sauter_gmbh.s_plan_api

import com.ninjasquad.springmockk.MockkBean
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
class SPlanApiApplicationTests {

  @Suppress("unused") @MockkBean private lateinit var jwtDecoder: JwtDecoder

  @Test fun contextLoads() {}
}
