package chat.config;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import chat.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		// 從請求頭中擷取 JWT
		String jwt = request.getHeader("Authorization");
		if (jwt != null && !jwt.isEmpty()) {
			// 假設 JWT 驗證成功並解析出用戶名
			String username = JwtUtil.getUsernameFromToken(jwt); // 這裡可以加入 JWT 驗證邏輯，解析出用戶名
			UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(username, null,
					null);

			// 將認證訊息設定到 SecurityContext 中
			SecurityContextHolder.getContext().setAuthentication(authentication);
			// System.out.println(authentication);
		}

		// 繼續處理其他過濾器鏈
		filterChain.doFilter(request, response);
	}

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
		return "websocket".equalsIgnoreCase(request.getHeader("Upgrade"));
	}
}
