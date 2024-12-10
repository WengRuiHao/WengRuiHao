package chat.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class SpringContext {
	//注入到靜態變量中，使得我們可以在非 Spring 管理的對象中方便地獲取 Spring 的 Bean。
	private static ApplicationContext context;

	@Autowired
	public void setApplicationContext(ApplicationContext applicationContext) {
		context = applicationContext;
	}

	public static <T> T getBean(Class<T> beanClass) {
		return context.getBean(beanClass);
	}
}