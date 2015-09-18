package com.mfh.comn.code.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.mfh.comn.code.ICodeHouse;
import com.mfh.comn.code.ICodeItem;
import com.mfh.comn.code.ICodeService;
import com.mfh.comn.code.ISimpleCodeHouse;

/**
 * 编码服务类实现
 * 
 * @author zhangyz created on 2013-6-7
 * @since Framework 1.0
 */
public class CodeService implements ICodeService {    
    private Map<String, ICodeHouse<?>> codeMap = new HashMap<String, ICodeHouse<?>>();
    
    private static CodeService service = null;
    
    /**
     * 增加一个直接编码服务工厂
     * @param codeDomain 编码域
     * @param codeMaps key-value对，key是编码，value是编码描述
     * @author zhangyz created on 2014-4-30
     */
    public static <T> void addCodeHouse(String codeDomain, Map<T, String> codeMaps) {        
        DirectCodeHouse<T> dc = new DirectCodeHouse<T>();
        for (T key : codeMaps.keySet()) {
            dc.addOption(key, codeMaps.get(key));
        }      
        getCodeService().addCodeHouse(dc,codeDomain);
    }
    
    /**
     * 增加并返回一个直接编码服务工厂，调用者后续可以直接添加
     * @param codeDomain 编码域
     * @return DirectCodeHouse
     * @author zhangyz created on 2014-4-30
     */
    public static <T> DirectCodeHouse<T> addCodeHouse(String codeDomain) {         
        DirectCodeHouse<T> dc = new DirectCodeHouse<T>();
        getCodeService().addCodeHouse(dc, codeDomain);
        return dc;
    }
    
    /**
     * 增加一个直接编码服务工厂
     * @param codeDomain 编码域
     * @param dc 简单编码工厂
     * @author zhangyz created on 2014-4-30
     */
    public static <T> void addCodeHouse(String codeDomain, ICodeHouse<T> dc) {     
        getCodeService().addCodeHouse(dc, codeDomain);
    }
    
    /**
     * 获取编码服务，单例模式
     * @return
     * @author zhangyz created on 2014-3-17
     */
    public static CodeService getCodeService() {
        if (service == null) {
            synchronized(CodeService.class) {
                if (service == null)
                    service = new CodeService();
            }
        }
        return service;
    }
    
    @Override
    public void addCodeHouse(ICodeHouse<?> codeHouse, String... domain) {
        codeMap.put(genFactDomain(domain), codeHouse);
    }
    
    @Override
    public <T extends ICodeHouse<?>> T initCodeHouse(Class<T> houseClassName, String... domain) {
        T house;
        try {
            house = (T)houseClassName.newInstance();
            codeMap.put(genFactDomain(domain), house);
        }
        catch (Exception e) {
            throw new RuntimeException("实例化编码提供者失败:" + e.getMessage(), e);
        }        
        return house;
    }
    
    /**
     * 直接用DirectCodeHouse类实现一个编码提供者，一般供原型快速开发用。
     * @param domain
     * @return 
     */
    public <T> DirectCodeHouse<T> initDirectCodeHouse(String... domain) {
        DirectCodeHouse<T> house = new DirectCodeHouse<T>();
        codeMap.put(genFactDomain(domain), house);           
        return house;
    }
    
    @Override
    public <T> String getValue(String domain, T code) {
        @SuppressWarnings("unchecked")
        ICodeHouse<T> codeHouse = (ICodeHouse<T>)codeMap.get(domain);
        if (codeHouse == null)
            return null;
        else
            return codeHouse.getValue(code);
    }

    @Override
    public <T> String getValue(Class<?> domain, T code) {
        return getValue(domain.getName(), code);
    }
    
    @Override
    public <T> String getValue(T code) {
        return getValue(DOMAIN_DEFAULT, code);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> ISimpleCodeHouse<T> getSimleCodeHouse(String... domain) {
        return (ISimpleCodeHouse<T>)codeMap.get(genFactDomain(domain));
    }

    /**
     * 获取一个简单编码提供者
     * @param domain
     * @return
     * @author zhangyz created on 2014-6-25
     */
    @SuppressWarnings("unchecked")
    public static <T> ICodeHouse<T> getCodeHouse(String... domain) {
        CodeService cs = getCodeService();
        return (ICodeHouse<T>)cs.codeMap.get(cs.genFactDomain(domain));
        //return getCodeService().getSimleCodeHouse(domain);
    }

    /**
     * 获取一个简单编码提供者
     * @param classObj
     * @return
     * @author zhangyz created on 2014-6-25
     */
    public static <T> ICodeHouse<T> getCodeHouse(Class<? extends ICodeHouse<?>> classObj) {
        return getCodeHouse(classObj.getName());
    }    
    
    /*@SuppressWarnings("unchecked")
    @Override
    public <T> ITreeCodeHouse<T> getTreeCodeHouse(String... domain) {
        return (ITreeCodeHouse<T>)codeMap.get(genFactDomain(domain));
    }*/
    
    @Override
    public <T> List<ICodeItem<T>> getOptions(String domain) {
        return getOptions(null, domain);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> List<ICodeItem<T>> getOptions(T parentCode, String domain) {
        ICodeHouse<T> codeHouse = (ICodeHouse<T>) codeMap.get(genFactDomain(domain));
        if (codeHouse == null)
            return null;
        else {
            if (parentCode != null)
                return ((ISimpleCodeHouse<T>)codeHouse).getOptions(parentCode);
            else
                return ((ISimpleCodeHouse<T>)codeHouse).getOptions();
        }
    }

    /*@Override
    public <T> List<ITreeCodeItem<T>> getTreeOptions(String... domain) {
        return getTreeOptions(null, domain);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> List<ITreeCodeItem<T>> getTreeOptions(T parentCode, String... domain) {
        ICodeHouse<T> codeHouse = (ICodeHouse<T>) codeMap.get(genFactDomain(domain));
        if (codeHouse == null)
            return null;
        else {
            if (parentCode != null)
                return ((ITreeCodeHouse<T>)codeHouse).getOptions(parentCode);
            else
                return ((ITreeCodeHouse<T>)codeHouse).getOptions();                
        }
    }*/

    @Override
    public String getBooleanValue(boolean code) {
        if (code)
            return String.valueOf("是");
        else
            return String.valueOf("否");
    }

    @Override
    public String getIntBooleanValue(int code) {
        if (code > 0)
            return String.valueOf("是");
        else
            return String.valueOf("否");
    }
    
    private String genFactDomain(String... domain) {
        String factDomain = null;
        if (domain != null && domain.length > 0)
            factDomain = domain[0];
        else
            factDomain = DOMAIN_DEFAULT;
        return factDomain;
    }
}
