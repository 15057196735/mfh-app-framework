
package com.mfh.comn.code.bean;

/**
 * @author administrator
 *
 *  编码项级别定义，也可作为维度的层次定义
 */
public class Level{
	protected String nullParentValue;
	protected String name;
	protected String column;
	protected String nameColumn;
	protected String type;
	protected String caption;
	protected String defaultvalue;
	protected String parentColumn;
	protected String table;	
	private boolean uniqueMembers;

	public Level() {
        super();
    }

    public Level(String name) {
        super();
        this.name = name;
    }
	
    /**
     * 
     * @param name level名字
     * @param tableName 表名
     */
    public Level(String name, String tableName) {
        super();
        this.name = name;
        this.table = tableName;
    }

    /**
     * 构造函数
     * @param name level名字
     * @param table 该level所属的表名
     * @param type level类型，如string,int
     */
    public Level(String name, String table, String type) {
        super();
        this.name = name;
        this.type = type;
        this.table = table;
    }

    public boolean getUniqueMembers() {
		return this.uniqueMembers;
	}

	public void setUniqueMembers(boolean uniqueMembers) {
		this.uniqueMembers = uniqueMembers;
	}
		
	/**
	 * @return Returns the nullParentValue.
	 */
	public String getNullParentValue() {
		if(nullParentValue == null)
			nullParentValue = ""; 
		return nullParentValue;
	}
	/**
	 * @param nullParentValue The nullParentValue to set.
	 */
	public void setNullParentValue(String nullParentValue) {
		this.nullParentValue = nullParentValue;
	}
    /**
     * @return Returns the parentColumn.
     */
    public String getParentColumn() {
        return parentColumn;
    }
    
    /**
     * 是否为父子型level
     * @return
     * @author zhangyz created on 2012-7-6
     */
    public boolean isPcKindLevle(){
        return parentColumn != null && parentColumn.length() > 0;
    }
    
    /**
     * @param parentColumn The parentColumn to set.
     */
    public void setParentColumn(String parentColumn) {
        this.parentColumn = parentColumn;
    }
    /**
     * @return Returns the table.
     */
    public String getTable() {
        return table;
    }
    
    /**
     * @param table The table to set.
     */
    public void setTable(String table) {
        this.table = table;
    }
    
	/**
	 * @return Returns the defaultvalue.
	 */
	public String getDefaultvalue() {
		return defaultvalue;
	}
	
	/**
	 * @param defaultvalue The defaultvalue to set.
	 */
	public void setDefaultvalue(String defaultvalue) {
		this.defaultvalue = defaultvalue;
	}
	
	/**
	 * @return Returns the caption.
	 */
	public String getCaption() {
		return caption;
	}
	
	/**
	 * @param caption The caption to set.
	 */
	public void setCaption(String caption) {
		this.caption = caption;
	}
	
	/**
	 * @return Returns the column.
	 */
	public String getColumn() {
		return column;
	}
	
	/**
	 * @param column The column to set.
	 */
	public void setColumn(String column) {
		this.column = column;
	}
	
	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return Returns the nameColumn.
	 */
	public String getNameColumn() {
		if((nameColumn == null) ||(nameColumn.length() == 0))
			return column;
		else
			return nameColumn;
	}
	/**
	 * @param nameColumn The nameColumn to set.
	 */
	public void setNameColumn(String nameColumn) {
		this.nameColumn = nameColumn;
	}
	/**
	 * @return Returns the type.
	 */
	public String getType() {
		return type;
	}
	/**
	 * @param type The type to set.
	 */
	public void setType(String type) {
		this.type = type;
	}
}
