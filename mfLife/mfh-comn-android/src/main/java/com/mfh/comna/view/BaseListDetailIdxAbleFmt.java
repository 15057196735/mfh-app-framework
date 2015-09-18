/*
 * 文件名称: BaseListDetailIdxbleFmt.java
 * 版权信息: Copyright 2013-2014 chunchen technology Co., LTD. All right reserved.
 * ----------------------------------------------------------------------------------------------
 * 修改历史:
 * ----------------------------------------------------------------------------------------------
 * 修改原因: 新增
 * 修改人员: zhangyz
 * 修改日期: 2014-3-12
 * 修改内容: 
 */
package com.mfh.comna.view;

import java.util.List;
import android.view.View;
import android.view.ViewGroup;
import com.mfh.comn.bean.EntityWrapper;
import com.mfh.comn.bean.PageInfo;
import com.mfh.comn.net.data.RspQueryResult;
import com.mfh.comna.comn.bean.GroupKeyValue;
import com.mfh.comna.comn.bean.KvBean;
import com.mfh.comna.comn.database.dao.NetProcessor;

/**
 * 用于显示一个bean的详细信息。
 * 详细信息分成多行显示；一行显示一个属性，一般分成两列，左列是属性名（中文名），右边是属性值。
 * 使用时需要
 * 1、先调用setDetailInfo(...)设置数据，
 * 2、然后继承实现fillComnListItemView(....)方法
 * 3、实现collectIndexLetter()提供分组字母集,缺省为A、B、C、D....
 * @author zhangyz created on 2014-3-12
 */
public abstract class BaseListDetailIdxAbleFmt extends BaseListIdxAbleFmt<GroupKeyValue>{
    private List<GroupKeyValue> detailInfo = null;
    
    /**
     * 设置要显示的信息，key-value列表。
     * @param detailInfo
     * @author zhangyz created on 2014-3-12
     */
    public void setDetailInfo(List<GroupKeyValue> detailInfo) {
        this.detailInfo = detailInfo;
    }
    
    @Override
    protected boolean isAsyncDao() {
        return false;
    }

    /**
     * 绘制一个普通的信息条目（一般两列，左边是属性名，右边是属性值）
     * @param kvValue
     * @param listItemView
     * @param parent
     * @author zhangyz created on 2014-3-12
     */
    protected abstract void fillComnListItemView(GroupKeyValue kvValue, 
            View listItemView, ViewGroup parent);
    
    @Override
    protected void fillComnListItemView(KvBean<GroupKeyValue> summary, 
            View listItemView, ViewGroup parent) {
        GroupKeyValue factValue = summary.getBean();
        fillComnListItemView(factValue, listItemView, parent);
    }

    protected List<KvBean<GroupKeyValue>> readListPageData(PageInfo pageInfo, 
            NetProcessor.QueryRsProcessor<GroupKeyValue> callBack) {
        return readListPageData(null, pageInfo, callBack);
    }

    @Override
    protected List<KvBean<GroupKeyValue>> readListPageData(String searchToken, 
            PageInfo pageInfo, NetProcessor.QueryRsProcessor<GroupKeyValue> callBack) {
        if (callBack != null) {
            RspQueryResult<GroupKeyValue> rs = prepareData(searchToken);
            callBack.processQueryResult(rs);
            return null;
        }
        else {
            RspQueryResult<GroupKeyValue> rs = prepareData(searchToken);
            List<KvBean<GroupKeyValue>> kvs = KvBean.exportToKvs(rs.getRowDatas());
            return kvs;
        }
    }  

    /**
     * 准备数据
     * @param token
     * @return
     * @author zhangyz created on 2014-3-12
     */
    private RspQueryResult<GroupKeyValue> prepareData(String token) {
        RspQueryResult<GroupKeyValue> rs = new RspQueryResult<GroupKeyValue>();
        if (detailInfo != null) {
            for (GroupKeyValue item : detailInfo) {
                if (token != null && token.length() > 0) {
                    if (token.equals(item.getKey()) || token.equals(item.getValueStr()))
                        continue;
                }
                rs.addRowItem(new EntityWrapper<>(item));
            }
            rs.setTotalNum(detailInfo.size());
        }
        else
            throw new RuntimeException("没有传递要显示的数据，先调用setDetailInfo");
        return rs;
    }

}
