package com.trust.ashram.res.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class TrustConfigSecurity extends WebSecurityConfigurerAdapter{
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private BCryptPasswordEncoder encode;
	
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
	
		  auth.userDetailsService(userDetailsService) .passwordEncoder(encode);
	}


	protected void configure(HttpSecurity http) throws Exception {
		http.httpBasic().disable();
		//url
		
		 /* http.authorizeRequests()
		  .antMatchers("/trust/index","/trust/employeeregisterpage","/trust/employeeregister","/trust/employeeloginpage").permitAll() //
		  .antMatchers("/trust/employeewelcomepage").hasAnyAuthority("EMPLOYEE","ADMIN")
		  .antMatchers("/trust/visitorsregisterpage").hasAnyAuthority("EMPLOYEE","ADMIN")
		  .antMatchers("/trust/deletvisitor").hasAuthority("ADMIN")
		  .antMatchers("/trust/adminfunction").hasAuthority("ADMIN")
		  //.antMatchers("/trust/deleteyoga").hasAuthority("ADMIN")
		  .antMatchers("/trust/nightstaydelete").hasAuthority("ADMIN")
		  .antMatchers("/trust/eventdelete").hasAuthority("ADMIN")
		
		  //form
		  .and() 
		  .formLogin() 
		  .loginPage("/trust/employeeloginpage")
		  .loginProcessingUrl("/login")
		  .defaultSuccessUrl("/trust/employeewelcomepage", true)
		  .failureUrl("/trust/employeeloginpage?error")

		  //logout
		  .and() 
		  .logout() 
		  .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
		  .logoutSuccessUrl("/trust/employeeloginpage?logout")
		  //execption 
		  .and() 
		  .exceptionHandling() 
		  .accessDeniedPage("/trust/accessdeniedpage") ; */
		
		 http.authorizeRequests()
		//  .antMatchers("/trust/employeewelcomepage").hasAnyAuthority("EMPLOYEE","ADMIN")
		  .antMatchers("/trust/adminfunctio").hasAnyAuthority("EMPLOYEE","ADMIN")
		  //logout
		  .and() 
		  .logout() 
		  .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
		  .logoutSuccessUrl("/trust/employeeloginpage?logout")
		  //execption 
		  .and() 
		  .exceptionHandling() 
		  .accessDeniedPage("/trust/accessdeniedpage") ;
		 		
		 		
	}
	
	
}
