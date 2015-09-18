package com.mfh.comna.bizz;

import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

public class HttpHelper {

	private static DefaultHttpClient httpClient = null;
	private static final int HTTP_REQUEST_TIMEOUT_MS = 20 * 1000;
	private static final int IO_BUFFER_SIZE = 8 * 1024;

    public static final String UTF_8 = "UTF-8";


	private static HttpClient getHttpClient() throws Exception {
		if (httpClient != null) {
			return httpClient;
		}
		/*HttpParams params = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(params,
				HTTP_REQUEST_TIMEOUT_MS);
		HttpConnectionParams.setSoTimeout(params, HTTP_REQUEST_TIMEOUT_MS);
		HttpConnectionParams.setSocketBufferSize(params, IO_BUFFER_SIZE);

		HttpClientParams.setRedirecting(params, true);
		httpClient = new DefaultHttpClient(params);
		return httpClient;*/
		BasicHttpParams basicHttpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(basicHttpParams, HTTP_REQUEST_TIMEOUT_MS);
		HttpConnectionParams.setSoTimeout(basicHttpParams, HTTP_REQUEST_TIMEOUT_MS);
		ConnManagerParams.setTimeout(basicHttpParams, HTTP_REQUEST_TIMEOUT_MS);
		SchemeRegistry localSchemeRegistry = new SchemeRegistry();
		localSchemeRegistry.register(new Scheme("http", PlainSocketFactory
				.getSocketFactory(), 80));
		localSchemeRegistry.register(new Scheme("https", PlainSocketFactory
				.getSocketFactory(), 443));
		httpClient = new DefaultHttpClient(new ThreadSafeClientConnManager(
				basicHttpParams, localSchemeRegistry),
				basicHttpParams);
		return httpClient;
	}

	public static String doPost(Map<String,Object> jsonInfo,String url) throws Exception {
		
	     HttpClient client = null;
		 try {
			  client = getHttpClient();
		 } catch (Exception e1) {
			  e1.printStackTrace();
			  return "";
		 }
	    try{  
	         // 创建HttpPost对象。
	         HttpPost httpRequest = new HttpPost(url); 
	         System.out.println("登录====="+url);
	         // 如果传递参数个数比较多的话可以对传递的参数进行封装   
	         List<NameValuePair> params = new ArrayList<NameValuePair>();  
	         for(String key : jsonInfo.keySet()){  
	               //封装请求参数   
	               params.add(new BasicNameValuePair(key , (String) jsonInfo.get(key)));  
	          }  
	          // 设置请求参数   
	         httpRequest.setEntity(new UrlEncodedFormEntity(  
	                params,HTTP.UTF_8));
	         System.out.println("登录参数====="+params);
	          // 发送POST请求   
	          HttpResponse httpResponse = client.execute(httpRequest);  
	      	
	          System.out.println("登录信息====="+httpResponse.getStatusLine().getStatusCode());
	         
	          if (httpResponse.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
					httpRequest.abort();
			   }
			   HttpEntity entity = httpResponse.getEntity();
			   
			   return EntityUtils.toString(entity, "utf-8").trim();
	        }catch (SocketException e) {
				System.out.println(e.getMessage());
				System.out.println("SocketException error");
				e.getStackTrace();
				throw new Exception("aa");
			} catch (Exception e) {
				e.getStackTrace();
				throw new Exception("aa");
			} finally {
				if (client != null) {
					 // client.getConnectionManager().closeExpiredConnections();
					 //client.getConnectionManager().shutdown();
				}
		}
	}




}
