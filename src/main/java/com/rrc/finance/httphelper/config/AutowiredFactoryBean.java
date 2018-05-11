package com.rrc.finance.httphelper.config;

import java.lang.reflect.Proxy;

import org.springframework.beans.factory.FactoryBean;

import com.rrc.finance.httphelper.HttpInvokerHandler;
/**
 * 实现了FactoryBean 接口的类，帮助自动注入所有的http接口
 * @author doujinlong
 *
 * @param <T>
 */
public class AutowiredFactoryBean<T> implements FactoryBean<T> {

	private Class<T> mapperInterface;
	
	@SuppressWarnings("unchecked")
	public T getObject() throws Exception {
		return (T) Proxy.newProxyInstance(ProxyGeneratorConfig.class.getClassLoader(), new Class[]{mapperInterface}, new HttpInvokerHandler());
	}

	public Class<?> getObjectType() {
		// TODO Auto-generated method stub
		return mapperInterface;
	}

	public Class<T> getMapperInterface() {
		return mapperInterface;
	}

	public void setMapperInterface(Class<T> mapperInterface) {
		this.mapperInterface = mapperInterface;
	}

	public boolean isSingleton() {
		return true;
	}
	
}
