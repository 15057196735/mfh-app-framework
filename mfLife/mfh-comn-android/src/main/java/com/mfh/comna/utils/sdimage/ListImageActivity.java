package com.mfh.comna.utils.sdimage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.ActionBar;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;

import com.mfh.comna.api.helper.UIHelper;
import com.mfh.comna.R;
import com.mfh.comna.comn.logic.IBaseViewComponent;
import com.mfh.comna.comn.logic.ServiceFactory;
import com.mfh.comna.utils.CameraSessionUtil;
import com.mfh.comna.view.BaseComnActivity;

public class ListImageActivity extends BaseComnActivity {

    private ArrayList<Image> imageList;
    private GridView listview;
    private List<HashMap<String, String>> images;
    private CameraSessionUtil cameraSessionUtil = ServiceFactory.getService(CameraSessionUtil.class.getName());
    private Button save;
    private List<String> checkImageList;

    @Override
    public int getLayoutId() {
        return R.layout.activity_list_image;
    }

    @Override
    protected void initActionBar(ActionBar actionBar) {
        super.initActionBar(actionBar);
        actionBar.setIcon(R.drawable.white_logo);
        actionBar.setTitle("选择图片");
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(R.layout.list_image_action);
        save = (Button) actionBar.getCustomView().findViewById(R.id.service_button_add);
        save.setText("确定");
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putSerializable(GridAdapter.DATA_IMAGE, (Serializable) checkImageList);
                intent.putExtras(bundle);
                setResult(2, intent);
                finish();
            }
        });
    }

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

        // 将所有的图片显示在listview中
        listview = (GridView) this.findViewById(R.id.gv);
        checkImageList= new ArrayList<String>();

//		allScan();
        allScanAgain();

        ImageService imageService = new ImageService(this);
        images = imageService.getImages();

        doAsyncTask();
	}
	
	public void allScan() { 
        sendBroadcast(new Intent( 
                Intent.ACTION_MEDIA_MOUNTED, 
                Uri.parse("file://" + Environment.getExternalStorageDirectory()))); 
    }

    public void allScanAgain() {
        MediaScannerConnection.scanFile(this, new String[]{Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getPath()}, null, null);
    }
	
    @Override
    public Object doInBackground(int taskKind, Object... params) {
        if (images.size() > 0) {
            imageList = new ArrayList<Image>();
            Image image;
            for(int i = 0; i < images.size(); i ++ ) {
                image = new Image();
                image.setPath(images.get(i).get("data"));
                imageList.add(image);
            }
        }
        return null;
    }

    @Override
    public void onPostExecute(int taskKind, Object result, Object...params) {
        if (null == imageList || imageList.size() <= 0)
            showHint("没有图片");
        else {
            GridAdapter adapter = new GridAdapter(this, imageList, checkImageList) {
                @Override
                protected void fillData(final int position, ViewHolder holder) {
                    super.fillData(position, holder);
                    holder.image.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (position == 0) {
                                cameraSessionUtil.makeCameraRequest(ListImageActivity.this);
                            } else {
                                Intent intent = new Intent(ListImageActivity.this, ZoomImageActivity.class);
                                intent.putExtra("imagePath", images.get(position - 1).getPath());
                                startActivity(intent);
                            }
                        }
                    });
                }
            };
            listview.setAdapter(adapter);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == IBaseViewComponent.RETURN_CODE_OK) {
            if (requestCode == UIHelper.REQUEST_CODE_CAMERA) {
                try {
                    checkImageList.clear();
                    String path = cameraSessionUtil.getFilePath(resultCode, data, ListImageActivity.this);
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    checkImageList.add(path);
                    bundle.putSerializable(GridAdapter.DATA_IMAGE, (Serializable) checkImageList);
                    intent.putExtras(bundle);
                    setResult(2,intent);
                    finish();
                }
                catch (Exception ex) {

                }
            }
        }
    }


}
