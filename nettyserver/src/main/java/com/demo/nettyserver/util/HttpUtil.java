package com.demo.nettyserver.util;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import javax.net.ssl.SSLException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class HttpUtil {

	private static final Logger logger = Logger.getLogger(HttpUtil.class);
	private final static int socketTimeout = 50000;
	private final static int connectionTimeout = 10000;
	private static CloseableHttpClient httpClient;
	private static final String UTF8 = "UTF-8";

	static {

		HttpRequestRetryHandler retryHandler = new HttpRequestRetryHandler() {
			public boolean retryRequest(IOException exception,
					int executionCount, HttpContext context) {
				if (executionCount >= 4) {
					// Do not retry if over max retry count
					return false;
				}
				if (exception instanceof InterruptedIOException) {
					// Timeout
					return false;
				}
				if (exception instanceof UnknownHostException) {
					// Unknown host
					return false;
				}
				if (exception instanceof SSLException) {
					// SSL handshake exception
					return false;
				}

				HttpClientContext clientContext = HttpClientContext
						.adapt(context);
				HttpRequest request = clientContext.getRequest();
				boolean idempotent = !(request instanceof HttpEntityEnclosingRequest);
				if (idempotent) {
					// Retry if the request is considered idempotent
					return true;
				}
				return false;
			}
		};
		PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();

		// cm.setMaxTotal(100);
		// cm.setDefaultMaxPerRoute(100);
		// HttpHost proxy = new HttpHost("127.0.0.1", 8989, "http");
		RequestConfig requestConfig = RequestConfig.custom()
				.setConnectTimeout(connectionTimeout)
				.setSocketTimeout(socketTimeout).build();
		httpClient = HttpClientBuilder.create().setConnectionManager(cm)
				.setRetryHandler(retryHandler)
				.setDefaultRequestConfig(requestConfig).build();

	}
	
	/**
	 * HttpClient Get 请求数据
	 * @param url
	 * @return
	 */
	public static String get(String url) {

		String result = null;
		// 创建httpget.
		HttpGet httpget = null;
		// 执行get请求.
		CloseableHttpResponse response = null;
		int status = 0;
		long startTime = System.currentTimeMillis();
		try {
			httpget = new HttpGet(url);
			response = httpClient.execute(httpget);
			// 获取响应实体
			HttpEntity entity = response.getEntity();
			status = response.getStatusLine().getStatusCode();
			// 打印响应状态
			logger.info("HttpUtilGet " + url + " responsecode:" + status);
			if (entity != null && status == 200) {
				// 打印响应内容
				result = EntityUtils.toString(entity);
				EntityUtils.consume(entity);
			} else {
				EntityUtils.consume(entity);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
		}

		long endTime = System.currentTimeMillis();
		logger.info(url + result + "costTime" + (endTime - startTime));
		return result;
	}
	
	/**
	 * HttpClient Get 请求数据
	 * @param url
	 * @param headers
	 * @return
	 */
	public static String get(String url, Map<String, String> headers) {

		String result = null;
		// 创建httpget.
		HttpGet httpget = null;
		// 执行get请求.
		CloseableHttpResponse response = null;

		int status = 0;
		long startTime = System.currentTimeMillis();
		try {
			httpget = new HttpGet(url);
			if (headers != null) {
				for (Entry<String, String> e : headers.entrySet()) {
					httpget.addHeader(e.getKey(), e.getValue());
				}
			}
			response = httpClient.execute(httpget);
			// 获取响应实体
			HttpEntity entity = response.getEntity();
			status = response.getStatusLine().getStatusCode();
			// 打印响应状态
			logger.info("HttpUtilGet " + url + " responsecode:" + status);
			if (entity != null && status == 200) {
				// 打印响应内容
				result = EntityUtils.toString(entity);
				EntityUtils.consume(entity);
			} else {
				EntityUtils.consume(entity);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
		}

		long endTime = System.currentTimeMillis();
		logger.info(url + result + "costTime" + (endTime - startTime));
		return result;
	}
	
	/**
	 * HttpClient Post 提交数据
	 * @param url
	 * @param params
	 * @param postJson
	 * @param headers
	 * @param reqEncode
	 * @param resEncode
	 * @return
	 */
	public static String post(String url, Map<String, Object> params,
			boolean postJson, Map<String, String> headers, String reqEncode,
			String resEncode) {
		HttpPost post = new HttpPost(url);
		if (StringUtils.isBlank(reqEncode)) {
			reqEncode = UTF8;
		}
		if (StringUtils.isBlank(resEncode)) {
			resEncode = UTF8;
		}
		StringBuilder headerString = new StringBuilder();
		String separator = "";
		if (headers != null) {
			for (Entry<String, String> e : headers.entrySet()) {
				post.addHeader(e.getKey(), e.getValue());
				headerString.append(separator).append(e.getKey()).append(":")
						.append(e.getValue());
				separator = "|";
			}
		}
		StringBuilder postData = new StringBuilder();
		if (params != null && !params.isEmpty()) {
			if (postJson) {
				String body = JsonUtils.toJson(params);
				// 记录需要发送的数据
				postData.append(body);
				BasicHttpEntity requestBody = new BasicHttpEntity();
				byte[] jsonBytes = null;
				try {
					jsonBytes = body.getBytes("UTF-8");
				} catch (UnsupportedEncodingException impossiable) {
					// should not happen
					throw new RuntimeException("UTF-8 is not surportted",
							impossiable);
				}
				requestBody.setContent(new ByteArrayInputStream(jsonBytes));
				requestBody.setContentLength(jsonBytes.length);
				post.setEntity(requestBody);
			} else {
				List httpParams = new ArrayList(params.size());
				for (Entry<String, Object> entry : params.entrySet()) {
					String k = entry.getKey();
					Object v = entry.getValue();
					if (v == null) {
						httpParams.add(new BasicNameValuePair(k, null));
						// 记录需要发送的数据
						postData.append(k).append("=&");
					} else if (!v.getClass().isArray()) {
						httpParams.add(new BasicNameValuePair(k, v.toString()));
						// 记录需要发送的数据
						postData.append(k).append("=").append(v.toString())
								.append("&");
					} else {// 数组要作特殊处理
						int len = Array.getLength(v);
						for (int i = 0; i < len; i++) {
							Object element = Array.get(v, i);
							if (element != null) {
								httpParams.add(new BasicNameValuePair(k,
										element.toString()));
								// 记录需要发送的数据
								postData.append(k).append("=")
										.append(element.toString()).append("&");
							} else {
								httpParams.add(new BasicNameValuePair(k, null));
								// 记录需要发送的数据
								postData.append(k).append("=&");
							}
						}
					}
				}
				if (postData.length() > 0) {
					// 去掉最后一个&号
					postData.deleteCharAt(postData.length() - 1);
				}
				try {
					post.setEntity(new UrlEncodedFormEntity(httpParams,
							reqEncode));
				} catch (UnsupportedEncodingException impossiable) {
					// shouldn't happen
					throw new RuntimeException("UTF-8 is not surportted",
							impossiable);
				}
			}
		}

		logger.debug("RemoteStart:"
				+ JsonUtils.toJson(new Object[] { "POST", url, headerString,
						postData.toString() }));
		logger.debug("RemoteStartHeaders:" + JsonUtils.toJson(headers));
		logger.debug("RemoteStartParams:" + JsonUtils.toJson(params));
		long start = System.currentTimeMillis();
		String response = null;
		try {

			HttpClient httpClient1 = HttpClients.createDefault();
			HttpResponse resp = httpClient1.execute(post);
			long end = System.currentTimeMillis();
			long time = (end - start);

			HttpEntity entity = resp.getEntity();
			response = EntityUtils.toString(entity, resEncode);

			logger.debug("RemoteEnd: "
					+ JsonUtils.toJson(new Object[] {
							resp.getStatusLine().getStatusCode(), response,
							time }));
			logger.debug("RemoteEndHeader:"
					+ JsonUtils.toJson(resp.getAllHeaders()));
		} catch (Exception e) {
			logger.error("RemoteEnd (With Exception)||uri=" + url, e);
		}
		return response;
	}

	// public static void main(String[] args) {
	// long startTime = System.currentTimeMillis();
	//
	// HttpUtil.get("http://tst00.heqi.xin/loanmgmt/spread/status");
	//
	// System.out.println("costTime:" + (System.currentTimeMillis() - startTime)
	// / 1000);
	// }

}
