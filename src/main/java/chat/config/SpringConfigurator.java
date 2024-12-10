package chat.config;

import jakarta.websocket.server.ServerEndpointConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SpringConfigurator extends ServerEndpointConfig.Configurator {
	// WebSocket 的端點交由 Spring 容器管理。
	@Autowired
	private SpringContext springContext;

	@Override
	public <T> T getEndpointInstance(Class<T> endpointClass) throws InstantiationException {
		// 使用 Spring 前後文來實例化 WebSocket 端點
		return springContext.getBean(endpointClass);
	}
}
