package com.convoy.dtd.tos.web.core

import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.context.annotation.ComponentScan
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource
import com.convoy.dtd.johnston.Johnston
import org.springframework.boot.autoconfigure.web.ErrorMvcAutoConfiguration
import org.springframework.boot.autoconfigure.gson.GsonAutoConfiguration
import org.springframework.boot.autoconfigure.jmx.JmxAutoConfiguration
import org.springframework.boot.autoconfigure.groovy.template.GroovyTemplateAutoConfiguration
import org.springframework.boot.autoconfigure.websocket.WebSocketAutoConfiguration
import com.convoy.dtd.tos.web.core.dao.TestDao
import com.convoy.dtd.tos.web.api.entity.TestBean
import com.convoy.dtd.tos.Tos
import com.convoy.dtd.johnston.web.adapter.filter.AbstractCorsFilter
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean

@Configuration
@EnableAutoConfiguration(exclude=Array(
		classOf[GroovyTemplateAutoConfiguration],
		classOf[GsonAutoConfiguration],
		classOf[WebSocketAutoConfiguration],
		classOf[ErrorMvcAutoConfiguration],
		classOf[JmxAutoConfiguration]
))
@EnableJpaRepositories(basePackageClasses = Array(classOf[TestDao]))
@EntityScan(basePackageClasses = Array(classOf[TestBean]))
@ComponentScan(basePackageClasses = Array(classOf[Tos], classOf[Johnston]))
class TosApplication 
{
  private object CorsFilter extends AbstractCorsFilter
  
  @Bean
  private[core] def corsFilter:FilterRegistrationBean =
  {
    val fgb = new FilterRegistrationBean(CorsFilter)
    fgb.setOrder(Integer.MIN_VALUE + 1)
    return fgb
  }
}