/**
 * Copyright (c) 2012-2013, Michael Yang 杨福海 (www.yangfuhai.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.tsz.afinal.http;

import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.protocol.HttpContext;

import com.mfh.comna.comn.logic.IPublishProgressAble;

import android.os.SystemClock;

import net.tsz.afinal.http.entityhandler.EntityCallBack;
import net.tsz.afinal.http.entityhandler.FileEntityHandler;
import net.tsz.afinal.http.entityhandler.StringEntityHandler;

/**
 * 同步http请求处理
 * 
 * @author zhangyz created on 2013-5-15
 * @since Framework 1.0
 */
public class SyncRequestHandler implements EntityCallBack {
	private final AbstractHttpClient client;
	private final HttpContext context;    
    private final StringEntityHandler mStrEntityHandler = new StringEntityHandler();
    private final FileEntityHandler mFileEntityHandler = new FileEntityHandler();

	private int executionCount = 0;
	private String charset;
    private long time;
	
    private String targetUrl = null; //下载的路径
    private boolean isResume = false; //是否断点续传
    
    private IPublishProgressAble<Object> procCallback;//用于回调通知目前的进度

	public SyncRequestHandler(AbstractHttpClient client, HttpContext context, String charset) {
		this.client = client;
		this.context = context;
		this.charset = charset;
	}
	
	public SyncRequestHandler(AbstractHttpClient client, HttpContext context, 
	        String charset, IPublishProgressAble<Object> procCallback) {
        this.client = client;
        this.context = context;
        this.charset = charset;
        this.procCallback = procCallback;
    }

	/**
	 * 执行http请求
	 * @param params 第一个参数是HttpUriRequest,后面两个参数用于支持断点续传
	 * @return
	 * @author zhangyz created on 2013-5-15
	 */
    public Object sendRequest (Object... params) {
        if(params!=null && params.length == 3){
            targetUrl = String.valueOf(params[1]);
            isResume = (Boolean) params[2];
        }
        
        try {
            return makeRequestWithRetries((HttpUriRequest)params[0]);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    private Object makeRequestWithRetries(HttpUriRequest request) throws IOException {
        if (isResume && targetUrl != null) {
            File downloadFile = new File(targetUrl);
            long fileLen = 0;
            if (downloadFile.isFile() && downloadFile.exists()) {
                fileLen = downloadFile.length();
            }
            if (fileLen > 0)
                request.setHeader("RANGE", "bytes=" + fileLen + "-");
        }

        boolean retry = true;
        IOException cause = null;
        HttpRequestRetryHandler retryHandler = client.getHttpRequestRetryHandler();
        while (retry) {
            try {
                HttpResponse response = client.execute(request, context);
                return handleResponse(response);
            }
            catch (UnknownHostException e) {
                cause = new IOException("unknownHostException：can't resolve host" + e.getMessage());
                break;
            }
            catch (IOException e) {
                cause = e;
                retry = retryHandler.retryRequest(cause, ++executionCount, context);
            }
            catch (NullPointerException e) {
                // HttpClient 4.0.x 之前的一个bug
                // http://code.google.com/p/android/issues/detail?id=5255
                cause = new IOException("NPE in HttpClient" + e.getMessage());
                retry = retryHandler.retryRequest(cause, ++executionCount, context);
            }
            catch (Exception e) {
                cause = new IOException("Exception" + e.getMessage());
                retry = retryHandler.retryRequest(cause, ++executionCount, context);
            }
        }
        if (cause != null)
            throw cause;
        else
            throw new IOException("未知网络错误");
    }

    private Object handleResponse(HttpResponse response) throws Exception {
        StatusLine status = response.getStatusLine();
        if (status.getStatusCode() >= 300) {
            String errorMsg = "response status error code:" + status.getStatusCode();
            if (status.getStatusCode() == 416 && isResume) {
                errorMsg += " \n maybe you have download complete.";
            }
            throw new HttpResponseException(status.getStatusCode(), status.getReasonPhrase() + "-" + errorMsg);
        }
        else {
            HttpEntity entity = response.getEntity();
            Object responseBody = null;
            if (entity != null) {
                time = SystemClock.uptimeMillis();
                if (targetUrl != null) {
                    responseBody = mFileEntityHandler.handleEntity(entity, this, targetUrl, isResume);
                }
                else {
                    responseBody = mStrEntityHandler.handleEntity(entity, this, charset);
                }
            }
            return responseBody;

        }
    }
    
	@Override
    public void callBack(long count, long current, boolean mustNoticeUI) {
        if (procCallback != null && procCallback.isProgress()) {
            if (mustNoticeUI) {
                procCallback.publishProgressByService(count, current);
            }
            else {
                long thisTime = SystemClock.uptimeMillis();
                if (thisTime - time >= procCallback.getRate()) {
                    time = thisTime;
                    procCallback.publishProgressByService(count, current);
                }
            }
        }
    }	
}
