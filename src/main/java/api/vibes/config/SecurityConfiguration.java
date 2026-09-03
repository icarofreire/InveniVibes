package api.vibes.config;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import javax.crypto.SecretKey;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;
import org.springframework.web.filter.CorsFilter;
import api.vibes.security.AuthoritiesConstants;

import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint;
import org.springframework.security.oauth2.server.resource.web.access.BearerTokenAccessDeniedHandler;
import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@EnableConfigurationProperties(JwtAuthenticationProperties.class)
@EnableMethodSecurity(securedEnabled = true)
class SecurityConfiguration {

  private final JwtAuthenticationProperties properties;
  // private final CorsFilter corsFilter;

  public SecurityConfiguration(JwtAuthenticationProperties properties/*, CorsFilter corsFilter*/) {
    this.properties = properties;
    // this.corsFilter = corsFilter;
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    // @formatter:off
    http
      .csrf(AbstractHttpConfigurer::disable)
      // .addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class)
      .headers(headers -> headers
        .contentSecurityPolicy(csp -> csp.policyDirectives(properties.getContentSecurityPolicy()))
        .frameOptions(FrameOptionsConfig::deny)
        .referrerPolicy(referrer -> referrer.policy(ReferrerPolicyHeaderWriter.ReferrerPolicy.STRICT_ORIGIN_WHEN_CROSS_ORIGIN))
        .permissionsPolicyHeader(permissions ->
          permissions.policy("camera=(), fullscreen=(self), geolocation=(), gyroscope=(), magnetometer=(), microphone=(), midi=(), payment=(), sync-xhr=()"))
      )
      .formLogin(AbstractHttpConfigurer::disable)
      .httpBasic(AbstractHttpConfigurer::disable)
      .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
      .authorizeHttpRequests(authz -> authz
        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
        .requestMatchers("/*").permitAll()
        .requestMatchers("/css/**", "/js/**", "/images/**", "/webjars/**", "/static/**").permitAll()
        .requestMatchers("/app/**").permitAll()
        .requestMatchers("/i18n/**").permitAll()
        .requestMatchers("/content/**").permitAll()
        .requestMatchers("/swagger-ui/**").permitAll()
        .requestMatchers("/swagger-ui.html").permitAll()
        .requestMatchers("/v3/api-docs/**").permitAll()
        .requestMatchers("/test/**").permitAll()
        .requestMatchers("/h2-console/**").permitAll() // Allow access to H2 console
        .requestMatchers(PathPatternRequestMatcher.withDefaults().matcher("/api/authenticate")).permitAll()
        .requestMatchers(PathPatternRequestMatcher.withDefaults().matcher("/api/register")).permitAll()
        .requestMatchers(PathPatternRequestMatcher.withDefaults().matcher("/api/activate")).permitAll()
        .requestMatchers(PathPatternRequestMatcher.withDefaults().matcher("/api/admin/unidades-ativas")).permitAll()
        .requestMatchers(PathPatternRequestMatcher.withDefaults().matcher("/api/admin/pontos-proximos")).permitAll()
        .requestMatchers(PathPatternRequestMatcher.withDefaults().matcher("/api/account/reset-password/init")).permitAll()
        .requestMatchers(PathPatternRequestMatcher.withDefaults().matcher("/api/account/reset-password/finish")).permitAll()
        .requestMatchers(PathPatternRequestMatcher.withDefaults().matcher("/api/admin/**")).authenticated()//.hasAuthority(AuthoritiesConstants.ADMIN)
        .requestMatchers(PathPatternRequestMatcher.withDefaults().matcher("/api/**")).authenticated()
        .requestMatchers(PathPatternRequestMatcher.withDefaults().matcher("/management/health")).permitAll()
        .requestMatchers(PathPatternRequestMatcher.withDefaults().matcher("/management/health/**")).permitAll()
        .requestMatchers(PathPatternRequestMatcher.withDefaults().matcher("/management/info")).permitAll()
        .requestMatchers(PathPatternRequestMatcher.withDefaults().matcher("/management/prometheus")).permitAll()
        .requestMatchers(PathPatternRequestMatcher.withDefaults().matcher("/management/**")).hasAuthority(AuthoritiesConstants.ADMIN)
        .anyRequest().authenticated()
      )
      .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
      .exceptionHandling(exceptions ->
          exceptions
              .authenticationEntryPoint(new BearerTokenAuthenticationEntryPoint())
              .accessDeniedHandler(new BearerTokenAccessDeniedHandler())
      )
      .oauth2ResourceServer(oauth2 -> oauth2.jwt(withDefaults()));

      return http.build();
    // @formatter:on
  }

  private SecretKey signingKey() {
    return Keys.hmacShaKeyFor(properties.getJwtBase64Secret().getBytes(StandardCharsets.UTF_8));
  }
}
