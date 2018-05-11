package com.rrc.finance.httphelper.annos;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Import;

import com.rrc.finance.httphelper.config.ProxyGeneratorConfig;
/**
 * spring项目直接加载注解，借助于@Import注解完成加载类
 * @author doujinlong
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import({ProxyGeneratorConfig.class})
public @interface EnableHttpHelper {

	/**
	 * 要加载的类所在的包名
	 * @return
	 */
	String[] basePackages();
	
	
}

