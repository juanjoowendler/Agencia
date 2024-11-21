package com.agencia.apigateway.security;

import lombok.var;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {

    @Bean
    public JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withJwkSetUri("https://labsys.frc.utn.edu.ar/aim/realms/backend-tps/protocol/openid-connect/certs").build();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        // ** Endpoints accesibles por EMPLEADO **
                        .requestMatchers("/pruebas/status").hasAuthority("ROLE_EMPLEADO")
                        .requestMatchers("/pruebas/listado").hasAuthority("ROLE_EMPLEADO")
                        .requestMatchers("/pruebas/en-curso").hasAuthority("ROLE_EMPLEADO")
                        .requestMatchers("/pruebas/{id}/finalizar").hasAuthority("ROLE_EMPLEADO")
                        .requestMatchers("/vehiculos/configuracion").hasAuthority("ROLE_EMPLEADO")
                        .requestMatchers("/vehiculos/status").hasAuthority("ROLE_EMPLEADO")
                        .requestMatchers(HttpMethod.POST, "/pruebas").hasAuthority("ROLE_EMPLEADO")
                        .requestMatchers(HttpMethod.POST, "/pruebas/notificar").hasAuthority("ROLE_EMPLEADO")

                        // ** Endpoints accesibles por VEHICULO **
                        .requestMatchers(HttpMethod.POST, "/vehiculos/evaluar").hasAuthority("ROLE_VEHICULO")

                        // ** Endpoints accesibles por ADMIN **
                        .requestMatchers("/pruebas/reporte/incidentes").hasAuthority("ROLE_ADMIN")
                        .requestMatchers("/pruebas/reporte/incidentes/**").hasAuthority("ROLE_ADMIN")
                        .requestMatchers("/pruebas/reporte/km-vehiculo/**").hasAuthority("ROLE_ADMIN")
                        .requestMatchers("/pruebas/reporte/detalle-vehiculo/**").hasAuthority("ROLE_ADMIN")

                        // ** Endpoints accesibles por cualquier usuario autenticado **
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt ->
                        jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
                ); // Configura la validación JWT para los endpoints protegidos
        return http.build();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        var jwtAuthenticationConverter = new JwtAuthenticationConverter();
        var grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();

        // Configura el prefijo para los roles (según cómo se define en tu JWT)
        grantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");
        grantedAuthoritiesConverter.setAuthoritiesClaimName("authorities"); // El nombre del campo en el JWT con los roles

        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }
}
