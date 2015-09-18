package com.mfh.comna.bizz.material;

import com.alibaba.fastjson.JSON;
import com.mfh.comna.bizz.material.entity.MsgAttr;
import com.mfh.comna.bizz.material.entity.ResourceData;
import com.mfh.comna.bizz.msg.entity.DataItem;
import com.mfh.comna.bizz.msg.entity.ImageTextParam;
import com.mfh.comna.comn.database.dao.FileNetDao;
import com.mfh.comna.view.img.FineImgView;

import java.util.ArrayList;
import java.util.List;

/**
 * 素材工具类
 * Created by Administrator on 14-5-19.
 */
public class ResourceUtil {

    /**
     * 获取多个属性
     * @param rsData
     * @return
     */
    public List<MsgAttr> getMsgAttrList(ResourceData rsData) {
        return getMsgAttrList(rsData.getType(), rsData.getParam());
    }

    public List<MsgAttr> getMsgAttrList(int type, String param) {
        List<MsgAttr> msgAttrList = null;
        switch(type){
            case 0://文本
                MsgAttr ma = JSON.parseObject(param, MsgAttr.class);
                msgAttrList = new ArrayList<MsgAttr>();
                msgAttrList.add(ma);
                break;
            case 1://图文
                msgAttrList = JSON.parseArray(param, MsgAttr.class);
                break;
            case 2://图片
            case 3://音频
            case 4://视频
            default:
                MsgAttr mat = JSON.parseObject(param, MsgAttr.class);
                msgAttrList = new ArrayList<MsgAttr>();
                msgAttrList.add(mat);
        }
        return msgAttrList;
    }

    /**
     * 获取单个属性
     * @return
     */
    public MsgAttr getMsgAttr(ResourceData rsData) {
        String param = rsData.getParam();
        switch(rsData.getType()){
            case 0://文本
                return JSON.parseObject(param, MsgAttr.class);
            case 1://图文
                List<MsgAttr> msgAttrList = JSON.parseArray(param, MsgAttr.class);
                return msgAttrList.get(0);
            case 2://图片
            case 3://音频
            case 4://视频
            default:
                return JSON.parseObject(param, MsgAttr.class);
        }
    }

    public static String MAT_IMG_DIR = "materialImgDir";
    /**
     * 获取素材图像的文件访问dao对象
     * @return
     */
    public static FileNetDao getMatImgFao() {
        return FineImgView.getFao(null, MAT_IMG_DIR);
    }

    /**
     * 转换
     * @param param
     * @return
     */
    public static List<MsgAttr> toMsgAttrs(ImageTextParam param) {
        List<DataItem> items = param.getData();
        if (items == null)
            return null;
        List<MsgAttr> ret = new ArrayList<MsgAttr>();
        for (DataItem item : items) {
            ret.add(item.toMsgAttr());
        }
        return ret;
    }
}
