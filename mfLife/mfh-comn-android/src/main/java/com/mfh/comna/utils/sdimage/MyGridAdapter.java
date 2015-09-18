package com.mfh.comna.utils.sdimage;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2014/10/21.
 * 工单界面展示的GridAdapter
 */
public class MyGridAdapter extends GridAdapter{

    public static final int REQUEST_CODE_LISTIMAGEACTIVITY = 101;
    public static final int REQUEST_CODE_EDITIMAGEACTIVITY = 102;
    private AlertDialog.Builder builder;

    public MyGridAdapter(Activity context, List<Image> images, List<String> checkImages) {
        super(context, images, checkImages);
        builder = new AlertDialog.Builder(context);
    }

    @Override
    protected void fillData(final int position, ViewHolder holder) {
        super.fillData(position, holder);
        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position == 0) {
                    Intent intent = new Intent(context, ListImageActivity.class);
                    context.startActivityForResult(intent, REQUEST_CODE_LISTIMAGEACTIVITY);
                }
                else {
                    Intent intent = new Intent(context, ZoomImageActivity.class);
                    intent.putExtra("imagePath", images.get(position - 1).getPath());
                    context.startActivity(intent);
                }
            }
        });
        if (position != 0) {
            holder.image.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    builder.setItems(
                            new String[]{"删除", "编辑"}, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    switch (which) {
                                        case 0:
                                            images.remove(images.get(position - 1));
                                            MyGridAdapter.this.notifyDataSetChanged();
                                            break;
                                        case 1:
                                            Intent intent = new Intent(context,EditImageActivity.class);
                                            Bundle bundle = new Bundle();
                                            bundle.putSerializable(DATA_IMAGE, (Serializable) images);
                                            intent.putExtras(bundle);
                                            context.startActivityForResult(intent, REQUEST_CODE_EDITIMAGEACTIVITY);
                                            break;
                                    }
                                }
                            }).setTitle("图片操作").create().show();
                    return true;
                }
            });
        }
        holder.imageCb.setVisibility(View.GONE);
    }

    public void onResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_LISTIMAGEACTIVITY) {
            if (data == null || data.getExtras() == null)
                return;
            Bundle bundle = data.getExtras();
            List<String> images = (List<String>) bundle.getSerializable(DATA_IMAGE);
            for (String path : images) {
                MyGridAdapter.this.addData(new Image(path));
            }
            MyGridAdapter.this.notifyDataSetChanged();
        }else if(requestCode == REQUEST_CODE_EDITIMAGEACTIVITY){
            if (data == null || data.getExtras() == null)
                return;
            Bundle bundle = data.getExtras();
            List<String> images = (List<String>) bundle.getSerializable(DATA_IMAGE);
            MyGridAdapter.this.removeData(images);
            MyGridAdapter.this.notifyDataSetChanged();
        }
    }

    /**
     * 将gridview中的图片转化问文件
     * @return
     */
    public List<File> collectCameraFiles() {
        List<File> files = null;
        for (int ii = 0; ii < images.size(); ii++) {
            File file = new File(images.get(ii).getPath());
            if (files == null)
                files = new ArrayList<File>();
            files.add(file);
        }
        return files;
    }
}
