package com.mfh.comn;

public class ConstantActions {
    //数据维护访问类型
    public static final String REGISTER = "register";//注册
    
    public static final String INSERT = ActionType.insert.toString();
    public static final String UPDATE = ActionType.update.toString();//Constants.FORM_UPDATE;
    public static final String DELETE = ActionType.delete.toString();
    public static final String SAVEAS = ActionType.saveAs.toString();  //另存 
    
    public static final String MOVE = ActionType.move.toString();
    public static final String FULLUPDATE = "fullUpdate"; //保存之前先全部删除
    public static final String IGNORE = "ignore"; //图片集(多图片字段)维护时对部分图片无需修改则置此值
    
    public static int OPER_INSERT = 0;
    public static int OPER_UPDATE = 1;
    public static int OPER_DELETE = 2;
    public static int OPER_IGNORE = 3;

    
    /**
     * 把字符串的操作类型转为整形
     * @param kind
     * @return
     * @author zhangyz created on 2012-5-22
     */
    public static int TranslateOperKind(String kind){
        int operKind = OPER_INSERT;
        if (kind.equals(UPDATE))
            operKind = OPER_UPDATE;
        else if (kind.equals(INSERT))
            operKind = OPER_INSERT;
        else if (kind.equals(DELETE))
            operKind = OPER_DELETE;
        else if (kind.equals(IGNORE))
            operKind = OPER_IGNORE;
        else
            throw new RuntimeException("不支持的操作类型" + kind);
        return operKind;
    }
}
