package com.example.demo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.thymeleaf.extras.springsecurity6.dialect.SpringSecurityDialect;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
	@Bean 
	public MemberDetailsService memberDetailsService() {
		return new MemberDetailsService();
		
	}
	
	
	
	@Bean 
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider provider=new DaoAuthenticationProvider();
		provider.setUserDetailsService(memberDetailsService());
		provider.setPasswordEncoder(passwordEncoder());
		return provider; 
	}
	
	
	
	/*@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		
		http.authorizeHttpRequests(
				(requests) -> requests
				.requestMatchers("/").permitAll()
				.requestMatchers("/addToFavorites").permitAll()
				.requestMatchers("/index").permitAll()
				.requestMatchers("/members").hasRole("ADMIN")
				.requestMatchers("/bootstrap//").permitAll()
				.requestMatchers("/images/*").permitAll()				
				.anyRequest().authenticated())	
				.formLogin(form -> form
                .loginPage("/login").permitAll()
                .successHandler((request, response, authentication) -> {
                    // Custom logic to redirect based on user's role
                    if (authentication.getAuthorities().stream()
                            .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"))) {
                        response.sendRedirect("/members");
                    } else {
                        response.sendRedirect("/index");
                    }
                }))
		
		.oauth2Login(oauth2 -> oauth2
                .loginPage("/login")
                .defaultSuccessUrl("/index", true))
				//.formLogin().loginPage("/login").permitAll()
				
				.logout((logout) -> logout.permitAll())	
				.exceptionHandling().accessDeniedPage("/403");
				
		return http.build();
	}*/
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests(
				(requests) -> requests.requestMatchers(request -> request.getServletPath().equals("/")).permitAll()
												
									
						.requestMatchers(request -> request.getServletPath().startsWith("/bootstrap/")).permitAll()
						.requestMatchers(request -> request.getServletPath().startsWith("/images/")).permitAll()
						.requestMatchers(request -> request.getServletPath().startsWith("/image/")).permitAll()
						.requestMatchers(request -> request.getServletPath().startsWith("/uploads/cards/")).permitAll()
						.requestMatchers(request -> request.getServletPath().startsWith("/uploads/members/")).permitAll()
						.requestMatchers(request -> request.getServletPath().startsWith("/uploads/packages/")).permitAll()
						.requestMatchers(request -> request.getServletPath().startsWith("/uploads/restaurants/")).permitAll()
						.requestMatchers(request -> request.getServletPath().startsWith("/uploads/trippackages/")).permitAll()
						.requestMatchers(request -> request.getServletPath().startsWith("/package/")).permitAll()
						.requestMatchers(request -> request.getServletPath().startsWith("/restaurant/")).permitAll()
						.requestMatchers(request -> request.getServletPath().startsWith("/trippackages/")).permitAll()
						.requestMatchers(request -> request.getServletPath().startsWith("/register")).permitAll()
						.requestMatchers(request -> request.getServletPath().startsWith("/TripPackagesView")).permitAll()
						.requestMatchers(request -> request.getServletPath().startsWith("/viewhotel")).permitAll()
						.requestMatchers(request -> request.getServletPath().startsWith("/viewrestaurant")).permitAll()
						.requestMatchers(request -> request.getServletPath().startsWith("/reset-password")).permitAll()
						.requestMatchers(request -> request.getServletPath().startsWith("/change-password")).permitAll()
						.requestMatchers(request -> request.getServletPath().startsWith("/aboutus")).permitAll()
						.requestMatchers(request -> request.getServletPath().startsWith("/information")).permitAll()
						.requestMatchers(request -> request.getServletPath().startsWith("/search")).permitAll()
						.requestMatchers(request -> request.getServletPath().startsWith("/subscribe")).permitAll()
						.requestMatchers("/members").hasRole("ADMIN")
						.requestMatchers(request -> request.getServletPath().startsWith("/card/")).permitAll()
						.requestMatchers(request -> request.getServletPath().startsWith("/css/")).permitAll()
						.requestMatchers(request -> request.getServletPath().startsWith("/js/")).permitAll()
						.anyRequest().authenticated())
		
						.formLogin(form -> form
						.loginPage("/login").permitAll()
				        .successHandler((request, response, authentication) -> {
				            // Custom logic to redirect based on user's role
				            if (authentication.getAuthorities().stream()
				                    .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"))) {
				                response.sendRedirect("/members");
				            } else {
				                response.sendRedirect("/");
				            }
				        }))

						 .logout(logout -> logout
						            .logoutUrl("/logout")
						            .logoutSuccessUrl("/signout") // Redirect to /signout after successful logout
						            .permitAll()
						    ).exceptionHandling()
				.accessDeniedPage("/403");

		return http.build();
	}
}
