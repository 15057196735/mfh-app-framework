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
package net.tsz.afinal.db.table;

import android.util.Log;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.util.FieldInfo;
import com.mfh.comn.bean.IIntId;
import com.mfh.comn.bean.ILongId;
import com.mfh.comn.bean.IStringId;

import net.tsz.afinal.exception.DbException;
import net.tsz.afinal.reflect.ClassUtils;
import net.tsz.afinal.reflect.FieldUtils;


public class TableInfo {

	private String className;
	private String tableName;
	
	private Id id;
	
	//key是field名，不是属性名
	public final Map<String, Property> propertyMap = new LinkedHashMap<String, Property>();
	
	//key是属性名,add by zhangyz
	private final Map<String, Property> propertyMap2 = new LinkedHashMap<String, Property>();
	
	public final Map<String, OneToMany> oneToManyMap = new LinkedHashMap<String, OneToMany>();
	public final Map<String, ManyToOne> manyToOneMap = new LinkedHashMap<String, ManyToOne>();
	
	private boolean checkDatabese;//在对实体进行数据库操作的时候查询是否已经有表了，只需查询一遍，用此标示
	
	
	private static final HashMap<String, TableInfo> tableInfoMap = new HashMap<String, TableInfo>();
	private static final Map<Class<?>, String> classTableNameMap = new HashMap<Class<?>, String>();
	
	private TableInfo(){}
	
	/**
	 * 设置表名，add by zhangyz 2013-06-11
	 * @param clazz
	 * @param tableName
	 * @author zhangyz created on 2013-6-13
	 */
	public static void setTableName(Class<?> clazz, String tableName) {
	    classTableNameMap.put(clazz, tableName);
	}
	
	/**
	 * 获取指定类对应的表名
	 * @param clazz
	 * @return
	 * @author zhangyz created on 2013-6-13
	 */
	public static String getTableName(Class<?> clazz) {
        String tableName = classTableNameMap.get(clazz);
        if (tableName == null) {
            tableName = ClassUtils.getTableName(clazz);
            classTableNameMap.put(clazz, tableName);
        }
        return tableName;
	}
	
	public static  TableInfo get(Class<?> clazz){
		if(clazz == null)
			throw new DbException("table info get error,because the clazz is null");

		TableInfo tableInfo = tableInfoMap.get(clazz.getName());
		if( tableInfo == null ){
			tableInfo = new TableInfo();

			String tableName = classTableNameMap.get(clazz);
			if (tableName == null) {
			    tableName = ClassUtils.getTableName(clazz);
			    classTableNameMap.put(clazz, tableName);
			}
			tableInfo.setTableName(tableName);
			tableInfo.setClassName(clazz.getName());

			List<FieldInfo> fins = ClassUtils.getAllNormalFields(clazz);//按属性名的字母顺序返回
			boolean bFindId = false;
			List<Property> pList = new ArrayList<Property>();
			for (FieldInfo item : fins) {
			    Field fieldItem = item.getField();
			    if (fieldItem == null)
			        continue;//有可能特殊情况

			    if (ClassUtils.isPrimaryKeyField(fieldItem)) {
			        bFindId = true;
	                Id id = new Id();
	                String[] colCaption = FieldUtils.getColumnByField(fieldItem);
	                id.setColumnAndCaption(colCaption[0], colCaption[1]);
	                id.setFieldName(fieldItem.getName());
	                id.setSet(FieldUtils.getFieldSetMethod(clazz, fieldItem));
	                id.setGet(FieldUtils.getFieldGetMethod(clazz, fieldItem));
                    //下面打个补丁
                    Class<?> pkClass = fieldItem.getType();
                    if (pkClass.equals(Object.class)) {//主键类型未定
                        if (item.getFieldClass() != null)
                            pkClass = item.getFieldClass();
                        if (pkClass.equals(Object.class)) {//修改了fastjson的底层，下面现在执行不到了
                            if (IStringId.class.isAssignableFrom(clazz))
                                pkClass = String.class;
                            else if (IIntId.class.isAssignableFrom(clazz))
                                pkClass = Integer.class;
                            else if (ILongId.class.isAssignableFrom(clazz))
                                pkClass = Long.class;
                            else
                                throw new RuntimeException(clazz.getName() + "应从IString、ILong或IInt等接口继承");//pkClass = String.class;
                        }
                    }
	                id.setDataType(pkClass);
	                tableInfo.setId(id);
			    }
			    else {
			        Property prop = ClassUtils.genProperty(fieldItem, clazz);
			        if (prop != null)
			            pList.add(prop);
			    }
			}
			if (!bFindId) {
			    throw new RuntimeException("this model["+clazz+"] has no id field,  \n can define _id,id property or use annotation @id to solution this exception");
			}
			
			//下面老的代码，已经被zhangyz废弃。
			/*Field idField = ClassUtils.getPrimaryKeyField(clazz);
			if(idField != null){
				Id id = new Id();
				String[] colCaption = FieldUtils.getColumnByField(idField);
				id.setColumnAndCaption(colCaption[0], colCaption[1]);
				id.setFieldName(idField.getName());
				id.setSet(FieldUtils.getFieldSetMethod(clazz, idField));
				id.setGet(FieldUtils.getFieldGetMethod(clazz, idField));
				id.setDataType(idField.getType());
				
				tableInfo.setId(id);
			}
			else{
				throw new DbException("the class["+clazz+"]'s idField is null , \n you can define _id,id property or use annotation @id to solution this exception");
			}			
			List<Property> pList = ClassUtils.getPropertyList(clazz);*/
			
			if(pList!=null){
				for(Property p : pList){
					if(p!=null) {
						tableInfo.propertyMap.put(p.getColumn(), p);
						tableInfo.propertyMap2.put(p.getFieldName(), p);
					}
				}
			}
			
			List<ManyToOne> mList = ClassUtils.getManyToOneList(clazz);
			if(mList!=null){
				for(ManyToOne m : mList){
					if(m!=null)
						tableInfo.manyToOneMap.put(m.getColumn(), m);
				}
			}
			
			List<OneToMany> oList = ClassUtils.getOneToManyList(clazz);
			if(oList!=null){
				for(OneToMany o : oList){
					if(o!=null)
						tableInfo.oneToManyMap.put(o.getColumn(), o);
				}
			}
			
			
			tableInfoMap.put(clazz.getName(), tableInfo);
		}
		
		return tableInfo;
	}
	
	
	public static TableInfo get(String className){
		try {
			return get(Class.forName(className));
		}
		catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 获取所有属性定义，key是propertyName
	 * @return
	 * @author zhangyz created on 2014-3-12
	 */
    public Map<String, Property> getPropertyMap() {
        return propertyMap2;
    }
    
    /**
     * 获取指定属性名代表的属性对象
     * @param propName
     * @return
     * @author zhangyz created on 2014-3-12
     */
    public Property getProperty(String propName) {
        return propertyMap2.get(propName);
    }
    
    /**
     * 获取指定列表代表的属性对象
     * @param columnName
     * @return
     * @author zhangyz created on 2014-3-12
     */
    public Property getPropertyByColumn(String columnName) {
        return propertyMap.get(columnName);
    }

    public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public Id getId() {
		return id;
	}

	public void setId(Id id) {
		this.id = id;
	}

	public boolean isCheckDatabese() {
		return checkDatabese;
	}

	public void setCheckDatabese(boolean checkDatabese) {
		this.checkDatabese = checkDatabese;
	}

	
	
}
