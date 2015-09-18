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
package net.tsz.afinal.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import com.alibaba.fastjson.util.DeserializeBeanInfo;
import com.alibaba.fastjson.util.FieldInfo;
import com.mfh.comn.annotations.Id;
import com.mfh.comn.annotations.Table;

import net.tsz.afinal.db.table.ManyToOne;
import net.tsz.afinal.db.table.OneToMany;
import net.tsz.afinal.db.table.Property;
import net.tsz.afinal.db.table.TableInfo;
import net.tsz.afinal.exception.DbException;

public class ClassUtils {
	/**
	 * 借助与阿里json开源工具，获取指定类的所有正常属性（包括父类的，有setter的或直接只定义field的，
	 * 不包括static和transient的）。
	 * 本afinal开源框架远远没有考虑周全。
	 * @param clazz
	 * @return
	 * @author zhangyz created on 2014-3-12
	 */
	public static List<FieldInfo> getAllNormalFields(Class<?> clazz) {
	    DeserializeBeanInfo beanInfo = new DeserializeBeanInfo(clazz);
	    DeserializeBeanInfo.computeSettersInner(beanInfo, (Type)clazz, true);
	    List<FieldInfo> fieldInfos = beanInfo.getFieldList();
	    return fieldInfos;
	}
    
	/**
	 * 根据实体类 获得 实体类对应的表名
	 * @param entity
	 * @return
	 */
	public static String getTableName(Class<?> clazz) {
		Table table = clazz.getAnnotation(Table.class);
		if(table == null || table.name().trim().length() == 0 ){
			//当没有注解的时候默认用类的名称作为表名,并把点（.）替换为下划线(_)
			return clazz.getName().replace('.', '_');
		}
		return table.name();
	}
	
	/**
	 * 
     * 获取指定对象的主键值
	 * @param entity
	 * @return
	 * @author zhangyz created on 2014-3-12
	 */
	public static Object getPrimaryKeyValue(Object entity) {
	    // ClassUtils.getPrimaryKeyField(entity.getClass())
		return FieldUtils.getFieldValue(entity, TableInfo.get(entity.getClass()).getId().getField());
	}
	
	/**
	 * 根据实体类获得表的主键数据库字段名
	 * @param entity
	 * @return
	 */
	public static String getPrimaryKeyColumn(Class<?> clazz) {
        return TableInfo.get(clazz).getId().getColumn();
		/*String primaryKey = null ;//老的代码，无用
		Field[] fields = clazz.getDeclaredFields();
		if(fields != null){
			Id idAnnotation = null ;
			Field idField = null ;
			
			for(Field field : fields){ //获取ID注解
				idAnnotation = field.getAnnotation(Id.class);
				if(idAnnotation != null){
					idField = field;
					break;
				}
			}
			
			if(idAnnotation != null){ //有ID注解
				primaryKey = idAnnotation.column();
				if(primaryKey == null || primaryKey.trim().length() == 0)
					primaryKey = idField.getName();
			}
			else{ //没有ID注解,默认去找 _id 和 id 为主键，优先寻找 _id
				for(Field field : fields){
					if("_id".equals(field.getName()))
						return "_id";
				}
				
				for(Field field : fields){
					if("id".equals(field.getName()))
						return "id";
				}
			}
		}else{
			throw new RuntimeException("this model["+clazz+"] has no field");
		}
		return primaryKey;*/
	}
	
	/**
	 * 判断是否为主键字段
	 * @param field
	 * @return
	 * @author zhangyz created on 2014-3-12
	 */
	public static boolean isPrimaryKeyField(Field field) {            
        //获取ID注解
        if(field.getAnnotation(Id.class) != null)
            return true;
        if("_id".equals(field.getName()))
            return true;
        if("id".equals(field.getName()))
            return true;
        return false;
    }
	
	/**
	 * 根据类名获得该类的主键java对象,原来的方法,老的代码已被zhangyz废弃
	 * @param entity
	 * @deprecated
	 * @return
	 */
	/*public static Field getPrimaryKeyField(Class<?> clazz) {
		Field primaryKeyField = null ;
		Field[] fields = clazz.getDeclaredFields();
		if(fields != null){
			
			for(Field field : fields){ //获取ID注解
				if(field.getAnnotation(Id.class) != null){
					primaryKeyField = field;
					break;
				}
			}
			
			if(primaryKeyField == null){ //没有ID注解
				for(Field field : fields){
					if("_id".equals(field.getName())){
						primaryKeyField = field;
						break;
					}
				}
			}
			
			if(primaryKeyField == null){ // 如果没有_id的字段
				for(Field field : fields){
					if("id".equals(field.getName())){
						primaryKeyField = field;
						break;
					}
				}
			}
			
		}else{
			throw new RuntimeException("this model["+clazz+"] has no id field");
		}
		return primaryKeyField;
	}*/
	
	
	/**
	 * 根据类名获得该类的主键属性名
	 * @param entity
	 * @return
	 */
	public static String getPrimaryKeyFieldName(Class<?> clazz) {
	    net.tsz.afinal.db.table.Id id = TableInfo.get(clazz).getId();
	    return id == null ? null : id.getFieldName();
		/*Field f = getPrimaryKeyField(clazz);
		return f==null ? null:f.getName();*///老的代码，无用
	}
	
	/**
	 * 把java字段对象转换成本框架的property对象
	 * @param f
	 * @param clazz
	 * @return
	 * @author zhangyz created on 2014-3-12
	 */
	public static Property genProperty(Field f, Class<?> clazz) {
	  //必须是基本数据类型和没有标瞬时态的字段
        if(!FieldUtils.isTransient(f)){
            if (FieldUtils.isBaseDateType(f)) {                
                Property property = new Property();
            
                //property.setColumn(FieldUtils.getColumnByField(f));
                String[] colCaption = FieldUtils.getColumnByField(f);
                property.setColumnAndCaption(colCaption[0], colCaption[1]);
                
                property.setFieldName(f.getName());
                property.setDataType(f.getType());
                property.setDefaultValue(FieldUtils.getPropertyDefaultValue(f));
                property.setSet(FieldUtils.getFieldSetMethod(clazz, f));
                property.setGet(FieldUtils.getFieldGetMethod(clazz, f));
                property.setField(f);
                
                return property;
            }
        }
        return null;
	}
	
	
	/**
	 * 将对象转换为ContentValues, 老的代码已被zhangyz废弃
	 * @deprecated
	 * @param entity
	 * @param selective 是否忽略 值为null的字段
	 * @return
	 */
    /*private static List<Property> getPropertyList(Class<?> clazz) {
		
		List<Property> plist = new ArrayList<Property>();
		try {
			Field[] fs = clazz.getDeclaredFields();
			String primaryKeyFieldName = getPrimaryKeyFieldName(clazz);
			for (Field f : fs) {
				//必须是基本数据类型和没有标瞬时态的字段
				if(!FieldUtils.isTransient(f)){
					if (FieldUtils.isBaseDateType(f)) {
						
						if(f.getName().equals(primaryKeyFieldName)) //过滤主键
							continue;
						
						Property property = new Property();
					
						//property.setColumn(FieldUtils.getColumnByField(f));
		                String[] colCaption = FieldUtils.getColumnByField(f);
		                property.setColumnAndCaption(colCaption[0], colCaption[1]);
						
						property.setFieldName(f.getName());
						property.setDataType(f.getType());
						property.setDefaultValue(FieldUtils.getPropertyDefaultValue(f));
						property.setSet(FieldUtils.getFieldSetMethod(clazz, f));
						property.setGet(FieldUtils.getFieldGetMethod(clazz, f));
						property.setField(f);
						
						plist.add(property);
					}
				}
			}
			return plist;
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}*/
	
	
	/**
	 * 将对象转换为ContentValues
	 * 
	 * @param entity
	 * @param selective 是否忽略 值为null的字段
	 * @return
	 */
	public static List<ManyToOne> getManyToOneList(Class<?> clazz) {
		
		List<ManyToOne> mList = new ArrayList<ManyToOne>();
		try {
			Field[] fs = clazz.getDeclaredFields();
			for (Field f : fs) {
				if (!FieldUtils.isTransient(f) && FieldUtils.isManyToOne(f)) {
					
					ManyToOne mto = new ManyToOne();
					mto.setManyClass(f.getType());
					//mto.setColumn(FieldUtils.getColumnByField(f));
                    String[] colCaption = FieldUtils.getColumnByField(f);
                    mto.setColumnAndCaption(colCaption[0], colCaption[1]);
                    
					mto.setFieldName(f.getName());
					mto.setDataType(f.getType());	
					mto.setSet(FieldUtils.getFieldSetMethod(clazz, f));
					mto.setGet(FieldUtils.getFieldGetMethod(clazz, f));
					
					mList.add(mto);
				}
			}
			return mList;
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}
	
	
	/**
	 * 将对象转换为ContentValues
	 * 
	 * @param entity
	 * @param selective 是否忽略 值为null的字段
	 * @return
	 */
	public static List<OneToMany> getOneToManyList(Class<?> clazz) {
		
		List<OneToMany> oList = new ArrayList<OneToMany>();
		try {
			Field[] fs = clazz.getDeclaredFields();
			for (Field f : fs) {
				if (!FieldUtils.isTransient(f) && FieldUtils.isOneToMany(f)) {
					
					OneToMany otm = new OneToMany();
					
					//otm.setColumn(FieldUtils.getColumnByField(f));
                    String[] colCaption = FieldUtils.getColumnByField(f);
                    otm.setColumnAndCaption(colCaption[0], colCaption[1]);
					
					otm.setFieldName(f.getName());
					
					Type type = f.getGenericType();
					
					if(type instanceof ParameterizedType){
						ParameterizedType pType = (ParameterizedType) f.getGenericType();
						Class<?> pClazz = (Class<?>)pType.getActualTypeArguments()[0];
						if(pClazz!=null)
							otm.setOneClass(pClazz);
					}else{
						throw new DbException("getOneToManyList Exception:"+f.getName()+"'s type is null");
					}
					
					otm.setDataType(f.getClass());
					otm.setSet(FieldUtils.getFieldSetMethod(clazz, f));
					otm.setGet(FieldUtils.getFieldGetMethod(clazz, f));
					
					oList.add(otm);
				}
			}
			return oList;
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}	
	
	
}
