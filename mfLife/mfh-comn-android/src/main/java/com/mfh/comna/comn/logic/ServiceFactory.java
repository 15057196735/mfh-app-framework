package com.mfh.comna.comn.logic;

import android.content.Context;

import com.mfh.comn.code.ICodeService;
import com.mfh.comn.code.impl.CodeService;
import com.mfh.comna.comn.seq.SequenceService;
import com.mfh.comna.comn.seq.SequenceServiceImpl;

import java.util.HashMap;
import java.util.Map;

/**
 * 服务工厂类，全部单例模式创建服务对象
 * 
 * @author zhangyz created on 2013-6-7
 * @since Framework 1.0
 */
public class ServiceFactory {
    @SuppressWarnings("rawtypes")
    private static Map<String, IService> serviceMap = new HashMap<>();
    private static ICodeService codeService = null;  
    private static SequenceService sequenceService = null;
    
    /**
     * 获取指定的服务
     * @param serviceClass service类名
     * @param context 安卓上下文环境
     * @return
     * @author zhangyz created on 2013-6-13
     */
    @SuppressWarnings("rawtypes")
    public static <T extends ComnService> T getService(Class<T> serviceClass, Context... context) {
        String keyName = serviceClass.getName();
        @SuppressWarnings("unchecked")
        T service = (T)serviceMap.get(keyName);
        if (service == null) {
            try {
                service = serviceClass.newInstance();
                serviceMap.put(keyName, service);
            }
            catch (Exception e) {
                throw new RuntimeException("创建服务对象失败:" + e.getMessage(), e);
            }
        }
        if (context != null && context.length > 0)
            service.setContext(context[0]);
        return service;
    }

    /**
     * 判断是否存在指定服务
     * @param serviceName
     * @return
     */
    public static boolean checkService(String serviceName) {
        return serviceMap.containsKey(serviceName);
    }

    /**
     * 获取指定命名的服务对象,若没有则返回null
     * @param serviceName
     * @param <T>
     * @return
     */
    public static <T extends IService> T getService(String serviceName) {
        @SuppressWarnings("unchecked")
        T service = (T)serviceMap.get(serviceName);
        if (service == null) {
            try {
                Class<?> serviceClass = Class.forName(serviceName);
                service = (T)serviceClass.newInstance();
                serviceMap.put(serviceName, service);
            }
            catch(Throwable e) {
                e.printStackTrace();
            }
        }
        return service;
    }

    public static void putService(String serviceName, IService service) {
        serviceMap.put(serviceName, service);
    }
    /**
     * 获取编码实例
     * @return
     * @author zhangyz created on 2013-6-7
     */
    public static ICodeService getCodeService() {
        if (codeService == null) {
            synchronized(CodeService.class) {
                if (codeService == null)
                    codeService = new CodeService();
            }
        }
        return codeService;
    }

    /**
     * 获取序列服务
     * @return
     * @author zhangyz created on 2013-6-7
     */
    public static SequenceService getSequenceService() {
        if (sequenceService == null) {
            synchronized(SequenceService.class) {
                if (sequenceService == null)
                    sequenceService = new SequenceServiceImpl();
            }
        }
        return sequenceService;
    }

    /**
     * 清除单例缓存
     */
    public static void cleanService() {
        serviceMap.clear();
    }
}
