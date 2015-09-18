package com.mfh.comna.bizz.member.logic;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;

import com.mfh.comna.api.helper.AppHelper;
import com.mfh.comna.bizz.login.LoginService;
import com.mfh.comna.bizz.login.logic.MfhLoginService;
import com.mfh.comn.bean.EntityWrapper;
import com.mfh.comn.bean.PageInfo;
import com.mfh.comn.net.data.RspQueryResult;
import com.mfh.comna.comn.database.dao.CursorUtil;
import com.mfh.comna.comn.database.dao.NetProcessor;
import com.mfh.comna.comn.logic.BaseService;
import com.mfh.comna.comn.logic.DataSyncStrategy;
import com.mfh.comna.comn.logic.MultiTaskWithPageSimple;
import com.mfh.comna.comn.logic.MultiTaskWithPageSimple.QueryAsyncTask;
import com.mfh.comna.comn.logic.MyAsyncTask;
import com.mfh.comna.comn.logic.ServiceFactory;
import com.mfh.comna.comn.logic.TwoLayerStrategy;
import com.mfh.comna.network.NetFactory;
import com.mfh.comna.utils.DialogUtil;
import com.mfh.comna.utils.MapUtils;
import com.mfh.comna.bizz.member.MemberConstants;
import com.mfh.comna.bizz.member.dao.MemberDao;
import com.mfh.comna.bizz.member.dao.MemberNetDao;
import com.mfh.comna.bizz.member.entity.Human;
import com.mfh.comna.bizz.member.entity.SubdisManager;


import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 通讯录服务表
 * Created by Administrator on 14-5-22.
 */
public class MemberService extends BaseService<Human, Long, MemberDao> {
    protected MfhLoginService ls = MfhLoginService.get();
    protected MemberNetDao netDao = new MemberNetDao();
    protected MemberSetUtil setUtil = null;
    protected MemberManagerService service =  ServiceFactory.getService(MemberManagerService.class, getContext());;


    @Override
    protected Class<MemberDao> getDaoClass() {
        return MemberDao.class;
    }

    @Override
    public DataSyncStrategy getDataSyncStrategy() {
        DataSyncStrategy dataSyncStrategy = new TwoLayerStrategy() {
            @Override
            public void syncDataFromOne_Two() {
                if (ls != null && ls.getLoginName() != null)
                    setUtil =  new MemberSetUtil(ls.getLoginName());
                else
                    return;
                String cursorValue = setUtil.getLastUpdate();
                List<EntityWrapper<Human>> humans = new ArrayList<EntityWrapper<Human>>();
                List<EntityWrapper<Human>>  temp;

                temp = netDao.listAllEmpHumanIdOfPmcId(ls.getUserId(), "");
                if (temp != null) humans.addAll(temp);

                temp = netDao.getCompanyList(ls.getUserId(), "");
                if (temp != null) humans.addAll(temp);

                temp = netDao.getSellSupportsByPowner(ls.getMySubdisIds(), "");
                if (temp != null) humans.addAll(temp);

                saveMember(humans);

                if (!TextUtils.isEmpty(ls.getMySubdisIds())) {
                    String[] sbudiss = ls.getMySubdisIds().split(",");
                    for (String s : sbudiss) {
                        temp = netDao.getPownerOfApartManager(ls.getUserId(), s, "", cursorValue);
                        saveMember(temp);
                    }
                }
            }

        };
        return dataSyncStrategy;
    }

    protected void saveMember( List<EntityWrapper<Human>> humans) {
        dao.beginTransaction();
        for (EntityWrapper<Human> entityWrapper : humans) {
            Human human = entityWrapper.getBean();
            human.setOwnerId(ls.getUserId());
            if (human.getSftype() == null || human.getSftype() == 0)
                human.setSftype(MemberConstants.MFH);
            if (MemberConstants.PMC.equals(human.getSftype())) {
                human.setLetterIndex(MemberConstants.MEMBER_TYPE_PROPERTY);//物业
                SubdisManager manager;
                if (!TextUtils.isEmpty(human.getSubdisId())) {  //将小区业主存放到另外一张表中
                    String[] subdisIds = human.getSubdisId().split(",");
                    String[] subdisNames = human.getSubdisName().split(",");
                    for (int i = 0; i < subdisIds.length; i++) {
                        manager = new SubdisManager();
                        manager.setOwnerId(ls.getUserId());
                        manager.setId(human.getName() + "-" + subdisIds[i] + "-" + subdisNames[i]);
                        manager.setName(human.getName());
                        manager.setSubdisId(subdisIds[i]);
                        manager.setSubdisName(subdisNames[i]);
                        manager.setHumanId(human.getId());
                        service.getDao().saveOrUpdate(manager);
                    }
                }else {
                    manager = new SubdisManager();
                    manager.setOwnerId(ls.getUserId());
                    manager.setId(human.getName());
                    manager.setName(human.getName());
                    manager.setSubdisId("");
                    manager.setSubdisName("总部");
                    manager.setHumanId(human.getId());
                    service.getDao().saveOrUpdate(manager);
                }
            }
            else if (MemberConstants.SU.equals(human.getSftype())) {
                human.setLetterIndex(MemberConstants.MEMBER_TYPE_BUSINESS);//商家
            }
            else if (MemberConstants.MFH.equals(human.getSftype()))
                human.setLetterIndex(MemberConstants.MEMBER_TYPE_MAN_FENG);//满分
            else if (MemberConstants.PO.equals(human.getSftype())) {
                //此处应设置到幢，可以考虑根据当前登录楼管管辖的幢列表去匹配进行分组
                human.setLetterIndex(MemberConstants.MEMBER_TYPE_OWNER);//业主
                solveOwnerMember(ls.getSubdisNames().split(","),human);
//                  获取最大游标
                Date curDate = human.getUpdatedDate();
                setUtil.nextRecordCurosr(curDate != null ? CursorUtil.dateFormat.format(curDate) : null);
            }
            dao.saveOrUpdate(human);
        }
        dao.setTransactionSuccessful();
        dao.endTransaction();
        setUtil.commitCursor();
    }

    /**
     * 清理当前用户的通讯录
     */
    public void clear() {
        if (ls != null && ls.getLoginName() != null)
            setUtil = new MemberSetUtil(ls.getLoginName());
        else
            return;
        setUtil.clearLastUpdate();
        Long userId = ls.getUserId();
        if (userId == null)
            return;
        dao.clear(userId);
        service.getDao().clean(userId);
    }

    /**
     * 执行下载通讯录过程，并保存到本地数据库
     * @param userId 楼管员userId
     * @param subdisIds 小区Id列表
     */
    public void queryFromNet(Long userId, final String subdisIds) {
        Map<String, Object> params = MapUtils.genMap("userId", userId, "subdisIds", subdisIds);
        Iterator<Integer> iter = MapUtils.genList(1, 2, 3).iterator();

        MultiTaskWithPageSimple queryTask = new MultiTaskWithPageSimple<Human>(
                iter ,
            new QueryAsyncTask<Human>() {
                private int downCount = 0;//已下载个数
                @Override
                public void doOneTask(Map<String, Object> paramIn, Integer taskKind, NetProcessor.QueryRsProcessor<Human> callbackMethod) {
                    Long userId = (Long)paramIn.get("userId");
                    String subdisIds = (String)paramIn.get("subdisIds");

                    if (taskKind == 1) //1、同事
                        netDao.listAllEmpHumanIdOfPmcId(userId, callbackMethod);
                    else if (taskKind == 2)//2、商家
                        netDao.getSellSupportsByPowner(subdisIds, callbackMethod);
                    else if (taskKind == 3) { //满分
                        netDao.getCompanyList(userId,callbackMethod);
                    }
                    else
                        return;
                }

                @Override
                public void afterOneTask(RspQueryResult<Human> rs, Integer taskKind){
                    LoginService ls = ServiceFactory.getService(LoginService.class.getName());
                    List<EntityWrapper<Human>> beans = rs.getRowDatas();
                    for (EntityWrapper<Human> wb : beans) {
                        Human human = wb.getBean();
                        human.setOwnerId(ls.getUserId());
                        if (human.getSftype() == null || human.getSftype() == 0)
                            human.setSftype(MemberConstants.MFH);
                        if (MemberConstants.PMC.equals(human.getSftype())) {
                            human.setLetterIndex(MemberConstants.MEMBER_TYPE_PROPERTY);//物业
                            SubdisManager manager;
                            if (!TextUtils.isEmpty(human.getSubdisId())) {  //将小区业主存放到另外一张表中
                                String[] subdisIds = human.getSubdisId().split(",");
                                String[] subdisNames = human.getSubdisName().split(",");
                                for (int i = 0; i < subdisIds.length; i++) {
                                    manager = new SubdisManager();
                                    manager.setOwnerId(ls.getUserId());
                                    manager.setId(human.getName() + "-" + subdisIds[i] + "-" + subdisNames[i]);
                                    manager.setName(human.getName());
                                    manager.setSubdisId(subdisIds[i]);
                                    manager.setSubdisName(subdisNames[i]);
                                    manager.setHumanId(human.getId());
                                    service.getDao().saveOrUpdate(manager);
                                }
                            }else {
                                manager = new SubdisManager();
                                manager.setOwnerId(ls.getUserId());
                                manager.setId(human.getName());
                                manager.setName(human.getName());
                                manager.setSubdisId("");
                                manager.setSubdisName("总部");
                                manager.setHumanId(human.getId());
                                service.getDao().saveOrUpdate(manager);
                            }
                        }
                        else if (MemberConstants.SU.equals(human.getSftype())) {
                            human.setLetterIndex(MemberConstants.MEMBER_TYPE_BUSINESS);//商家
                        }
                        else if (MemberConstants.MFH.equals(human.getSftype()))
                            human.setLetterIndex(MemberConstants.MEMBER_TYPE_MAN_FENG);//满分

                        dao.saveOrUpdate(human);
                    }
                    downCount += beans.size();
                }

                @Override
                public void onError(Throwable ex) {
                    onFinishTasks();
                    logger.error(ex.getMessage(), ex);
                    DialogUtil.showMessage(getContext(), "通讯录下载同步出错,请稍候重试:" +  ex.getMessage());
                    Intent intent = new Intent(MemberConstants.ACTION_MEMBER_ERROR);
                    getContext().sendBroadcast(intent);
                }

                @Override
                public void onFinishTasks() {
//                    if (downCount > 0) {
//                        Intent intent = new Intent(MemberConstants.ACTION_MEMBER_NEW);
//                        getContext().sendBroadcast(intent);
//                    }
                    queryAllOwner();
                }
            },
            1000//最多下载1000条记录,三种通讯录，每种都允许1000个下载，其实主要是针对业主部分的,数量最多。
        );
        queryTask.startWork(params);
    }

    public void queryAllOwner() {
        PageInfo pageInfo = new PageInfo(1, 100000);
        if (ls != null && ls.getLoginName() != null)
            setUtil =  new MemberSetUtil(ls.getLoginName());
        else
            return;
        String cursorValue = setUtil.getLastUpdate();
        netDao.getPownerOfApartManager(ls.getUserId(),null,new NetProcessor.QueryRsProcessor<Human>(pageInfo) {
            @Override
            public void processQueryResult(RspQueryResult<Human> rs) {
                new SaveOwnerTask(false).execute(rs);
            }

            @Override
            protected void processFailure(Throwable t, String errMsg) {
                Intent intent = new Intent(MemberConstants.ACTION_MEMBER_ERROR);
                getContext().sendBroadcast(intent);
                super.processFailure(t, errMsg);
            }
        },cursorValue);
    }

    /**
     * 根据humanId从网上获取member
     * @param humanId
     */
    public void phoneOnTheInternet(Long humanId, final Activity that) {
        AjaxParams params = new AjaxParams();
        params.put("humanId", humanId + "");
        NetFactory.getHttp().post(NetFactory.getServerUrl() + "/sys/human/getHumanById", params, new AjaxCallBack<Object>() {
            @Override
            public void onSuccess(Object o) {
                super.onSuccess(o);
                try {
                    JSONObject object = new JSONObject(o.toString());
                    JSONObject jsonObject = object.getJSONObject("data");
                    String phone = jsonObject.getString("mobile");
                    AppHelper.callTel(that, phone);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

//    /**
//     * 根据小区编号下载小区业主
//     * */
//    public void queryOwnerBySubdisId(Long userId, String subdisId, final String subdisName) {
//        PageInfo pageInfo = new PageInfo(1, 100000);   //一次性下载完
//        if (ls != null && ls.getLoginName() != null)
//            setUtil =  new MemberSetUtil(ls.getLoginName(), subdisId);
//        else
//            return;
//        String cursorValue = setUtil.getLastUpdate();
//        netDao.getPownerOfApartManager(userId, subdisId, new QueryRsProcessor<Human>(pageInfo) {
//
//            @Override
//            public void processQueryResult(RspQueryResult<Human> rs) {
//                new SaveOwnerTask(false,subdisName).execute(rs);
//            }
//
//            @Override
//            protected void processFailure(Throwable t, String errMsg) {
//                Intent intent = new Intent(MemberConstants.ACTION_MEMBER_OWNER_ERROR);
//                getContext().sendBroadcast(intent);
//                super.processFailure(t, errMsg);
//            }
//        }, cursorValue);
//    }

    /**
     * 保存小区业主
     * */
    public class SaveOwnerTask extends MyAsyncTask<RspQueryResult<Human>, Void>{


        protected SaveOwnerTask(boolean showDialog) {
            super(showDialog);
        }

        @Override
        protected Void doInBackgroundInner(RspQueryResult<Human>... params) {
            List<EntityWrapper<Human>> beans = params[0].getRowDatas();
            setUtil.beginRecordCursor();
            dao.beginTransaction();
            for (EntityWrapper<Human> wb : beans) {
                Human human = wb.getBean();
                human.setOwnerId(ls.getUserId());
                if (MemberConstants.PO.equals(human.getSftype())) {
                    //此处应设置到幢，可以考虑根据当前登录楼管管辖的幢列表去匹配进行分组
                    human.setLetterIndex(MemberConstants.MEMBER_TYPE_OWNER);//业主
                    solveOwnerMember(ls.getSubdisNames().split(","),human);
//                  获取最大游标
                    Date curDate = human.getUpdatedDate();
                    setUtil.nextRecordCurosr(curDate != null ? CursorUtil.dateFormat.format(curDate) : null);
                    dao.saveOrUpdate(human);
                }
            }
            setUtil.commitCursor();
            dao.setTransactionSuccessful();
            dao.endTransaction();
            return null;
        }

        @Override
        protected void onPostExecuteInner(Void result, RspQueryResult<Human>... params) {
            Intent intent = new Intent(MemberConstants.ACTION_MEMBER_OWNER_NEW);
            getContext().sendBroadcast(intent);
        }

        @Override
        protected void doInBackgroundException(Throwable ex, RspQueryResult<Human>... params) {
            Intent intent = new Intent(MemberConstants.ACTION_MEMBER_ERROR);
            getContext().sendBroadcast(intent);
            super.doInBackgroundException(ex, params);
        }
    }

//    /**
//     * 将业主的楼栋号分隔出来
//     * */
//    private void solveOwnerMember(String subdisName, Human human) {
//        try {
//            if (TextUtils.isEmpty(human.getSignname()))
//                return;
//            if (human.getSignname().contains(subdisName)) {
//                String temp = human.getSignname().substring(subdisName.length(), human.getSignname().indexOf("-") + 1).trim();
//                if (temp.endsWith("-")) {
//                    temp = temp.substring(0, temp.length() - 1);
//                    for (int j = 0; j < temp.length(); j++) {
//                        if (temp.charAt(j) <= '9' && temp.charAt(j) >= '0') {
//                            continue;
//                        }
//                        temp = "0";
//                        break;
//                    }
//                }
//                human.setHouseNumber(Integer.parseInt(temp));
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

     /**
     * 将业主的楼栋号分隔出来
     * */
    private void solveOwnerMember(String[] subdisNames,Human human) {
        try {
            for (int i = 0; i < subdisNames.length; i++) {
                if (TextUtils.isEmpty(human.getSignname()))
                    continue;
                if (human.getSignname().contains(subdisNames[i])) {
                    String temp = human.getSignname().substring(subdisNames[i].length(), human.getSignname().indexOf("-") + 1).trim();
                    if (temp.endsWith("-")) {
                        temp = temp.substring(0, temp.length() - 1);
                        for (int j = 0; j < temp.length(); j++) {
                            if (temp.charAt(j) <= '9' && temp.charAt(j) >= '0') {
                                continue;
                            }
                            temp = "0";
                            break;
                        }
                    }
                    human.setHouseNumber(Integer.parseInt(temp));
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 获得可以成为受理人的人（基本都是物业的人员）
     * @return
     */
    public List<Human> getServiceHuman() {
        PageInfo pageInfo = new PageInfo();
        return getDao().getSearchListByletterIndex("", ls.getUserId(), MemberConstants.MEMBER_TYPE_PROPERTY, pageInfo);
    }

    public Intent getMemberDetailSkipIntent(Intent intent, Human bean) {
        intent.putExtra("humanId", bean.getId());
        return intent;
    }
}
