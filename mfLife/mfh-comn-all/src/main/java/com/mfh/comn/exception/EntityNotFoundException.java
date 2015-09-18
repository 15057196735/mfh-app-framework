package com.mfh.comn.exception;

/**
 * 自定义异常,指示实体未找到的异常
 * 
 * @author zhangyz created on 2012-8-29
 * @since Framework 1.0
 */
@SuppressWarnings("serial")
public class EntityNotFoundException extends RuntimeException{

    public EntityNotFoundException() {
        super();
        // TODO Auto-generated constructor stub
    }

    public EntityNotFoundException(String arg0, Throwable arg1) {
        super(arg0, arg1);
        // TODO Auto-generated constructor stub
    }

    public EntityNotFoundException(String arg0) {
        super(arg0);
        // TODO Auto-generated constructor stub
    }

    public EntityNotFoundException(Throwable arg0) {
        super(arg0);
        // TODO Auto-generated constructor stub
    }

}
