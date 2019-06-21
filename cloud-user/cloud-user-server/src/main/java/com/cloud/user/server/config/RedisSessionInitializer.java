package com.cloud.user.server.config;

import org.springframework.session.web.context.AbstractHttpSessionApplicationInitializer;

public class RedisSessionInitializer extends AbstractHttpSessionApplicationInitializer {

	public RedisSessionInitializer() {
		super(RedisSessionConfig.class);
	}
}