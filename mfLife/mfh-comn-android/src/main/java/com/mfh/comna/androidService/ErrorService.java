package com.mfh.comna.androidService;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Toast;

import com.mfh.comna.bizz.error.SendErrorActivity;
import com.mfh.comna.bizz.error.service.ErrorCollectService;
import com.mfh.comna.comn.logic.ServiceFactory;

/**错误的后台service
 * Created by 李潇阳 on 2014/11/6.
 */
public class ErrorService extends Service {

    private static ErrorService mInstance = null;

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public static ErrorService getInstance() {
        return mInstance;
    }



    public  void sendError(final String message, Throwable ex){
        ErrorCollectService collectService = ServiceFactory.getService(ErrorCollectService.class);
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, SendErrorActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        StringBuilder builder = new StringBuilder();
        StackTraceElement[] stackTraceElements = ex.getStackTrace();
        for (StackTraceElement stackTraceElement:stackTraceElements){
            builder.append(stackTraceElement).append("\n");
        }
        intent.putExtra("msg", ex.toString() + builder);
        collectService.sendErrorInformationToNet(ex);
        Bundle bundle = new Bundle();
        bundle.putSerializable("ex_entity", ex);
        intent.putExtras(bundle);
        //intent.putExtra("ex", ex);
        startActivity(intent);
        stopSelf();
    }

}
