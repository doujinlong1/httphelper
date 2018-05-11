package com.rrc.finance.httphelper.annos;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标准http路径参数的注解，强制用在String数组上， 如 url=http://127.0.0.1:8080/%s/%s。
 * @author doujinlong
 *
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PathParam {
}
