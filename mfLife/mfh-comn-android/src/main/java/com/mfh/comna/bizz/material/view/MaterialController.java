package com.mfh.comna.bizz.material.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.mfh.comna.api.helper.UIHelper;
import com.mfh.comna.bizz.msg.MsgConstants;
import com.mfh.comna.R;
import com.mfh.comna.comn.logic.ServiceFactory;
import com.mfh.comna.utils.CameraSessionUtil;

/**
 * 聊天用到的素材类控制面板
 * Created by Shicy on 14-3-20.
 */
public class MaterialController  extends FrameLayout implements View.OnClickListener {

    private String[] pictureMenus = new String[]{"拍照", "从相册选择", "图片素材库"};
    //private Uri cameraImgUrl = null;//用来存储相机拍照的文件url，临时变量
    private CameraSessionUtil cameraUtil = ServiceFactory.getService(CameraSessionUtil.class.getName());

    public MaterialController(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.initView(context);
    }

    /**
     * 获取相机工具类对象
     * @return
     */
    public CameraSessionUtil getCameraUtil() {
        return cameraUtil;
    }

    private void initView(Context context) {
        View view =  LayoutInflater.from(context).inflate(R.layout.subview_material_controller, this, true);

        view.findViewById(R.id.chat_material_cyy).setOnClickListener(this);//常用语
        view.findViewById(R.id.chat_material_tp).setOnClickListener(this); //图片
        view.findViewById(R.id.chat_material_tw).setOnClickListener(this); //图文
        view.findViewById(R.id.chat_material_mp).setOnClickListener(this); //名片
    }

    @Override
    public void onClick(View view) {
        Activity context = (Activity)getContext();
        if (view.getId() == R.id.chat_material_cyy) {
//            context.startActivityForResult(new Intent(context, MaterialCyyActivity.class), MsgConstants.CODE_REQUEST_CYY);
            context.startActivity(new Intent(context, MaterialCyyActivity.class));
        }
        else if (view.getId() == R.id.chat_material_tp) {
            this.showPictureMenus(context);
        }
        else if (view.getId() == R.id.chat_material_tw) {
            context.startActivity(new Intent(context, MaterialTwActivity.class));
        }
        else if (view.getId() == R.id.chat_material_mp) {
            //context.startActivity(new Intent(context, MemberListActivity.class));
            Toast.makeText(context, "此功能尚未开放", Toast.LENGTH_SHORT).show();
        }
    }


    //选择图片
    private void showPictureMenus(final Activity context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final MaterialController that = this;
        builder.setSingleChoiceItems(pictureMenus, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                if (i == 0) { //拍照
                    cameraUtil.makeCameraRequest(context);
                }
                else if (i == 1) { //从相册选择
                    context.startActivityForResult(new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI),
                            UIHelper.REQUEST_CODE_XIANGCE);
                }
                else if (i == 2) { //图片素材库
                    context.startActivityForResult(new Intent(context, MaterialTpActivity.class),
                            MsgConstants.CODE_REQUEST_MATERIAL_LIB);
                }
            }
        });
        builder.show();
    }

}
