package com.demo.nettyserver.util;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Map;
import java.util.TreeMap;

/**
 * 签名工具
 */
public class SignUtils {
	// 签名name
	public static final String SIGN_NAME = "sign";
	// 签名算法HmacSha256
	private static final String HMAC_SHA256 = "HmacSHA256";
	// 编码UTF-8
	private static final String ENCODING = "UTF-8";
	// 换行符
	private static final String LF = "\n";
	// 参与签名的系统Header前缀,只有指定前缀的Header才会参与到签名中
	private static final String CA_HEADER_TO_SIGN_PREFIX_SYSTEM = "X-Ca-";
	// 请求时间戳
	public static final String X_CA_TIMESTAMP = "X-Ca-Timestamp";
	// 请求放重放Nonce,15分钟内保持唯一,建议使用UUID
	public static final String X_CA_NONCE = "X-Ca-Nonce";
	public final static Logger logger = Logger.getLogger(SignUtils.class);

	/**
	 * 计算签名
	 *
	 * @param method
	 *            GET/POST
	 * @param url
	 *            Path + Query
	 * @param headers
	 *            Http头
	 * @param formParamMap
	 *            POST表单参数
	 * @param secret
	 *            APP密钥
	 * 
	 * @return 签名后的字符串
	 */
	public static String sign(String method, String url, Map<String, String> headers, Map<String, String> formParamMap,
			String secret) {

		try {
			Mac hmacSha256 = Mac.getInstance(HMAC_SHA256);
			byte[] keyBytes = secret.getBytes(ENCODING);
			hmacSha256.init(new SecretKeySpec(keyBytes, 0, keyBytes.length, HMAC_SHA256));
			String toSign = buildStringToSign(headers, url, formParamMap, method);
			return Base64.encodeBase64URLSafeString(hmacSha256.doFinal(toSign.getBytes(ENCODING)));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 构建待签名字符串
	 *
	 * @param headers
	 *            Http头
	 * @param url
	 *            Path+Query
	 * @param formParamMap
	 *            POST表单参数
	 * @param method
	 * 
	 * @return 签名字符串
	 */
	private static String buildStringToSign(Map<String, String> headers, String url, Map<String, String> formParamMap,
			String method) {
		StringBuilder sb = new StringBuilder();
		sb.append(method.toUpperCase()).append(LF);
		sb.append(buildHeaders(headers));
		sb.append(buildResource(url, formParamMap));
		return sb.toString();
	}

	/**
	 * 构建待签名Path+Query+FormParams
	 *
	 * @param url
	 *            Path+Query
	 * @param formParamMap
	 *            POST表单参数
	 * @return 待签名Path+Query+FormParams
	 */
	private static String buildResource(String url, Map<String, String> formParamMap) {
		Map<String, String> sortMap = new TreeMap<String, String>();
		if (url.indexOf("?") > -1) {
			String[] arr = url.split("\\?");
			url = arr[0];
			String queryString = arr[1];
			if (queryString != null && queryString.length() > 0) {
				for (String query : queryString.split("\\&")) {
					arr = query.split("\\=");
					String key = arr[0];
					String value = arr.length >= 2 ? arr[1] : "";
					if (sortMap.get(key) == null) {
						sortMap.put(key, value);
					}
				}
			}
		}
		if (formParamMap != null) {
			sortMap.putAll(formParamMap);
		}
		sortMap.remove(SIGN_NAME);
		if (sortMap.size() > 0) {
			StringBuilder sb = new StringBuilder();
			for (Map.Entry<String, String> e : sortMap.entrySet()) {
				String key = e.getKey();
				String val = e.getValue();
				if (val == null || val.length() == 0) {
					sb.append('&').append(key);
				} else {
					sb.append('&').append(key).append("=").append(val);
				}
			}
			url += "?" + sb.substring(1);
		}
		return url;
	}

	/**
	 * 构建待签名Http头
	 *
	 * @param headers
	 *            请求中所有的Http头
	 * 
	 * @return 待签名Http头
	 */
	private static String buildHeaders(Map<String, String> headers) {
		Map<String, String> headersToSign = new TreeMap<String, String>();
		if (headers != null) {
			for (Map.Entry<String, String> header : headers.entrySet()) {
				if (isHeaderToSign(header.getKey())) {
					headersToSign.put(header.getKey(), header.getValue());
				}
			}
		}
		StringBuilder sb = new StringBuilder();
		for (Map.Entry<String, String> e : headersToSign.entrySet()) {
			sb.append(e.getKey()).append(':').append(e.getValue()).append(LF);
		}
		return sb.toString();
	}

	/**
	 * Http头是否参与签名 return
	 */
	private static boolean isHeaderToSign(String headerName) {
		if (headerName == null) {
			return false;
		}
		if (headerName.startsWith(CA_HEADER_TO_SIGN_PREFIX_SYSTEM)) {
			return true;
		}
		return false;
	}
}
