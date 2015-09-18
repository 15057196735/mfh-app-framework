package com.mfh.comna.bizz.member;

import com.mfh.comn.code.impl.CodeService;

/**
 * 通讯录相关常量
 * Created by Administrator on 14-5-23.
 */
public class MemberConstants {
    public static Integer ADMIN = 1;
    public static Integer PMC   = 2;
    public static Integer PO 	 = 3;
    public static Integer FD 	 = 4;
    public static Integer SU = 7;
    public static Integer MFH = 13;

    static public void initCode(){
        CodeService.addCodeHouse("humanType")
                .addOption(PMC, "物业人员")
                .addOption(PO, "业主")
                .addOption(FD, "快递服务商")
                .addOption(SU, "服务提供商")
                .addOption(MFH, "满分工作人员")
                .addOption(ADMIN, "管理员");
    }

    public static final String ACTION_MEMBER_NEW = "action.member.receiveNew";//下载到新的通讯录
    public static final String ACTION_MEMBER_REFRESH = "action.member.refresh";//刷新通讯录
    public static final String ACTION_MEMBER_ERROR = "action.member.error";//下载到新的通讯录

    public static final String ACTION_MEMBER_OWNER_ERROR = "action.member.owner.error";//下载到新的通讯录
    public static final String ACTION_MEMBER_OWNER_NEW = "action.member.owner.receiveNew";//下载到新的通讯录

    public static final String ACTION_MEMBER_SERVICE = "action.member.service";//人员相关服务列表信息
    public static final String ACTION_MEMBER_SELECTED = "action.member.selected";//人员被选中信息
    public static final String ACTION_MEMBER_UNSELECTED = "action.member.unselected";//人员被选中信息

    public static final String ACTION_MEMBER_ADDED = "action.member.added";//添加人员信息
    public static final String ACTION_MEMBER_DELETED = "action.member.delete";//移除人员信息

   //类名字符串
    public static final String PMB_MEMBER_DETAIL_ACTIVITY_CLASS_NAME = "msg_pmc_member";//PmbMemberDetailActivity

    //人员类型字符串,前面加上0，1,2,3等是为了便于按这些分组排序
    public static final String MEMBER_TYPE_PROPERTY = "0-物业";
    public static final String MEMBER_TYPE_BUSINESS = "2-商家";
    public static final String MEMBER_TYPE_MAN_FENG = "1-满分";
    public static final String MEMBER_TYPE_OWNER = "3-业主";

    public final static String RELEASE_GROUP_FINISH = "release.group.finish";//全部解散成功
    public final static String RELEASE_GROUP_START = "release.group.start";//全部解散成功

    public final static String ACTION_MANAGER_QUERY_FINISH = "member.query.finish";

    //服务类型
    public static final Integer SERVICE_TYPE_TS = 0;//投诉
    public static final Integer SERVICE_TYPE_WX = 1; //维修
    public static final Integer SERVICE_TYPE_ORDER = 2; //订单
}
