package com.convoy.dtd.tos.web.rest

import org.slf4j.LoggerFactory
import org.springframework.boot.SpringApplication
import com.convoy.dtd.tos.web.core.TosApplication

object TosRest 
{
  private val logger = LoggerFactory.getLogger(this.getClass)
  
  def main(args:Array[String]):Unit =
  {
    val ctx = SpringApplication.run(classOf[TosApplication], args:_*)
    if(logger.isDebugEnabled())
    {
      ctx.getBeanDefinitionNames.sorted.foreach{ logger.debug(_)} 
    }
  }
  
}