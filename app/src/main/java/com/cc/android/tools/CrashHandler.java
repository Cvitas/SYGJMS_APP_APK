package com.cc.android.tools;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import com.cc.android.KeDaoApplication;

import java.io.*;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yh on 2016/6/13.
 *
 * UncaughtException������,��������Uncaught�쳣��ʱ��,�и������ӹܳ���,����¼���ʹ��󱨸�.
 *
 * ��Ҫ��Application��ע�ᣬΪ��Ҫ�ڳ����������ͼ����������
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {

    public static final String TAG = "CrashHandler";

    // ϵͳĬ�ϵ�UncaughtException������
    private Thread.UncaughtExceptionHandler mDefaultHandler;
    // CrashHandlerʵ��
    private static CrashHandler instance;
    // �����Context����
    private Context mContext;
    // �����洢�豸��Ϣ���쳣��Ϣ
    private Map<String, String> infos = new HashMap<String, String>();

    // ���ڸ�ʽ������,��Ϊ��־�ļ�����һ����
    private DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");

    /** ��ֻ֤��һ��CrashHandlerʵ�� */
    private CrashHandler() {
    }

    /** ��ȡCrashHandlerʵ�� ,����ģʽ */
    public static CrashHandler getInstance() {
        if (instance == null)
            instance = new CrashHandler();
        return instance;
    }

    /**
     * ��ʼ��
     */
    public void init(Context context) {
        mContext = context;
        // ��ȡϵͳĬ�ϵ�UncaughtException������
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        // ���ø�CrashHandlerΪ�����Ĭ�ϴ�����
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    /**
     * ��UncaughtException����ʱ��ת��ú���������
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (!handleException(ex) && mDefaultHandler != null) {
            // ����û�û�д�������ϵͳĬ�ϵ��쳣������������
            mDefaultHandler.uncaughtException(thread, ex);
        } else {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                Log.e(TAG, "error : ", e);
            }
            // �˳�����
            ActivityManager.getScreenManager().popAllActivity();
            KeDaoApplication.getInstance().onTerminate();
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        }
    }

    /**
     * �Զ��������,�ռ�������Ϣ ���ʹ��󱨸�Ȳ������ڴ����.
     *
     * @param ex
     * @return true:��������˸��쳣��Ϣ;���򷵻�false.
     */
    private boolean handleException(Throwable ex) {
        if (ex == null) {
            return false;
        }
        // �ռ��豸������Ϣ
        //collectDeviceInfo(mContext);

        // ʹ��Toast����ʾ�쳣��Ϣ
		/*new Thread() {
			@Override
			public void run() {
				Looper.prepare();
				Toast.makeText(mContext, "�ܱ�Ǹ,��������쳣,�����˳�.", Toast.LENGTH_SHORT).show();
				Looper.loop();
			}
		}.start();*/

        // ������־�ļ�
        saveCatchInfo2File(ex);
        return true;
    }

    /**
     * �ռ��豸������Ϣ
     *
     * @param ctx
     */
    public void collectDeviceInfo(Context ctx) {
        try {
            PackageManager pm = ctx.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(), PackageManager.GET_ACTIVITIES);
            if (pi != null) {
                String versionName = pi.versionName == null ? "null" : pi.versionName;
                String versionCode = pi.versionCode + "";
                infos.put("versionName", versionName);
                infos.put("versionCode", versionCode);
            }
        } catch (NameNotFoundException e) {
            Log.e(TAG, "an error occured when collect package info", e);
        }
        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                infos.put(field.getName(), field.get(null).toString());
                Log.d(TAG, field.getName() + " : " + field.get(null));
            } catch (Exception e) {
                Log.e(TAG, "an error occured when collect crash info", e);
            }
        }
    }

    /**
     * ���������Ϣ���ļ���
     *
     * @param ex
     * @return �����ļ�����,���ڽ��ļ����͵�������
     */
    private String saveCatchInfo2File(Throwable ex) {

        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, String> entry : infos.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            sb.append(key + "=" + value + "\n");
        }

        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();
        String result = writer.toString();
        sb.append(result);
        try {
            long timestamp = System.currentTimeMillis();
            String time = formatter.format(new Date());
            String fileName = "crash-" + time + "-" + timestamp + ".log";
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                String path = Environment.getExternalStorageDirectory()	+ "/Moli/crash/";
                File dir = new File(path);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                FileOutputStream fos = new FileOutputStream(path + fileName);
                fos.write(sb.toString().getBytes());
                // ���͸�������Ա
                sendCrashLog2PM(path + fileName);
                fos.close();
            }
            return fileName;
        } catch (Exception e) {
            Log.e(TAG, "an error occured while writing file...", e);
        }
        return null;
    }

    /**
     * ������ĵ��±����Ĵ�����Ϣ���͸�������Ա
     *
     * Ŀǰֻ��log��־������sdcard �������LogCat�У���δ���͸���̨��
     */
    private void sendCrashLog2PM(String fileName) {
        if (!new File(fileName).exists()) {
            //Toast.makeText(mContext, "��־�ļ������ڣ�", Toast.LENGTH_SHORT).show();
            return;
        }
        FileInputStream fis = null;
        BufferedReader reader = null;
        String s = null;
        try {
            fis = new FileInputStream(fileName);
            reader = new BufferedReader(new InputStreamReader(fis, "GBK"));
            while (true) {
                s = reader.readLine();
                if (s == null)
                    break;
                // ����Ŀǰ��δȷ���Ժ��ַ�ʽ���ͣ������ȴ��log��־��
                Log.e("com.hfyj.android", s.toString());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally { // �ر���
            try {
                reader.close();
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
