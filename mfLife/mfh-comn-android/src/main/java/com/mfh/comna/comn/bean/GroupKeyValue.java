package com.mfh.comna.comn.bean;


/**
 * keyValue值对,用于界面显示的bean
 * 
 * @author zhangyz created on 2013-5-18
 * @since Framework 1.0
 */
@SuppressWarnings("serial")
public class GroupKeyValue implements ILetterIndexAble<String> {
    private String key;
    private Object value;
    private String groupName;
    
    public GroupKeyValue(String groupName, String key, String value) {
        super();
        if (key == null)
            key = "";
        this.key = key;
        this.value = value;
        this.groupName = groupName;
    }   
    
    public GroupKeyValue(String groupName, String key, Object value) {
        super();
        if (key == null)
            key = "";
        this.key = key;
        this.value = value;
        this.groupName = groupName;
    } 
    
    public GroupKeyValue(String key, String value) {
        super();
        if (key == null)
            key = "";
        this.key = key;
        this.value = value;
    }
    
    public GroupKeyValue(String key, Object value) {
        super();
        if (key == null)
            key = "";
        this.key = key;
        this.value = value;
    }

    public GroupKeyValue() {
        super();
    }

    public String getKey() {
        return key;
    }
    
    public void setKey(String key) {
        this.key = key;
    }
    
    public Object getValue() {
        return value;
    }
    
    public String getValueStr() {
        if (value == null)
            return "";
        else
            return value.toString();
    }
    
    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String getId() {
        return key;
    }

    //@Override
    public void setId(String id) {
        key = id;
    }

    @Override
    public String getLetterIndex() {
        return groupName;
    }
    
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
}
