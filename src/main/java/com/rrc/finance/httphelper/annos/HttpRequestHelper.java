package com.rrc.finance.httphelper.annos;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.rrc.finance.httphelper.enums.RequestMethod;
/**
 * 标注接口方法的注解，url,method,不能为空，与 @GetParam @PathParam @PostParam注解合作即可完成url的拼装
 * @author doujinlong
 *
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface HttpRequestHelper {

	String url();

	RequestMethod method();
	
	String octoKey() default "";
	
	String octoSecret() default "";
	
	int readTimeout() default 3000;
	
	int connectTimeout() default 3000;
	
	String contentType() default "application/json";
	
	
	
}
