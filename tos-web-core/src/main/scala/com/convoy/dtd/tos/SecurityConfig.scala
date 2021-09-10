package com.convoy.dtd.tos


//import org.springframework.security.config.annotation.web.builders.HttpSecurity
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
//import com.convoy.dtd.tos.web.core.dao.UserDao
//import javax.inject.Inject
//import javax.servlet.http.{HttpServletRequest, HttpServletResponse}
//import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
//import org.springframework.security.config.http.SessionCreationPolicy
//import org.springframework.security.core.AuthenticationException
//import org.springframework.security.web.AuthenticationEntryPoint
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
//import org.springframework.security.core.userdetails.UsernameNotFoundException
//import org.springframework.web.filter.CorsFilter
//
//@EnableWebSecurity
//@EnableGlobalMethodSecurity(
//  securedEnabled = true,
//  jsr250Enabled = true,
//  prePostEnabled = true
//)
//class SecurityConfig extends WebSecurityConfigurerAdapter {
//
////  @Inject
//  private var userDao:UserDao = _
//
//
//
////  @throws[Exception]
////  override protected def configure(auth: AuthenticationManagerBuilder): Unit = {
////    auth.userDetailsService((email: String) => userDao.getByEmail(email).orElse(
////      throw new UsernameNotFoundException("User registered with email: %s, not found".format(email))
////    ))
////  }
//
//  @throws[Exception]
//  override protected def configure(http: HttpSecurity): Unit = {
//    // TODO configure web security
//
//    // Enable CORS and disable CSRF
//    http.cors.and.csrf.disable
//
//    // Set session management to stateless
//    http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and
//
//
//
//    http.exceptionHandling.authenticationEntryPoint(
//      new AuthenticationEntryPoint {
//        override def commence(httpServletRequest: HttpServletRequest, httpServletResponse: HttpServletResponse, e: AuthenticationException): Unit = {
//          (request:HttpServletRequest, response: HttpServletResponse, exception:AuthenticationException) => {
//            response.sendError(
//              HttpServletResponse.SC_UNAUTHORIZED, exception.getMessage
//            )}
//      }}
//    ).and
//
//    // Set permissions on endpoints
//    http.authorizeRequests()
//      // Our public endpoints
//      .antMatchers("/test/**").permitAll()
////      .antMatchers(HttpMethod.GET, "/api/author/**").permitAll()
////      .antMatchers(HttpMethod.POST, "/api/author/search").permitAll()
////      .antMatchers(HttpMethod.GET, "/api/book/**").permitAll()
////      .antMatchers(HttpMethod.POST, "/api/book/search").permitAll()
//      // Our private endpoints
////      .antMatchers("/api/admin/user/**").hasRole(Role.USER_ADMIN)
////      .antMatchers("/api/author/**").hasRole(Role.AUTHOR_ADMIN)
////      .antMatchers("/api/book/**").hasRole(Role.BOOK_ADMIN)
////      .anyRequest().authenticated();
//
//  }
//}