package com.convoy.dtd.tos

import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true) class SecurityConfig extends WebSecurityConfigurerAdapter {
  @throws[Exception]
  override protected def configure(http: HttpSecurity): Unit = {
    http.antMatcher("/**")
      .authorizeRequests()
      .antMatchers("/").permitAll()
      .anyRequest().authenticated()
      //.and().formLogin()
  }
}
