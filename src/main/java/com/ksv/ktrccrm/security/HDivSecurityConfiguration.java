//package com.ksv.ktrccrm.security;
//
//import org.hdiv.config.annotation.EnableHdivWebSecurity;
//import org.hdiv.config.annotation.ExclusionRegistry;
//import org.hdiv.config.annotation.configuration.HdivWebSecurityConfigurerAdapter;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.ComponentScan;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//@Configuration
//@EnableHdivWebSecurity
//@ComponentScan
//public class HDivSecurityConfiguration extends HdivWebSecurityConfigurerAdapter {
//
//	/* H-div Security */
//	@Bean
//	WebConfig webConfig() {
//		return new WebConfig();
//	}
//
//	protected static class WebConfig implements WebMvcConfigurer {
//
//		@Override
//		public void addViewControllers(final ViewControllerRegistry registry) {
//			registry.addViewController("/login").setViewName("login");
//		}
//	}
//
//	@Override
//	public void addExclusions(final ExclusionRegistry registry) {
//		registry.addUrlExclusions("/login");
//	}
//}


