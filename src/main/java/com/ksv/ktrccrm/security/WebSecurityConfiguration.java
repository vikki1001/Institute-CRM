//package com.ksv.ktrccrm.security;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.ComponentScan;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
//import org.springframework.security.config.BeanIds;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.builders.WebSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.web.authentication.logout.HeaderWriterLogoutHandler;
//import org.springframework.security.web.header.writers.ClearSiteDataHeaderWriter;
//import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
//
//import static org.springframework.security.config.Customizer.withDefaults;
//
//@Configuration
//@EnableWebSecurity
//@ComponentScan
//public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {
//
//	@Autowired
//	private BCryptPasswordEncoder bCryptPasswordEncoder;
////	@Autowired
////	private BeforeAuthenticationFilter beforeLoginFilter;
////	@Autowired
////	private LoginSuccessHandler loginSuccessHandler;
////	@Autowired
////	private LoginFailureHandler loginFailureHandler;
//	
//	@Bean(name = BeanIds.AUTHENTICATION_MANAGER)
//	@Override
//	public AuthenticationManager authenticationManagerBean() throws Exception {
//		return super.authenticationManagerBean();
//	}
//
//	@Override
//	@Bean
//	public UserDetailsService userDetailsService() {
//		return new UserDetailsServiceImpl();
//	}
//
//    @Bean
//    DaoAuthenticationProvider authenticationProvider() {
//		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
//		authProvider.setUserDetailsService(userDetailsService());
//		authProvider.setPasswordEncoder(bCryptPasswordEncoder);
//		return authProvider;
//	}
//	
//	@Override
//	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//		auth.authenticationProvider(authenticationProvider());
//	}
//
//	@Override
//	protected void configure(HttpSecurity http) throws Exception {
//        http.authorizeRequests(withDefaults())
//      //          .addFilterBefore(beforeLoginFilter, BeforeAuthenticationFilter.class)
//                .formLogin(login -> login
//                        .loginPage("/login").permitAll()
//                        .loginProcessingUrl("/loginSuccess").permitAll()
//                        .usernameParameter("loginId")
//                        .passwordParameter("password")
// 
////                        .successHandler(loginSuccessHandler)
////                       .failureHandler(loginFailureHandler)
//                        .defaultSuccessUrl("/loginSuccess"))
//                .logout(logout -> logout.logoutUrl("/logout")
//                        .addLogoutHandler(new HeaderWriterLogoutHandler( new
//                                ClearSiteDataHeaderWriter( ClearSiteDataHeaderWriter.Directive.CACHE,
//                                ClearSiteDataHeaderWriter.Directive.COOKIES,
//                                ClearSiteDataHeaderWriter.Directive.STORAGE)))
//                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET"))
//                        .clearAuthentication(true)
//                        .invalidateHttpSession(true)
//                        .deleteCookies("JSESSIONID", "remember-me")
//                        .logoutSuccessUrl("/login?logout").permitAll())
//                .exceptionHandling(handling -> handling
//                        .accessDeniedPage("/error"));
//        http.sessionManagement(management -> management
//                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
//                .invalidSessionUrl("/login"));
//	}
//	
////	@Override
////	protected void configure(HttpSecurity http) throws Exception {
////		http.authorizeRequests()
////		.and()			
////		.addFilterBefore(beforeLoginFilter, BeforeAuthenticationFilter.class)
////		.formLogin()
////		.loginPage("/login").permitAll()
////		.loginProcessingUrl("/loginSuccess").permitAll()
////		.usernameParameter("loginId")
////		.passwordParameter("password")
////		.successHandler(loginSuccessHandler)
////		.failureHandler(loginFailureHandler)
////		.defaultSuccessUrl("/loginSuccess")
////		.and()
////		.logout().logoutUrl("/logout")
////		.addLogoutHandler(new HeaderWriterLogoutHandler( new 
////		ClearSiteDataHeaderWriter( ClearSiteDataHeaderWriter.Directive.CACHE, 
////		ClearSiteDataHeaderWriter.Directive.COOKIES,
////		ClearSiteDataHeaderWriter.Directive.STORAGE)))
////		.logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET"))
////		.clearAuthentication(true)
////		.invalidateHttpSession(true)
////		.deleteCookies("JSESSIONID", "remember-me")
////		.logoutSuccessUrl("/login?logout").permitAll()
////		.and()
////		.exceptionHandling()
////		.accessDeniedPage("/error");
////		
////		http.sessionManagement()
////			.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
////            .invalidSessionUrl("/login");
////	}
//
//	@Override
//	public void configure(WebSecurity web) throws Exception {
//		web.ignoring().antMatchers("/resources/**", "/static/**", "/css/**", "/js/**", "/images/**", "/scss/**", "/ksv/**", "/vendor/**");
//	}
//}

package com.ksv.ktrccrm.security;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.HeaderWriterLogoutHandler;
import org.springframework.security.web.header.writers.ClearSiteDataHeaderWriter;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


@Configuration
@EnableWebSecurity
@ComponentScan
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	@Autowired
	private BeforeAuthenticationFilter beforeLoginFilter;
	@Autowired
	private LoginSuccessHandler loginSuccessHandler;
	@Autowired
	private LoginFailureHandler loginFailureHandler;	  
	
	@Bean(name = BeanIds.AUTHENTICATION_MANAGER)
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		//System.out.println("Inside authenticationManager bean......2");
		return super.authenticationManagerBean();
	}

	@Override
	@Bean
	public UserDetailsService userDetailsService() {
		//System.out.println("Inside userDetailsService .....8");
		return new UserDetailsServiceImpl();
	}

	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		//System.out.println("Inside  authenticationProvider.....7");
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService());
		authProvider.setPasswordEncoder(bCryptPasswordEncoder);
		return authProvider;
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		//System.out.println("Inside first configure.....6");
		auth.authenticationProvider(authenticationProvider());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {   
		//System.out.println("Inside webSecurity configure.....9");
		http.authorizeRequests()
		.and()
			.addFilterBefore(beforeLoginFilter, BeforeAuthenticationFilter.class)
		.formLogin()
		.loginPage("/login").permitAll()  
		.loginProcessingUrl("/loginSuccess").permitAll()
		.usernameParameter("loginId")
		.passwordParameter("password")
		.successHandler(loginSuccessHandler)
		.failureHandler(loginFailureHandler)
		.defaultSuccessUrl("/loginSuccess")
		.and()		
		.logout().logoutUrl("/logout")
		.addLogoutHandler(new HeaderWriterLogoutHandler( new 
		ClearSiteDataHeaderWriter( ClearSiteDataHeaderWriter.Directive.CACHE, 
		ClearSiteDataHeaderWriter.Directive.COOKIES,
		ClearSiteDataHeaderWriter.Directive.STORAGE)))
		.logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET"))
		.clearAuthentication(true)
		.invalidateHttpSession(true)
		.deleteCookies("JSESSIONID", "remember-me")
		.logoutSuccessUrl("/login?logout").permitAll()
		.and()
		.exceptionHandling()
		.accessDeniedPage("/error");
		     
        http.headers()
        //.defaultsDisabled()
        .frameOptions().sameOrigin()
  		.cacheControl()
		.and()
		.contentTypeOptions()
		.and()
        .httpStrictTransportSecurity().includeSubDomains(true).preload(true).maxAgeInSeconds(31536000)
		.and()		
		.xssProtection().block(true)
		.and()
		.contentSecurityPolicy("form-action 'self'");
        
		http.sessionManagement()
		.sessionFixation()
		.migrateSession()
		.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
		.invalidSessionUrl("/login") // Redirect URL on session timeout
        .maximumSessions(1) // Allow only one session per user
        .maxSessionsPreventsLogin(true)
        .expiredUrl("/login?expired=true"); // Redirect URL after session expires; // Prevent new logins when session limit is reached
			
	}

	@Bean
	public HttpSessionEventPublisher httpSessionEventPublisher() {
	    return new HttpSessionEventPublisher();
	}
	
	@Override
	public void configure(WebSecurity web) throws Exception {
		//System.out.println("Inside configure configure...........10");
		web.ignoring().antMatchers("/resources/**", "/static/**", "/css/**", "/js/**", "/images/**", "/scss/**", "/ksv/**", "/vendor/**");
	}
}