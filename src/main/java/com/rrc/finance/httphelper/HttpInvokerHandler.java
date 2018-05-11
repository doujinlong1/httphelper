package com.rrc.finance.httphelper;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rrc.finance.httphelper.annos.FormParam;
import com.rrc.finance.httphelper.annos.GetParam;
import com.rrc.finance.httphelper.annos.HttpRequestHelper;
import com.rrc.finance.httphelper.annos.PathParam;
import com.rrc.finance.httphelper.annos.PostParam;
import com.rrc.finance.httphelper.enums.RequestMethod;
import com.rrc.finance.httphelper.utils.HttpUtil;
import com.rrc.finance.httphelper.utils.JsonUtil;
import com.rrc.finance.httphelper.utils.JwtUtil;

/**
 * 代理类，实现了InvocationHandler接口，java自己的动态代理，该类完成了http的请求
 * 
 * @author doujinlong
 *
 */
public class HttpInvokerHandler implements InvocationHandler {

	private static final Logger logger = LoggerFactory.getLogger(HttpInvokerHandler.class);

	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

		HttpRequestHelper httpRequestHelperAnno = method.getAnnotation(HttpRequestHelper.class);
		if (httpRequestHelperAnno == null) {
			logger.error("httpRequestHelperAnno = null, can not invoke http request");
			return null;
		}
		String res = "";
		// ==================获取http请求的所有参数 start==================
		try {
			String url = getRealUrl(method, args);
			RequestMethod httpMethod = httpRequestHelperAnno.method();
			String postParam = getPostParam(method, args);
			Map<String, Object> formParam = getFormParam(method, args);
			int readTimeout = httpRequestHelperAnno.readTimeout();
			int connectTimeout = httpRequestHelperAnno.connectTimeout();
			String contentType = httpRequestHelperAnno.contentType();
			res = HttpUtil.request(url, httpMethod, postParam, formParam, readTimeout, connectTimeout, contentType);
		} catch (Exception e) {
			logger.error("method = {} http assemble param error", method.getName());
			logger.error("http assemble param error,exception = ", e);
			return null;
		}
		// ==================获取http请求的所有参数 end==================

		try {
			Class<?> clz = method.getReturnType();
			if (clz.isPrimitive() && clz.getName().equals("void")) {
				logger.info("method returnType = void,return null");
				return null;
			}
			return clz.equals(String.class) ? res : JsonUtil.parseFromJson(res, clz);
		} catch (Exception e) {
			logger.error("method = {} http resolve return error", method.getName());
			logger.error("http resolve return error,exception = ", e);
		}
		return null;
	}

	private Map<String, Object> getFormParam(Method method, Object[] args) {
		Parameter[] parameters = method.getParameters();
		for (int i = 0; i < parameters.length; i++) {
			Parameter p = parameters[i];
			if (null != p.getAnnotation(FormParam.class) && args[i] != null) {
				return ((Map<String, Object>) args[i]);
			}
		}
		return new HashMap<String, Object>();
	}

	private static String getPostParam(Method method, Object[] args) {
		Parameter[] parameters = method.getParameters();
		for (int i = 0; i < parameters.length; i++) {
			Parameter p = parameters[i];
			if (null != p.getAnnotation(PostParam.class) && args[i] != null) {
				return ((String) args[i]);
			}
		}
		return "{}";
	}

	private String getRealUrl(Method method, Object[] args) {
		HttpRequestHelper httpRequestHelperAnno = method.getAnnotation(HttpRequestHelper.class);
		Parameter[] parameters = method.getParameters();
		Map<String, Object> getParamMap = new HashMap<String, Object>();
		String[] PathParam = null;
		for (int i = 0; i < parameters.length; i++) {
			Parameter p = parameters[i];
			if (null != p.getAnnotation(PathParam.class) && args[i] != null) {
				PathParam = (String[]) args[i];
			} else if (null != p.getAnnotation(GetParam.class) && args[i] != null) {
				getParamMap.putAll((Map<String, Object>) args[i]);
			}
		}
		String url = httpRequestHelperAnno.url();
		String octoKey = httpRequestHelperAnno.octoKey();
		String octoSecret = httpRequestHelperAnno.octoSecret();
		if (isNotEmpty(octoKey) && isNotEmpty(octoSecret)) {
			String octo = JwtUtil.generateJwtToken(octoKey, octoSecret);
			getParamMap.put("_octo", octo);
		}
		if (PathParam !=null) {
			url = String.format(url, PathParam);
		}
		return MapArgs2UrlArgs(url,getParamMap);
	}

	private boolean isNotEmpty(String octoKey) {
		return octoKey != null && octoKey.length() > 0;
	}

	private static String MapArgs2UrlArgs(String url, Map<String, Object> param) {
		String ret = url.contains("?")?url:url+"?";
		for (String key : param.keySet()) {
			ret += ("&"+key + "=" + param.get(key));
		}
		return ret;
	}
}
