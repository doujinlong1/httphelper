package com.rrc.finance.httphelper.utils;

import java.sql.Timestamp;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * octo工具类
 * @author doujinlong
 *
 */
public class JwtUtil {

	public static String generateJwtToken(String octoKey,String octoSecret){
		Date beginDate = new Date();
		Timestamp endDate = new Timestamp(System.currentTimeMillis() + 3*60*1000);
		HashMap<String,Object> map = new HashMap<String,Object>();
		map.put("typ", "JWT");
		String OctoPrivilegeSecretBase64 = Base64.getEncoder().encodeToString(octoSecret.getBytes());
		return Jwts.builder().setHeader(map).setIssuer(octoKey).setIssuedAt(beginDate).setExpiration(endDate).signWith(SignatureAlgorithm.HS256, OctoPrivilegeSecretBase64).compact();
	}
}
