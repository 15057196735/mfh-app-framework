package com.mfh.comna.bizz.member.entity;

import com.mfh.comna.comn.bean.ILetterIndexAble;
import com.mfh.comna.bizz.member.MemberConstants;

import java.io.Serializable;
import java.util.Date;

/**
 * 人的基本信息
 * Created by zhangyz on 14-5-22.
 */
public class Human implements Serializable, Ihuman, ILetterIndexAble<Long>{
    private Long id;
    private String name;
    private String cardNo;
    private Integer sex; //**性别  1=女，0=男,-1=未知'*/
    private String mobile;
    private String email ;
    private String remark = "";
    private String guid;
    private String address;///**地址，格式：小区+短地址+姓名*/
    private String signname = null;//签名
    private Integer sftype;//身份
    private Long userId;////关联的userId，
    private String headimage = "";//头像地址
    private String subdisId;
    private String subdisName;
    private Long ownerId = null;//属于谁的，客户端专有字段
    private String letterIndex = null;//额外添加的，用于分组显示
    private Date updatedDate; //修改日期
    private Integer houseNumber; //业主的楼栋号

    /*小区编号*/
    //private String SubdisId

     @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String getGuid() {
        if (guid == null)//有数据可能为空
            return id.toString();
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSignname() {
        return signname;
    }

    /**
     * 获取真正的分组索引字串
     * @return
     */
    public String genLastLetterIndex() {
        if (MemberConstants.PO.equals(getSftype()))
            return getSignname().substring(0, getSignname().lastIndexOf("-"));
        else
            return this.getLetterIndex();
    }

    /**
     * 生成分隔显示用的分隔标签
     * @return
     */
    public String genSeperatorLabel() {
        String labelCaption = this.getLetterIndex().substring(2);//把1-、2-等去掉。
        if (MemberConstants.PO.equals(getSftype())) {
            labelCaption = getSignname();
            int index = labelCaption.lastIndexOf("-");
            if (index > 0)
                labelCaption = labelCaption.substring(0, index);
        }
        return labelCaption;
    }

    public void setSignname(String signname) {
        this.signname = signname;
    }

    public Integer getSftype() {
        return sftype;
    }

    public void setSftype(Integer sftype) {
        this.sftype = sftype;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public String getHeadimage() {
        return headimage;
    }

    public void setHeadimage(String headimage) {
        this.headimage = headimage;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public String getLetterIndex() {
        return letterIndex;
    }

    public void setLetterIndex(String letterIndex) {
        this.letterIndex = letterIndex;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String getSubdisId() {
        return subdisId;
    }

    public void setSubdisId(String subdisId) {
        this.subdisId = subdisId;
    }

    public Integer getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(Integer houseNumber) {
        this.houseNumber = houseNumber;
    }

    @Override
    public String toString() {
        return name;
    }

    public String getSubdisName() {
        return subdisName;
    }

    public void setSubdisName(String subdisName) {
        this.subdisName = subdisName;
    }
}
