package com.mfh.comna.bizz.member.entity;

import com.mfh.comn.bean.IObject;

/**
 * Created by Administrator on 2014/10/22.
 * 小区物业专门的一张表
 */
public class SubdisManager implements IObject<String>{

    private String id;
    private String name;
    private String subdisName;
    private String subdisId;
    private Long ownerId;
    private String signname;
    private String headimage;
    private Long humanId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSubdisId() {
        return subdisId;
    }

    public void setSubdisId(String subdisId) {
        this.subdisId = subdisId;
    }

    public String getSubdisName() {
        return subdisName;
    }

    public void setSubdisName(String subdisName) {
        this.subdisName = subdisName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public String getSignname() {
        return signname;
    }

    public void setSignname(String signname) {
        this.signname = signname;
    }

    public String getHeadimage() {
        return headimage;
    }

    public void setHeadimage(String headimage) {
        this.headimage = headimage;
    }

    public Long getHumanId() {
        return humanId;
    }

    public void setHumanId(Long humanId) {
        this.humanId = humanId;
    }
}
