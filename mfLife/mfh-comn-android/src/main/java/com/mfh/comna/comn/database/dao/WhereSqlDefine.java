package com.mfh.comna.comn.database.dao;

import android.text.TextUtils;

import java.util.List;

/**
 * Created by Administrator on 2014/12/25.
 */
public class WhereSqlDefine {

    public static class Where {
        private String columnName;
        private String operator;
        private Object value;

        public Where(String columnName, String operator, Object value) {
            this.columnName = columnName;
            this.operator = operator;
            this.value = value;
        }
    }

    private static final String OR = " OR ";
    private static final String AND = " AND ";
    private static final String AND_OP = "*";
    private static final String OR_OP = "+";
    public static final String PLACE_HOLDER = "p";

    /**
     *
     * @param clause ;//逻辑运算指示符,例如"!p*(p+p)*p", 其中！非，*与，+或。该属性可以不设置，缺省全部为与
     * */
    public static String getWhereSql(List<Where> wheres, String clause) {
        if (wheres == null || wheres.size() == 0) {
            return null;
        }
        String defaultClause;
        if (TextUtils.isEmpty(clause))
            defaultClause = getDefaultClause(wheres);
        else
            defaultClause = clause.trim();
        return replaceWheres(wheres, defaultClause);
    }

    private static String getDefaultClause(List<Where> wheres) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < wheres.size(); i++) {
            builder.append(PLACE_HOLDER);
            if (i != wheres.size() - 1)
                builder.append(AND_OP);
        }
        return builder.toString();
    }

    private static String replaceWheres(List<Where> wheres, String clause) {
        try {
            String temp = replaceClause(clause);
            String[] result = temp.split(PLACE_HOLDER);
            StringBuilder builder = new StringBuilder();
            Where where = null;
            for (int i = 0; i < wheres.size(); i++) {
                where = wheres.get(i);
                builder.append(result[i]).append(where.columnName).append(where.operator);
                Class<?> clazz = where.value.getClass();
                if (clazz == int.class || clazz == Integer.class || clazz == Long.class || clazz == long.class) {
                    builder.append(where.value);
                } else if (clazz == String.class) {
                    builder.append("'").append(where.value).append("'");
                }
            }
            if (result.length == wheres.size() + 1) {
                builder.append(result[wheres.size()]);
            }
            return builder.toString();
        }catch (Exception e) {
            throw new RuntimeException("格式不符合要求");
        }
    }

    private static String replaceClause(String clause) {
        if (clause.contains(AND_OP) || clause.contains(OR_OP)) {
            String result = clause.replace(AND_OP, AND);
            result = result.replace(OR_OP, OR);
            return result;
        }else {
            throw new RuntimeException("clause 格式不符合要求");
        }
    }
}
