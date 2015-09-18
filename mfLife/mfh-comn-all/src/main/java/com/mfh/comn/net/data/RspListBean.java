package com.mfh.comn.net.data;

import java.util.ArrayList;
import java.util.List;

/**
 * bean的list对象
 * Created by Administrator on 14-5-21.
 */
public class RspListBean<T> implements IResponseData{
    private List<T> value;

    public RspListBean(List<T> value) {
        super();
        this.value = value;
    }

    public RspListBean() {
        super();
        this.value = new ArrayList<T>();
    }

    /**
     * 增加一个bean
     * @param bean
     */
    public void addBean(T bean) {
        this.value.add(bean);
    }

    public List<T> getValue() {
        return value;
    }
}
