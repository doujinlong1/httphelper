package com.rrc.finance.httphelper.utils;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.kevinsawicki.http.HttpRequest;
import com.rrc.finance.httphelper.enums.RequestMethod;
/**
 * http请求工具类
 * @author doujinlong
 *
 */
public class HttpUtil {

	private static final Logger logger = LoggerFactory.getLogger(HttpUtil.class);
	
	private static final int READ_TIMEOUT = 3000;

	private static final int CONNECT_TIMEOUT = 3000;

	private static final int HTTP_STATUS_OK = 200;
	
	private static final int HTTP_STATUS_CREATED = 201;

	private static final String CONTENT_TYPE_JSON = "application/json";
	
	private static final String CONTENT_TYPE_FORM = "application/x-www-form-urlencoded";
	
	public static String request(String url,RequestMethod method,String postParam,Map formParam){
		return request(url, method, postParam, formParam,READ_TIMEOUT, CONNECT_TIMEOUT,CONTENT_TYPE_JSON);
	}


	public static String request(String url,RequestMethod method,String postParam,Map<?, ?> formParam,int readTimeout,int connectTimeout
			,String contentType){
		logger.info("http request url ={},method = {}, param={}",url,method,postParam);
		try {
			HttpRequest httpRequest = getHttpRequest(url, method, postParam,formParam,readTimeout, connectTimeout,contentType);
			String str = httpRequest.body();
			int code = httpRequest.code();
			logger.info("http response url ={},method = {}, param={},code={},response={}",url,method,postParam,code,str);
			if (code == HTTP_STATUS_OK || code == HTTP_STATUS_CREATED) {
				return str;
			}
			return null;
		} catch (Exception e) {
			logger.error("http request error,exception = ",e);
			return null;
		}
	}

	private static HttpRequest getHttpRequest(String url, RequestMethod method, String postParam,Map<?, ?> formParam,int readTimeout,
			int connectTimeout,String contentType) {
		System.out.println(method.name());
		HttpRequest httpRequest = new HttpRequest(url,method.name())
				.readTimeout(readTimeout)
				.connectTimeout(connectTimeout)
				.contentType(contentType);
		if (method.equals(RequestMethod.POST)&&contentType.equals(CONTENT_TYPE_FORM)) {
			return httpRequest.form(formParam);
		}/*else if (method.equals(RequestMethod.POST)&&contentType.equals(CONTENT_TYPE_JSON)){
			return httpRequest.send(postParam);
		}*/else if(method.equals(RequestMethod.POST)){
			/**
			 * 否则默认走json格式
			 */
			return httpRequest.send(postParam);
		}
		return httpRequest;
	}
	
}
