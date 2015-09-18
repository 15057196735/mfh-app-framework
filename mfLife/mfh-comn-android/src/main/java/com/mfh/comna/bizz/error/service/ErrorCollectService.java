package com.mfh.comna.bizz.error.service;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import com.mfh.comna.bizz.login.logic.MfhLoginService;
import com.mfh.comn.bean.TimeCursor;
import com.mfh.comna.bizz.error.ErrorConstants;
import com.mfh.comna.bizz.error.dao.ErrorDbDao;
import com.mfh.comna.bizz.error.dao.ErrorNetDao;
import com.mfh.comna.bizz.error.entity.ErrorEntity;
import com.mfh.comna.comn.database.dao.NetProcessor;
import com.mfh.comna.comn.logic.BaseService;
import com.mfh.comna.comn.logic.DataSyncStrategy;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**收集错误的service
 * Created by 李潇阳 on 2014/11/7.
 */
public class ErrorCollectService extends BaseService<ErrorEntity, Long, ErrorDbDao> {

    private MfhLoginService ls = MfhLoginService.get();
    private ErrorNetDao netDao = new ErrorNetDao();
    @Override
    protected Class<ErrorDbDao> getDaoClass() {
        return ErrorDbDao.class;
    }

    @Override
    public DataSyncStrategy getDataSyncStrategy() {
        return null;
    }


    /**
     * 发送错误信息到服务器,现在不用这个方法了
     * @param ex
     */
    public void sendErrorInformationToNet(Throwable ex) {
        ErrorEntity errorEntity = getNewEntity(ex);//利用ex生成新的错误信息
        getDao().save(errorEntity);//把错误信息保存到本地
        //上传到网络...
        netDao.save(errorEntity, new NetProcessor.ComnProcessor<Long>() {
            @Override
            protected void processOperResult(Long resultKey) {
                getContext().sendBroadcast(new Intent(ErrorConstants.ERROR_DISMISS_DIALOG));
            }

            @Override
            protected void processFailure(Throwable t, String errMsg) {
                super.processFailure(t, errMsg);
                getContext().sendBroadcast(new Intent(ErrorConstants.ERROR_DISMISS_DIALOG_FAIL));
            }
        }, "/clientLog/create");

    }

    /**
     * 后台启动程序，把错误信息传送到后台服务器，
     * 考率到时耗时操作，所以在Service，或者新启动一个线程调用
     */
    public void sendErrorInformationToSer() {
        //获取到尚未被上传的错误
        List<ErrorEntity> errorEntities = getDao().getAllUnUploadError();
        boolean is = (getDao() == null);
        for (int i = 0; i < errorEntities.size(); i++) {
            ErrorEntity bean = errorEntities.get(0);
            Long id = bean.getId();
            bean.setId(null);
            netDao.save(bean, new NetProcessor.ComnProcessor<Long>() {
                @Override
                protected void processOperResult(Long resultKey) {

                }
            }, "/clientLog/create");//无论成功与否，都不做任何处理
            bean.setIsUpload(1);
            bean.setId(id);
            getDao().saveOrUpdate(bean);
        }
    }

    /**
     * 根据传进来的异常生成ErrorEntity
     * @param ex
     * @return
     */
    public ErrorEntity getNewEntity(Throwable ex) {
        ErrorEntity errorEntity = new ErrorEntity();
        errorEntity.setAndroidLevel(android.os.Build.VERSION.RELEASE);//设置android 版本
        errorEntity.setErrorTime(new SimpleDateFormat(TimeCursor.INNER_DATAFORMAT).format(new Date()));//设置时间
        errorEntity.setHardwareInformation(getMobileInfo());//设置硬件信息
        errorEntity.setSoftVersion(getVersionInfo());//设置软件信息
        errorEntity.setLoginName(ls.getLoginName());//设置登陆名称
        errorEntity.setStackInformation(getStackDetailDescribe(ex));//设置堆栈信息
        return errorEntity;
    }

    /**
     * 获取堆栈信息
     * @param ex
     * @return
     */
    public  String getStackDetailDescribe(Throwable ex) {
        StringBuilder builder = new StringBuilder();
        StackTraceElement[] stackTraceElements = ex.getStackTrace();
        for (StackTraceElement stackTraceElement:stackTraceElements){
            builder.append(stackTraceElement).append("\n");
        }
        if (builder.length() > 1000)
            builder.setLength(1000);
        return String.valueOf(builder);
    }

    /**
     * 获取程序的版本信息
     * @return
     */
    private String getVersionInfo(){
        try {
            PackageManager pm = getContext().getPackageManager();
            PackageInfo info =pm.getPackageInfo(getContext().getPackageName(), 0);
            return  info.versionName;
        } catch (Exception e) {
            e.printStackTrace();
            return "版本号未知";
        }
    }

    /**
     * 获取手机的硬件信息
     * @return
     */
    private String getMobileInfo() {
        StringBuffer sb = new StringBuffer();
        //通过反射获取系统的硬件信息
        try {
            Field[] fields = Build.class.getDeclaredFields();
            for(Field field: fields){
                //暴力反射 ,获取私有的信息
                field.setAccessible(true);
                String name = field.getName();
                String value = field.get(null).toString();
                if ("FINGERPRINT".equals(name)) {
                    sb.append(name+"="+value);
                    sb.append("\n");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (sb.length() > 100)
            sb.substring(0, 100);
        return sb.toString();
    }
}
