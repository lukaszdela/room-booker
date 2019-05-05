package eu.lukks.config;


import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{
	@Autowired
	private DataSource dataSource;
	
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception{
		authenticationManagerBuilder.jdbcAuthentication().dataSource(dataSource)
		.passwordEncoder(new BCryptPasswordEncoder())
		.usersByUsernameQuery("select username, password, enabled from users where username=?")
		.authoritiesByUsernameQuery("select username, role from user_roles where username=?");
	}
	
	@Override
    protected void configure(HttpSecurity http) throws Exception {
		 http
         .authorizeRequests()
         .antMatchers("/admin/**")
         .hasRole("ADMIN")
             .antMatchers("/", "/reservation/**", "/list/**").permitAll()
             .anyRequest().authenticated()
             .and()
         .formLogin()
             .loginPage("/login").defaultSuccessUrl("/admin/reservations")
             .permitAll()
             .and()
         .logout().logoutSuccessUrl("/").logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
             .permitAll();
	}
	
	 @Override
	    public void configure(WebSecurity web) throws Exception {
	        web
	                .ignoring()
	                .antMatchers("/resources/**", "/static/**", "/css/**", "/js/**", "/images/**", "/upload/**", "/webjars/**");
	}
	 
}
