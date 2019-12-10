package com.jt.config;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
/**
 * redis的配置类
 * @author wan_ys
 *
 */
import org.springframework.context.annotation.PropertySource;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;

@PropertySource("classpath:/properties/redis.properties")
@Configuration
public class RedisConfig {
	@Value("${redis.host}")
	private String host;
	@Value("${redis.port}")
	private Integer port;

	@Bean
	public Jedis jedis() {
		return new Jedis(host, port);
	}

	// redis.nodes=192.168.175.129:7000,192.168.175.129:7001,192.168.175.129:7002,192.168.175.129:7003,192.168.175.129:7004,192.168.175.129:7005
	@Value("${redis.nodes}")
	private String redisNodes;

	@Bean
	public JedisCluster jedisCluster() {
		Set<HostAndPort> nodes = new HashSet<>();
		// 1.根据,号拆分为多个node
		String[] strNode = redisNodes.split(",");
		// IP:端口
		for (String node : strNode) {
			String host = node.split(":")[0];
			int port = Integer.parseInt(node.split(":")[1]);
			HostAndPort hostAndPort = new HostAndPort(host, port);
			nodes.add(hostAndPort);
		}

		return new JedisCluster(nodes);
	}

}
