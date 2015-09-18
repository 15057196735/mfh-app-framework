package com.mfh.comn.code.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.mfh.comn.code.ICodeItem;
import com.mfh.comn.code.ITreeCodeItem;
import com.mfh.comn.code.UnionCode;

/**
 * 一个树编码项
 * T: 编码值类型
 * @author zhangyz created on 2013-6-7
 * @since Framework 1.0
 */
public class ParentChildItem<T> implements Cloneable, ITreeCodeItem<T>, Serializable{
    private static final long serialVersionUID = 1L;
    protected T pid;
    protected String codeTypeId;//相当于属于哪个level
    protected List<ICodeItem<T>> items = null;    
    private boolean bHasChild = true;//默认认为有子节点,但不能根据items判断
    
    protected T id;//编码值    
    protected String name;//编码描述
   
    public ParentChildItem() {
        super();
    }

    public ParentChildItem(T entity) {
        ParentChildItem<T> parentChildItem = (ParentChildItem) entity;
        this.pid = (T) ((ParentChildItem) entity).getPid();
        this.codeTypeId = ((ParentChildItem) entity).getCodeTypeId();
        this.items = ((ParentChildItem) entity).getChildItems();
        this.bHasChild = ((ParentChildItem) entity).bHasChild;
        this.id = (T) ((ParentChildItem) entity).getId();
        this.name = ((ParentChildItem) entity).getValue();

       // return parentChildItem;
    }

    @Override
    public ParentChildItem<T> clone(){
        ParentChildItem<T> ret = new ParentChildItem<T>(this.id, this.name);
        ret.codeTypeId = this.codeTypeId;
        ret.pid = this.pid;
        ret.bHasChild = this.bHasChild;
        ret.items = this.items;
        return ret;
    }
    
    /**
     * 创建一个对象
     * @param name 名称
     * @param value 值
     * @param levelName level名
     * @param noChild 是否还有子节点
     * @return
     * @author zhangyz created on 2012-4-11
     */
    public static <T> ParentChildItem<T> makeOption(T name, String value, String levelName, boolean noChild){
        ParentChildItem<T> item = new ParentChildItem<T>(name, value, null, levelName);
        if (noChild)
            item.notifyNoChild();
        return item;
    }
    
    public ParentChildItem(T id, String name) {
        super();
        this.id = id;
        this.name = name;
    }

    public ParentChildItem(T id, String name, T pid) {
        super();
        this.id = id;
        this.name = name;
        this.pid = pid;
    }
    
    /**
     * 完整的构造函数
     * @param id 编码值
     * @param name 编码名称
     * @param pid 父编码值
     * @param levelName 属于哪个级别
     */
    public ParentChildItem(T id, String name, T pid, String levelName) {
        super();
        this.id = id;
        this.name = name;
        this.pid = pid;
        this.codeTypeId = levelName;
    }
    
    /**
     * 获取该编码所属的level名，若为空，代表只是一个level
     * @return
     * @author zhangyz created on 2012-4-9
     */
    public String getLevelName() {
        return codeTypeId;
    }

    /**
     * 设置该编码所属的level名，若为空，代表只是一个level
     * @return
     * @author zhangyz created on 2012-4-9
     */
    public void setLevelName(String levelName) {
        this.codeTypeId = levelName;
    }
    
    /**
     * 告知不会有子节点
     * @see hasChildAbility hasChildFact
     * @author zhangyz created on 2012-4-11
     */
    @Override
    public void notifyNoChild(){
        bHasChild = false;
        if (items != null)
            items.clear();
    }
    
    /**
     * 告知含有子节点
     * 
     * @author zhangyz created on 2012-4-11
     */
    @Override
    public void notifyHaveChild(){
        bHasChild = true;
    }
    
    /**
     * 是否可能有子节点
     * @return
     * @author zhangyz created on 2012-4-11
     */
    public boolean hasChildAbility(){
        return bHasChild;
    }
    
    /**
     * 当前是否有子节点;未来通过进一步读取可能有，但是不管了。
     * @return
     * @author zhangyz created on 2012-4-1
     */
    @Override
    public boolean hasChildFact(){
        if (items == null)
            return false;
        else if (items.size() > 0)
            return true;
        else
            return false;
    }
    
    /*public String[] getTypes() {
        return types;
    }

    public void setTypes(String[] types) {
        this.types = types;
    }

    public long getPk() {
        return pk;
    }

    public void setPk(long pk) {
        this.pk = pk;
    }*/
    
    /**
     * @return Returns the items.
     */
    @Override
    public List<ICodeItem<T>> getChildItems() {
        return items;
    }

    /**
     * 直接赋予所有子节点
     * @param inItems
     * @author zhangyz created on 2012-4-11
     */
    public void setChildItems(List<ICodeItem<T>> inItems){
        items = inItems;
        if (items != null && items.size() > 0){
            this.bHasChild = true;
            for (int ii = 0; ii < inItems.size(); ii++){
                ParentChildItem<T> pi = (ParentChildItem<T>)inItems.get(ii);
                if (pi.getPid() == null)
                    pi.setPid(this.getCode());
            }
        }    
    }
    
    public void addChildItem(ICodeItem<T> item) {
        if (items == null)
            items = new ArrayList<ICodeItem<T>>(); 
        items.add(item);
        ParentChildItem<T> pi = (ParentChildItem<T>)item;
        if (pi.getPid() == null)
            pi.setPid(this.getCode());
        this.bHasChild = true;
    }

    public boolean isTopParent() {
        if (pid == null || pid.toString().length() == 0) {
            return true;
        }
        return false;
    }
    
    /**
     * @return Returns the pid.
     */
    public T getPid() {
        return pid;
    }

    /**
     * @param pid The pid to set.
     */
    public void setPid(T pid) {
        this.pid = pid;
    }

    /**
     * @see java.lang.Object#equals(Object)
     */
    public boolean equals(Object object) {
        if (!(object instanceof ParentChildItem)) {
            return false;
        }
        @SuppressWarnings("unchecked")
        ParentChildItem<T> rhs = (ParentChildItem<T>) object;
        if (this.id.equals(rhs.id)) {
            if (this.name.equals(rhs.name)) {
                if (this.getPid() == null && rhs.getPid() == null) {
                    return true;
                }
                if (this.getPid() != null && rhs.getPid() == null) {
                    return false;
                }
                if (this.getPid() == null && rhs.getPid() != null) {
                    return false;
                }
                else {
                    return false;
                }
            }
            else {
                return false;
            }

        }
        else {
            return false;

        }
    }

    public String getCodeTypeId()
    {
        return codeTypeId;
    }

    public void setCodeTypeId(String codeTypeId)
    {
        this.codeTypeId = codeTypeId;
    }

    @Override
    public String getKind() {
        return codeTypeId;
    }

    @Override
    public UnionCode getUnionCode() {
        return new UnionCode(id.toString());
    }
    
    public T getId() {
        return id;
    }
    
    public void setId(T id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public T getCode() {
        return id;
    }

    @Override
    public String getValue() {
        return name;
    }

    @Override
    public boolean isNullId() {
        return CodeItem.isNullId(id);
    }

    @Override
    public void setKind(String levelName) {
        setLevelName(levelName);        
    }
}

