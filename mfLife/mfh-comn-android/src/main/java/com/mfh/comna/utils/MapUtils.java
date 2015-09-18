/*
 * 文件名称: MapUtils.java
 * 版权信息: Copyright 2013-2014 chunchen technology Co., LTD. All right reserved.
 * ----------------------------------------------------------------------------------------------
 * 修改历史:
 * ----------------------------------------------------------------------------------------------
 * 修改原因: 新增
 * 修改人员: zhangyz
 * 修改日期: 2014-5-8
 * 修改内容: 
 */
package com.mfh.comna.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * map工具类
 * @author zhangyz created on 2014-5-8
 */
public class MapUtils {

    /**
     * 直接生成map，并传入key-value对
     * @param keysAndValues
     * @return
     * @author zhangyz created on 2014-5-8
     */
    public static Map<String, Object> genMap(Object... keysAndValues) {
        Map<String, Object> ret = new HashMap<String, Object> ();
        
        int len = keysAndValues.length;
        if (len == 0)
            return ret;
        
        if (len % 2 != 0)
          throw new IllegalArgumentException("传入的参数必须成对!");
        for (int i = 0; i < len; i += 2) {
          String key = String.valueOf(keysAndValues[i]);
          Object val = keysAndValues[i + 1];
          if (val != null)
              ret.put(key, val);
        }
        
        return ret;
    }

    /**
     * 生成list列表
     * @param values
     * @param <T>
     * @return
     */
    public static <T> List<T> genList(T... values) {
        List<T> ret = new ArrayList<T>();
        if (values == null)
            return ret;

        for (T value : values) {
            ret.add(value);
        }
        return ret;
    }
}
