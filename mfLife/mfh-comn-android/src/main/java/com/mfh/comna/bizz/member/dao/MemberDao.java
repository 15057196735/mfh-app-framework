package com.mfh.comna.bizz.member.dao;

import android.text.TextUtils;

import com.mfh.comn.bean.PageInfo;
import com.mfh.comn.bean.Pair;
import com.mfh.comna.R;
import com.mfh.comna.comn.bean.KvBean;
import com.mfh.comna.bizz.member.MemberConstants;
import com.mfh.comna.bizz.member.entity.Human;
import com.mfh.comna.comn.database.dao.BaseDbDao;

import java.util.ArrayList;
import java.util.List;

/**
 * 通讯录数据库dao
 * Created by Administrator on 14-5-22.
 */
public class MemberDao extends BaseDbDao<Human, Long> {
    @Override
    protected Pair<String, String> initTableChName() {
        return new Pair<String, String>("通讯录表", "emb_member");
    }

    @Override
    protected Class<Human> initPojoClass() {
        return Human.class;
    }

    @Override
    protected Class<Long> initPkClass() {
        return Long.class;
    }

    /**
     * 清理当前用户的所有通讯录信息
     * @param userId
     */
    public void clear(Long userId) {
        this.getDb().deleteByWhere(Human.class, "ownerId=" + userId.toString());
    }

    /**
     * 获取当前登录用户的通讯录个数
     * @return
     */
    public Integer getCountByOwnerId(Long userId) {
        return this.getDb().findCount(Human.class, "ownerId=" + userId.toString());
    }

    /**
     * 生成where语句
     * @param
     * @param searchToken 查询token
     * @return
     */
    private String genWhereSql(Long ownerId, String searchToken) {
        String sql = "ownerId = " + ownerId;
        if (searchToken != null && searchToken.length() > 0) {
            searchToken = "%" + searchToken + "%";
            sql += " and (name like '" + searchToken
                    + "' or address like '" + searchToken + "'"
                    + "' or signname like '" + searchToken + "')";
        }
        return sql;
    }

    /**
     * 查询指定session下的消息类比，按照逆序
     * @param ownerId
     * @param searchToken 搜索串
     * @param pageInfo
     * @return
     */
    public List<Human> queryMemsByOwnerId(Long ownerId, String searchToken, PageInfo pageInfo) {
        String sql = genWhereSql(ownerId, searchToken);
        return getDb().findAllByWhere(Human.class, sql, "letterIndex, signname", pageInfo);//"id desc"
    }

    /**
     * 根据名字模糊查询
     * */
    public List<Human> getSearchList(String name,Long ownerId,String subdisId,PageInfo pageInfo) {
        String where = "subdisId = " + subdisId + " and ownerId = " + ownerId + " and letterIndex = '" + getContext().getString(R.string.member_owner) + "'";
        if (!TextUtils.isEmpty(name)) {
            where += " and name like '%" + name +"%'";
        }
        return getDb().findAllByWhere(Human.class, where, "houseNumber,signname", pageInfo);
    }

    public List<Human> getListForSearch(String searchToken, Long ownerId, PageInfo pageInfo) {
        if (searchToken != null && !"".equals(searchToken)) {
            String where ="ownerId = " + ownerId + " and name like '%" + searchToken + "%'";
            return getDb().findAllByWhere(Human.class,where,"houseNumber,signname", pageInfo);
        }else {
            List<Human> objects = new ArrayList<Human>();
            new KvBean<Human>("通讯录没有改关键字相关项");
            objects.add(new KvBean<Human>("通讯录没有改关键字相关项").getBean());
            return objects;
        }
    }

    /**
     * 根据名字模糊查询
     * */
    public List<Human> getSearchListByletterIndex(String name, Long ownerId, String letterIndex, PageInfo pageInfo) {
        String where = "letterIndex = '" + letterIndex + "' and ownerId = " + ownerId;
        if (!TextUtils.isEmpty(name)) {
            where += " and name like '%" + name +"%'";
        }
        return getDb().findAllByWhere(Human.class,where,"signname",pageInfo);
    }

    public List<Human> getAllOwner() {
        String where = "sftype = " + MemberConstants.PO + " and houseNumber is null" ;
        return getDb().findAllByWhere(Human.class, where);
    }

    /**
     * 查找出业主以外所有的人
     * */
    public List<Human> getAllOutOwner(Long ownerId,PageInfo pageInfo) {
        String where = "ownerId = " + ownerId + " and letterIndex != '" + getContext().getString(R.string.member_owner) + "'";
        return getDb().findAllByWhere(Human.class,where,"letterIndex, signname" ,pageInfo);
    }
}
