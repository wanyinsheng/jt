package com.jt;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * (exclude = DataSourceAutoConfiguration.class) 启动时不加载数据源
 * 
 * @author 000
 *
 */
/**
 * 分析:如果能够获取url值, 这个值就是页面跳转名称 思路: 获取url中指定参数
 * 
 * restFul: 1.要求参数必须使用/分割 2.参数位置必须固定 3.接收参数时必须使用{}标识参数. 使用特定的注解 @PathVariable
 * 并且名称最好一致
 */
//根据用户请求跳转页面
//@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@SpringBootApplication
@MapperScan("com.jt.mapper")
public class SpringBootRun {
	public static void main(String[] args) {
		SpringApplication.run(SpringBootRun.class, args);
	}

}
