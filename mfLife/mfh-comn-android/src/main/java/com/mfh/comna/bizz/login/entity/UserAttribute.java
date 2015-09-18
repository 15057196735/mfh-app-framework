package com.mfh.comna.bizz.login.entity;

/**
 *
 "userAttribute": {
     "mobile": "15250065084",
     "guid": "245389",
     "humanid": 245389,
     "ownerId": null,
     "humanName": "冰珊孤雪",
     "headimage": "33c49435faec068197c4e06f0e5d4eb7.jpg"
 }
 * */
public class UserAttribute implements java.io.Serializable {

    private String mobile = "";
    private String guid = "";
    private String humanId = "";
    private String ownerId = "";
    private String humanName = "";
    private String headimage = "";
    private String sex = "";//1=女，0=男,-1=未知

//    private String loginType = "";
//	// 物业
//	private String pmcName = "";
//	private String subdisIds = "";
//	private String curSubdisId = "";
//	/** 微信服务号 */
//	private String curWxsuId = "";
//	private String logopic = "";
//	private String pmcLevel = "";
//	private String mySubdisIds = "";
//    private String moduleNames = "";
//    private String cpid = "";
//    private List<SubdisList> subdisList;
//    private String spid = "";



//	public String getMySubdisIds() {
//		return mySubdisIds;
//	}
//
//	public void setMySubdisIds(String mySubdisIds) {
//		this.mySubdisIds = mySubdisIds;
//	}
//
//	public String getLogopic() {
//		return logopic;
//	}
//
//	public void setLogopic(String logopic) {
//		this.logopic = logopic;
//	}
//
//	public String getPmcName() {
//		return pmcName;
//	}
//
//	public void setPmcName(String pmcName) {
//		this.pmcName = pmcName;
//	}
//
//	public String getSubdisIds() {
//		return subdisIds;
//	}
//
//	public void setSubdisIds(String subdisIds) {
//		this.subdisIds = subdisIds;
//	}
//
//	public String getCurSubdisId() {
//		return curSubdisId;
//	}
//
//	public void setCurSubdisId(String curSubdisId) {
//		this.curSubdisId = curSubdisId;
//	}
//
//	public String getPmcLevel() {
//		return pmcLevel;
//	}
//
//	public void setPmcLevel(String pmcLevel) {
//		this.pmcLevel = pmcLevel;
//	}
//
//	public String getCurWxsuId() {
//		return curWxsuId;
//	}
//
//	public void setCurWxsuId(String curWxsuId) {
//		this.curWxsuId = curWxsuId;
//	}
//
//	public String getLoginType() {
//		return loginType;
//	}
//
//	public void setLoginType(String loginType) {
//		this.loginType = loginType;
//	}
//
//    public String getCpid() {
//        return cpid;
//    }
//
//    public void setCpid(String cpid) {
//        this.cpid = cpid;
//    }
//
//    public String getModuleNames() {
//        return moduleNames;
//    }
//
//    public void setModuleNames(String moduleNames) {
//        this.moduleNames = moduleNames;
//    }
//
//    public List<SubdisList> getSubdisList() {
//        return subdisList;
//    }
//
//    public void setSubdisList(List<SubdisList> subdisList) {
//        this.subdisList = subdisList;
//    }
//public String getSpid() {
//    return spid;
//}
//
//    public void setSpid(String spid) {
//        this.spid = spid;
////    }

    public String getHumanName() {
        return humanName;
    }

    public void setHumanName(String humanName) {
        this.humanName = humanName;
    }



    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getHumanId() {
        return humanId;
    }

    public void setHumanId(String humanId) {
        this.humanId = humanId;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getHeadimage() {
        return headimage;
    }

    public void setHeadimage(String headimage) {
        this.headimage = headimage;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }
}