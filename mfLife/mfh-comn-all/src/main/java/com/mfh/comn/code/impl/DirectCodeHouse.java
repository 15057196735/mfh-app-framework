/*
 * 文件名称: DomainDirectRef.java
 * 版权信息: Copyright 2013-2014 chunchen technology Co., LTD. All right reserved.
 * ----------------------------------------------------------------------------------------------
 * 修改历史:
 * ----------------------------------------------------------------------------------------------
 * 修改原因: 新增
 * 修改人员: zhangyz
 * 修改日期: 2014-3-19
 * 修改内容: 
 */
package com.mfh.comn.code.impl;

import java.util.ArrayList;
import java.util.List;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mfh.comn.code.ICodeItem;
import com.mfh.comn.code.ICodeValueItem;
import com.mfh.comn.code.ISimpleCodeHouse;
import com.mfh.comn.code.ITreeCodeItem;
import com.mfh.comn.code.bean.Level;
import com.mfh.comn.code.bean.ParentChildItem;

/**
 * 直接编码库
 * 同时支持支持简单编码和树编码
 * @author zhangyz created on 2012-4-6
 * @since Framework 1.0
 */
public class DirectCodeHouse<T> extends CodeHouse<T> implements ISimpleCodeHouse<T>{
    private List<ICodeItem<T>> options;
    private DomanOkCache<ITreeCodeItem<T>> accessOkCache = null;//完全通过验证的item项,在逐级请求树编码时可以加快速度
    private List<Level> levels = null;
    
    public DirectCodeHouse() {
        super();
        options = new ArrayList<ICodeItem<T>>();
    }
    
    public DirectCodeHouse(List<ICodeItem<T>> options) {
        super();
        this.options = options;
    }

    @Override
    public List<ICodeItem<T>> getOptions() {
        return options;
    }

    @Override
    public List<ICodeItem<T>> getOptions(T parentCode) {
        if (parentCode != null)
            return getOptionsByCode(options, parentCode);
        else
            return options;
    }
    
    /**
     * 根据编码值获取对应的编码信息
     * @param code
     * @return
     * @author zhangyz created on 2012-7-10
     */
    public ICodeItem<T> getOption(T code){
        return getOptionByCode(options, code);
    }
        
    public void setOptions(List<ICodeItem<T>> options) {
        this.options = options;
    }
    
    public void setOptions(List<ICodeItem<T>> options, 
            DomanOkCache<ITreeCodeItem<T>> accessOkCache) {
        this.options = options;
        this.accessOkCache = accessOkCache;
    }
    
    /**
     * 获取第一个层次的名
     * @return
     * @author zhangyz created on 2012-6-14
     */
    protected String getFirstLevelName(){
        if (levels == null || levels.size() == 0)
            return null;
        else
            return levels.get(0).getName();
    }
    
    public static <T> DirectCodeHouse<T> getRef(){
        return new DirectCodeHouse<T>();
    }
    
    public List<Level> getLevels() {
        return levels;
    }

    /**
     * 设置级别定义
     * @param levels
     * @author zhangyz created on 2012-4-9
     */
    public void setLevels(List<Level> levels) {
        this.levels = levels;
    }

    public void addLevel(Level level){
        if (levels == null)
            levels = new ArrayList<Level>();
        if (!levels.contains(level))
            levels.add(level);
        else
            return;
    }
    
    public void addLevel(String levelName){
        Level level = new Level(levelName);
        addLevel(level);
    }
   
    public void addLevel(String levelName, String tableName){
        Level level = new Level(levelName, tableName);
        addLevel(level);
    }

    public void addLevel(String levelName, String tableName, String type){
        Level level = new Level(levelName, tableName, type);
        addLevel(level);
    }

    /**
     * 增加一个顶层编码
     * @param name
     * @param value
     * @author zhangyz created on 2012-4-2
     */
    public DirectCodeHouse<T> addOption(T name, String value){
        return addOption(name, value, null, true);
    }
    
    /**
     * 增加一个顶层编码,同时告知有没有子节点
     * @param name
     * @param value
     * @param noChild 是否有子节点
     * @author zhangyz created on 2012-4-2
     */
    public DirectCodeHouse<T> addOption(T name, String value, boolean noChild){
        return addOption(name, value, null, noChild);
    }
    
    /**
     * 增加一个顶层编码
     * @param name 编码名
     * @param value 编码值
     * @param levelName 级别名
     * @return
     * @author zhangyz created on 2012-4-9
     */
    public DirectCodeHouse<T> addOption(T name, String value, String levelName){
        return addOption(name, value, levelName, true);
    }
    
    /**
     * 增加一个顶层编码,同时告知有没有子节点
     * @param name 编码名
     * @param value 编码值
     * @param levelName 级别名
     * @param noChild 是否有子节点
     * @return
     * @author zhangyz created on 2012-4-9
     */
    public DirectCodeHouse<T> addOption(T name, String value, String levelName, boolean noChild){
        ParentChildItem<T> item = new ParentChildItem<T>(name, value, null, levelName);
        if (noChild)
            item.notifyNoChild();
        options.add(item);
        return this;
    }
    
    /**
     * 直接增加一个顶层编码项
     * @param item
     * @return
     * @author zhangyz created on 2012-4-9
     */
    public DirectCodeHouse<T> addOption(ICodeItem<T> item) {
        options.add(item);
        return this;
    }
    
    /**
     * 增加一个顶层编码,编码值和编码名同名
     * @param name
     * @return
     * @author zhangyz created on 2012-4-9
     */
    public DirectCodeHouse<T> addOption(T name){
        return addOption(name, name.toString(), null, true);
    }

    /**
     * 内部递归函数
     * @param options
     * @param jsonOptions
     * @author zhangyz created on 2012-4-1
     */
    private boolean addOptions(List<ICodeItem<T>> options, JSONArray jsonOptions, boolean bTop){
        if (options == null)
            return false;
        JSONObject jsonItem;
        boolean hasChild = false;
        if (this.levels != null && levels.size() > 0) {
            //带有level的
            ICodeValueItem<T> item;
            for (int jj = 0; jj < options.size(); jj++) {
                jsonItem = new JSONObject();
                item = (ICodeValueItem<T>)options.get(jj);        
                jsonItem.put("code",item.getCode());
                jsonItem.put("value", item.getValue());
                if (item.getKind() == null)
                    jsonItem.put("levelName", "");
                else
                    jsonItem.put("levelName", item.getKind());
                jsonItem.put("hasChild", item.hasChildAbility() ? "true" : "false");
                
                if (item.hasChildAbility()) {
                    ITreeCodeItem<T> treeItem = (ITreeCodeItem<T>)item;
                    if (bTop && accessOkCache != null) {
                        jsonItem.put("access", Integer.toString(accessOkCache.getAccessFlag(treeItem)));
                    }
                    else
                        jsonItem.put("access", Integer.toString(ICodeValueItem.NODE_VIEW_OK));//本层已经完全通过验证.
                    
                    if (treeItem.hasChildFact()){
                        JSONArray jsonSubOptions = new JSONArray();
                        List<ICodeItem<T>> childs = treeItem.getChildItems();
                        addOptions(childs, jsonSubOptions, false);
                        jsonItem.put("items", jsonSubOptions);
                        hasChild = true;
                    }
                }
                else
                    jsonItem.put("access", Integer.toString(ICodeValueItem.NODE_VIEW_OK));
                
                jsonOptions.add(jsonItem);
            } 
        }
        else {//更简洁，加快速度
            ICodeItem<T> item;
            for (int jj = 0; jj < options.size(); jj++) {
                jsonItem = new JSONObject();
                item = options.get(jj);        
                jsonItem.put("code",item.getCode());
                jsonItem.put("value", item.getValue());
                
                jsonOptions.add(jsonItem);
            }            
        }
        return hasChild;
    }
    
    /**
     * 将传入的值输出成json数组
     * @return
     * @author zhangyz created on 2012-4-1
     */
    /*public JSONArray exportToJson(){
        JSONArray jsonOptions = new JSONArray();        
        addOptions(options, jsonOptions, true);
        return jsonOptions;
    }*/
    
    /**
     * 将指定的列表输出成json格式
     * @param theOptions
     * @return
     * @author zhangyz created on 2014-6-18
     */
    public JSONObject getJsonObjectInner(List<ICodeItem<T>> theOptions) {
        JSONArray jsonLevel = null;
        int levelSize = 0;
        boolean haveParent = false;
        if (this.levels != null){
            jsonLevel = new JSONArray();
            JSONObject eleLevel = null;
            for (Level level :levels){
                eleLevel = new JSONObject();
                eleLevel.put("name", level.getName());
                if (level.getTable() == null)
                    eleLevel.put("table", "");
                else
                    eleLevel.put("table", level.getTable());
                if (level.getType() == null)
                    eleLevel.put("type", "string");
                else
                    eleLevel.put("type", level.getType());
                if (level.getParentColumn() != null) {
                    eleLevel.put("parentColumn", level.getParentColumn());
                    haveParent = true;
                }
                else
                    eleLevel.put("parentColumn", "");
                    
                jsonLevel.add(eleLevel);
                levelSize++;
            }
        }
        else
            levelSize = 1;        
        JSONArray jsonOptions = new JSONArray();
        
        boolean bDeepType = addOptions(theOptions, jsonOptions, true);        
        JSONObject jsonDomain = new JSONObject();
        if (bDeepType || haveParent)
            jsonDomain.put("deepType", "1");
        else
            jsonDomain.put("deepType", "0");
        if (parent != null)
            jsonDomain.put("parent", parent);
            
        jsonDomain.put("levelNum", Integer.toString(levelSize));
        if (jsonLevel != null)
            jsonDomain.put("levels", jsonLevel);
        jsonDomain.put("options", jsonOptions);
        return jsonDomain;
    }
    
    @Override
    public JSONObject getJsonObject() {
        return getJsonObjectInner(options);
    }

    /**
     * 根据编码描述获取编码值
     * @param caption
     * @return
     * @author zhangyz created on 2012-8-25
     */
    public T getCodeByValue(String caption){
        ICodeItem<T> option = getOptionByCaption(options, caption);
        if (option == null)
            return null;
        else
            return option.getCode();
    }
    
    /**
     * 根据编码描述获取编码值
     * @param options
     * @param caption
     * @return
     * @author zhangyz created on 2012-4-6
     */
    private ICodeItem<T> getOptionByCaption(List<ICodeItem<T>> options, String caption) {
        if (options == null)
            return null;
        ICodeItem<T> item;
        for (int jj = 0; jj < options.size(); jj++){
            item = options.get(jj);
            if (item.getValue().equals(caption))
                return item;
            if (item.hasChildAbility()) {
                ITreeCodeItem<T> treeItem = (ITreeCodeItem<T>)item;
                List<ICodeItem<T>> subOptions = treeItem.getChildItems();
                if (subOptions != null && subOptions.size() > 0){
                    ICodeItem<T> subItem = getOptionByCaption(subOptions, caption);
                    if (subItem != null)
                        return subItem;
                }
            }
        }
        return null;
    }

    /**
     * 根据编码值获取对应的编码信息
     * @param code 当前编码值
     * @return
     * @author zhangyz created on 2012-7-10
     */
    private ICodeItem<T> getOptionByCode(List<ICodeItem<T>> options, T code){
        if (options == null)
            return null;
        ICodeItem<T> item;
        for (int jj = 0; jj < options.size(); jj++){
            item = options.get(jj);
            if (item.getCode().equals(code))
                return item;
            if (item.hasChildAbility() && item instanceof ITreeCodeItem) {
                ITreeCodeItem<T> treeItem = (ITreeCodeItem<T>)item;
                List<ICodeItem<T>>  subOptions = treeItem.getChildItems();
                if (subOptions != null && subOptions.size() > 0){
                    item = getOptionByCode(subOptions, code);
                    if (item != null)
                        return item;
                }
            }
        }
        return null;
    }
    
    /**
     * 根据编码值获取对应的子编码列表信息
     * @param code 当前编码值
     * @return
     * @author zhangyz created on 2012-7-10
     */
    private List<ICodeItem<T>> getOptionsByCode(List<ICodeItem<T>> options, T code){
        if (options == null)
            return null;
        ICodeItem<T> item;
        for (int jj = 0; jj < options.size(); jj++) {
            item = options.get(jj);
            if (!item.hasChildAbility())
                continue;
            ITreeCodeItem<T> treeItem = (ITreeCodeItem<T>)item;
            List<ICodeItem<T>> subOptions = treeItem.getChildItems();
            if (item.getCode().equals(code)) {
                return subOptions;
            }
            if (subOptions != null && subOptions.size() > 0){
                subOptions = getOptionsByCode(subOptions, code);
                if (subOptions != null)
                    return subOptions;
            }
        }
        return null;
    }
    
    @Override
    public String getValue(T code) {
        if (code == null)
            return null;
        ICodeItem<T> option = getOptionByCode(options, code);
        if (option == null)
            return code.toString();
        else
            return option.getValue();
    }

    @Override
    public boolean isTreeAble() {
        if (levels != null && levels.size() > 1)
            return true;
        else if (options != null) {
            ICodeItem<T> option;
            for (int jj = 0; jj < options.size(); jj++) {
                option = options.get(jj);
                if (option.hasChildAbility()) {
                    if (((ITreeCodeItem<T>)option).hasChildFact())
                        return true;
                }
            }
            return false;
        }
        else
            return false;
    }

    /**
     * 做清理工作
     * @author zhangyz created on 2012-6-14
     */
    @Override
    protected void clearCodes() {
        if (options != null)
            options.clear();
        if (levels != null)
            levels.clear();        
    }
    
    public DomanOkCache<ITreeCodeItem<T>> getAccessOkCache() {
        return accessOkCache;
    }
}
