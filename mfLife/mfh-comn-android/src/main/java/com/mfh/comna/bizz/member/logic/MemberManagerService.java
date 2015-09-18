package com.mfh.comna.bizz.member.logic;

import android.content.Intent;
import android.text.TextUtils;

import com.mfh.comna.comn.logic.AsyncTaskCallBack;
import com.mfh.comna.comn.logic.BaseService;
import com.mfh.comna.comn.logic.DataSyncStrategy;
import com.mfh.comna.bizz.member.MemberConstants;
import com.mfh.comna.bizz.member.dao.MemberManagerDao;
import com.mfh.comna.bizz.member.dao.MemberManagerNetDao;
import com.mfh.comna.bizz.member.entity.SubdisManager;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2014/10/22.
 * 小区管理人
 */
public class MemberManagerService extends BaseService<SubdisManager, String, MemberManagerDao>{

    private MemberManagerNetDao netDao = new MemberManagerNetDao();

    @Override
    protected Class<MemberManagerDao> getDaoClass() {
        return MemberManagerDao.class;
    }

    @Override
    public DataSyncStrategy getDataSyncStrategy() {
        return null;
    }


    public void queryManagerHouseNumber(Long userId, String subdisId) {
        netDao.queryManagerHouseNumber(userId, subdisId, new AsyncTaskCallBack<String>() {
            @Override
            protected void doSuccess(String rawValue) {
                try {
                    JSONObject jsonObject = new JSONObject(rawValue).getJSONObject("data");
                    String number = jsonObject.getString("val");
                    if (!TextUtils.isEmpty(number)) {
                        Intent intent = new Intent(MemberConstants.ACTION_MANAGER_QUERY_FINISH);
                        intent.putExtra("number", number);
                        getContext().sendBroadcast(intent);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
