/*
 * 文件名称: KvUtil.java
 * 版权信息: Copyright 2013-2014 chunchen technology Co., LTD. All right reserved.
 * ----------------------------------------------------------------------------------------------
 * 修改历史:
 * ----------------------------------------------------------------------------------------------
 * 修改原因: 新增
 * 修改人员: zhangyz
 * 修改日期: 2014-3-12
 * 修改内容: 
 */
package com.mfh.comna.comn.bean;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import com.mfh.comn.bean.EntityWrapper;

import net.tsz.afinal.db.table.Id;
import net.tsz.afinal.db.table.Property;
import net.tsz.afinal.db.table.TableInfo;

/**
 * 支持将原始数据结构EntityWrapperbean的导出成GroupKeyValue界面显示结构(key-value)形式，便于view层使用。
 * @author zhangyz created on 2014-3-12
 */
public class KvBean<T> {
    protected EntityWrapper<T> wrapperBean = null;
    private transient List<GroupKeyValue> kvsList = null;//供在列表中显示一条记录
    private transient List<GroupKeyValue> kvsDetail = null;//供在详细页面中显示一条记录
    private final static String KEY_SEPARATOR = "key_seprator";

    // 测试需要
    public KvBean() {
    }

    /**
     * 按分隔bean构造
     * @param caption
     */
    public KvBean(String caption) {
        wrapperBean = new EntityWrapper<T>(null);
        wrapperBean.addCaption(KEY_SEPARATOR, caption);
    }

    /**
     * 按真实bean构造
     * @param bean
     */
    public KvBean(T bean) {
        super();
        this.wrapperBean = new EntityWrapper<T>(bean);
    }

    /**
     * 按真实bean构造
     * @param wrapperBean
     */
    public KvBean(EntityWrapper<T> wrapperBean) {
        super();
        this.wrapperBean = wrapperBean;
    }

    /**
     * 是否分隔bean
     * @return
     */
    public boolean isSeparator() {
        if (wrapperBean != null && (wrapperBean.getBean() == null)
                && wrapperBean.getCaption() != null) {
            return true;
        }
        else
            return false;
    }

    /**
     * 获取分隔标题
     * @return
     */
    public String getSeparatTitle() {
        if (isSeparator()) {
            return wrapperBean.getCaption().get(KEY_SEPARATOR);
        }
        else
            return null;
    }

    /**
     * 转换形式
     * @param wrapperBeans
     * @return
     * @author zhangyz created on 2014-3-12
     */
    public static <T> List<KvBean<T>> exportToKvs(List<EntityWrapper<T>> wrapperBeans) {
        if (wrapperBeans == null)
            return null;
        List<KvBean<T>> ret = new ArrayList<KvBean<T>>();
        for (EntityWrapper<T> wrapper : wrapperBeans) {
            ret.add(new KvBean<T>(wrapper));
        }
        return ret;
    }

    /**
     * 直接把bean转换成目标格式
     * @param beans
     * @param <T>
     * @return
     */
    public static <T> List<KvBean<T>> exportToKvsDirect(List<T> beans) {
        if (beans == null)
            return null;
        List<KvBean<T>> ret = new ArrayList<KvBean<T>>();
        for (T bean : beans) {
            ret.add(new KvBean<T>(new EntityWrapper(bean)));
        }
        return ret;
    }

    /**
     * 获取实际bean对象
     * @return
     * @author zhangyz created on 2014-3-12
     */
    public T getBean() {
        return wrapperBean.getBean();
    }

    /**
     * 将bean导出成key-value形式，供界面上列表直接显示一条记录。
     * 其中key是属性名，value是中文名
     * @return
     * @author zhangyz created on 2014-3-10
     */
    public List<GroupKeyValue> getKvsList() {
        if (kvsList == null)
            kvsList = exportKeyValueInner(false);
        return kvsList;
    }
    
    /**
     * 获取指定属性的属性值（字符串值）,若该属性不存在则抛出异常提醒错误
     * @param propName
     * @return
     * @author zhangyz created on 2014-3-12
     */
    public String getPropValue(String propName) {
        List<GroupKeyValue> kvs = getKvsList();
        for (GroupKeyValue item : kvs) {
            if (item.getKey().equals(propName))
                return item.getValueStr();
        }
        throw new RuntimeException("不存在的属性名，请检查!");
    }
    
    /**
     * 将bean导出成key-value形式，供界面上单条记录详细显示。
     * 其中key是属性名的中文名，value是中文名，两个都是中文名
     * @return
     * @author zhangyz created on 2014-3-10
     */
    public List<GroupKeyValue> getKvsDetail() {
        if (kvsDetail == null)
            kvsDetail = exportKeyValueInner(true);
        return kvsDetail;
    }
    
    /**
     * 将bean导出成key-value形式
     * @param detail true：详细页面显示；false：列表下显示
     * @return
     * @author zhangyz created on 2014-3-10
     */
    private List<GroupKeyValue> exportKeyValueInner(boolean detail) {
        T bean = wrapperBean.getBean();
        Map<String, String> captions = wrapperBean.getCaption();
        
        TableInfo table = TableInfo.get(bean.getClass());
        Iterator<Property> iter = table.propertyMap.values().iterator();
        List<GroupKeyValue> result = new ArrayList<GroupKeyValue>();
        
        Object propValue;
        String caption;
        String propName = "id";
        Id id = table.getId();
        if (id != null) {
            propValue = id.getValue(bean);
            caption = getFactCaption(propName, propValue, captions);
            if (detail)
                result.add(new GroupKeyValue(id.getCaption() == null ? propName : id.getCaption(), caption));
            else
                result.add(new GroupKeyValue(propName, caption));
        }        
        while (iter.hasNext()) {
            Property prop = iter.next();
            propName = prop.getFieldName();
            propValue = prop.getValue(bean);
            caption = getFactCaption(propName, propValue, captions);
            if (detail)
                result.add(new GroupKeyValue(prop.getCaption() == null ? propName : prop.getCaption(), caption));
            else
                result.add(new GroupKeyValue(propName, caption));                
        }        
        return result;
    }  
    
    private String getFactCaption(String propName, Object propValue, 
            Map<String, String> captions) {
        if (captions != null) {
            String captionValue = captions.get(propName);
            if (captionValue != null)
                propValue = captionValue;
        }
        if (propValue == null)
            return "";
        else
            return propValue.toString();
    }
}
