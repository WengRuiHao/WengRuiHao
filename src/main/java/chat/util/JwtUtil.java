package chat.util;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {
	
	private static final long EXPIRATION_TIME = 864_000_000; // 10 days in milliseconds
	private static final SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256);// 將字串金鑰轉換為 SecretKey

	//生成JWT
	public static String generateToken(String username) {
		return Jwts.builder().setSubject(username).setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
				.signWith(key, SignatureAlgorithm.HS256).compact();
	}

	//解析JWT
	public static Claims parseToken(String token) {
		if (token != null && token.startsWith("Bearer ")) {
	        token = token.substring(7); // 移除 "Bearer " 前綴
	    }
		
		return Jwts.parserBuilder().setSigningKey(key) // 傳入 SecretKey
				.build() // 建立 JwtParser
				.parseClaimsJws(token)
				.getBody();
	}

	// 從 Token 中提取用戶名
	public static String getUsernameFromToken(String token) {
		return parseToken(token).getSubject();
	}

	// 驗證 Token 是否過期
	public static boolean isTokenExpired(String token) {
		return parseToken(token).getExpiration().before(new Date());
	}

	// 驗證 Token
	public static boolean validateToken(String token) {
		try {
			parseToken(token); // 能成功解析即為有效
			return true;
		} catch (Exception e) {
			return false; // 捕捉任何解析錯誤
		}
	}
}
