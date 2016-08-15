package com.PublicacionySuscripcion.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import ve.gob.mercal.app.models.CustomAuthenticationProvider;
import ve.gob.mercal.app.models.RolAuthentication;


@Configuration
@EnableWebSecurity

public class SecurityConfig extends WebSecurityConfigurerAdapter {
    	@Autowired
	private RolAuthentication ra;
	
	@Autowired
	public CustomAuthenticationProvider customAuthenticationProvider;

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(customAuthenticationProvider);           
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {

	http.authorizeRequests() 
                .antMatchers("/admin","/pdi","/confcargas","/gestioncargas","/tiendaadmin","/gusuarios").hasRole("administrador")   
                .antMatchers("/Publicador","/AgregarP","/Crear","/Detalle","/Modificar","/Publicar","/Psuscriptor").hasRole("publicador")
                .antMatchers("/Suscriptor","/Consulta").hasRole("suscriptor")
                .and()
		.formLogin().loginPage("/login").permitAll()
                .failureUrl("/login")
		.usernameParameter("username").passwordParameter("password")
		.successHandler(ra)
                .and()
		.logout().logoutSuccessUrl("/login")
		.and()
		.csrf();	
	}
        
}