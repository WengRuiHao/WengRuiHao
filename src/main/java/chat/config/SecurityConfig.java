package chat.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends SecurityConfigurerAdapter {

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		return http.csrf(csrf -> csrf.disable()) // 關閉了 CSRF 保護
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.cors(cors -> cors.configurationSource(corsConfigurationSource())) // 啟用自訂義的cors
				.authorizeHttpRequests(authz -> authz// 設定請求授權規則
						.requestMatchers("/Register", "/Login").permitAll() // 註冊和登錄不需要認證
						.requestMatchers(request -> "websocket".equalsIgnoreCase(request.getHeader("Upgrade")))
						.permitAll()// 只允許帶有 Upgrade: websocket 的請求
						.anyRequest().authenticated()) // 其他請求需要認證器
				.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class) // 配置 JWT 過濾器
				.build();
	}

	@Bean
	public UserDetailsService userDetailsService() {
		// 返回一個空的 UserDetailsService，避免產生預設用戶
		return username -> null;
	}

	// 配置 JWT 認證過濾器
	@Bean
	public JwtAuthenticationFilter jwtAuthenticationFilter() {
		return new JwtAuthenticationFilter();
	}

	// 定義 CORS 配置
	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(List.of("http://localhost:3000")); // 允許來自 localhost:3000 的請求
		configuration.setAllowedMethods(List.of(HttpMethod.OPTIONS.name(), HttpMethod.GET.name(),
				HttpMethod.POST.name(), HttpMethod.PUT.name(), HttpMethod.DELETE.name())); // 允許的 HTTP 方法
		configuration.setAllowedHeaders(List.of("*")); // 允許所有請求頭
		configuration.setAllowCredentials(true); // 允許攜帶憑證
		configuration.setExposedHeaders(List.of("Authorization"));
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration); // 配置所有路徑
		source.registerCorsConfiguration("/ws/**", configuration); // 設定對 /ws 路徑開放 CORS

		return source;
	}
}
