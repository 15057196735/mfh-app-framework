package com.mfh.comn.bean;

/**
 * 两个对象的包装器
 * @param <T1>
 * @param <T2>
 * @author zhangyz created on 2012-3-28
 * @since Framework 1.0
 */
public class Pair<T1,T2> {
    private  T1 v1;
    private  T2 v2;
    
    public Pair(){
        ;
    }
    
    public Pair(T1 v1, T2 v2) {
        this.v1 = v1;
        this.v2 = v2;
    }

    public T1 getV1() {
        return v1;
    }
    
    public T1 getFirst() {
        return v1;
    }
    
    public void setV1(T1 v1) {
        this.v1 = v1;
    }
    
    public void setFirst(T1 v1) {
        this.v1 = v1;
    }
    
    public T2 getV2() {
        return v2;
    }
    
    public T2 getSecond() {
        return v2;
    }    
    
    public void setV2(T2 v2) {
        this.v2 = v2;
    }   
    
    public void setSecond(T2 v2) {
        this.v2 = v2;
    }
    
    public String toString()
    {
        return (new StringBuilder()).append("(").append(v1).append(", ").append(v2).append(")").toString();
    }
}
