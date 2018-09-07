package com.cc.android.net;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import com.android.volley.*;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cc.android.KeDaoApplication;
import com.cc.android.R;
import com.cc.android.activity.LoginActivity;
import com.cc.android.entity.RspResult;
import com.cc.android.tools.ActivityManager;
import com.cc.android.utils.Utils;
import com.cc.android.widget.CProgressDialog;
import com.google.gson.Gson;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by yh on 2016/6/15.
 */
public class NetUtils {
    static boolean isCopy = false;
    static StringRequest retry;

    public interface NetCallBack<T> {
        void success(T rspData);

        void failed(String msg);
    }

    public interface StringCallBack {
        void success(String rspData);

        void failed(String msg);

    }

    //测试地址
    //public static final String BASE_URL = "http://192.168.10.17:8080/rms/";
    public static String BASE_URL = "http://120.27.111.37:8087/cms/";
    public static final String SERVICE_URL = "http://120.27.111.37:8087/cms/";
    //public static  String BASE_URL = "http://ht.kdcdn.cn/rms/";
    //public static final String SERVICE_URL = "http://ht.kdcdn.cn/rms/";
    //public static  String SERVICE_URL = "http://192.168.1.30:8080/rms/";
    //public static  String BASE_URL = "http://192.168.1.30:8080/rms/";

    public static void setBaseUrl(String baseUrl) {
        BASE_URL = baseUrl;
    }

    public static String getServiceUrl() {
        return SERVICE_URL;
    }

    // 正式环境
    // public static final String BASE_URL = "http://114.55.9.46:8084/";

    private static RequestQueue mQueue = Volley.newRequestQueue(KeDaoApplication.getContext());


    /***
     * NetCallBack
     ***/
    // POST
    public static <T, X extends RspResult<T>> void post(Context context, String url, final Map<String, String> params,
                                                        final NetCallBack<T> callBack, final Class<X> rspCls) {
        request(Request.Method.POST, context, url, params, null, false, callBack, rspCls);
    }

    // POST
    public static <T, X extends RspResult<T>> void post(Context context, String url, final Map<String, String> params,
                                                        final Map<String, String> headers, final NetCallBack<T> callBack, final Class<X> rspCls) {
        request(Request.Method.POST, context, url, params, headers, false, callBack, rspCls);
    }

    // get
    public static <T, X extends RspResult<T>> void get(Context context, String url, final Map<String, String> params,
                                                       final NetCallBack<T> callBack, final Class<X> rspCls) {
        request(Request.Method.GET, context, url + getReqPama(params), null, null, false, callBack, rspCls);
    }

    // get
    public static <T, X extends RspResult<T>> void get(Context context, String url, final Map<String, String> params,
                                                       final Map<String, String> headers, final NetCallBack<T> callBack, final Class<X> rspCls) {
        request(Request.Method.GET, context, url + getReqPama(params), null, headers, false, callBack, rspCls);
    }

    public static <T, X extends RspResult<T>> void request(int method, final Context context, String path,
                                                           final Map<String, String> params, final Map<String, String> headers, boolean copy,
                                                           final NetCallBack<T> callBack, final Class<X> rspCls) {
        final CProgressDialog progressDialog = new CProgressDialog(context, R.style.Dialog);
        String url = BASE_URL + path;
        StringRequest request = new StringRequest(method, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String jsonObject) {
                System.out.println(jsonObject);
                if (progressDialog != null) progressDialog.dismiss();
                Gson gson = new Gson();
                if (callBack != null) {
                    if (rspCls == null) {
                        return;
                    }
                    try {
                        X resp = (X) gson.fromJson(jsonObject, rspCls);
                        if (resp.getCode() != 200) {
                            if (resp.getCode() == 431) {
                                Intent intent = new Intent(context, LoginActivity.class);
                                context.startActivity(intent);
                                ActivityManager.getScreenManager().popAllActivity();
                                KeDaoApplication.getInstance().onTerminate();
                                ((Activity) context).overridePendingTransition(R.anim.ani_left_get_into, R.anim.ani_right_sign_out);
                                callBack.failed("与主机失去连接，请重新登录。");
                            } else {
                                callBack.failed(resp.getMessage());
                            }
                        } else
                            callBack.success(resp.getResult());
                    } catch (Exception e) {
                        callBack.failed("服务请求响应解析失败");
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (progressDialog != null) progressDialog.dismiss();
                if (error instanceof NetworkError) {
                    if (callBack != null) callBack.failed("网络异常，请检查网络后重试!");
                    return;
                } else if (error instanceof ServerError) {
                    if (callBack != null) callBack.failed("网络异常，请检查主机收银台是否开启？");
                    return;
                } else if (error instanceof AuthFailureError) {
                } else if (error instanceof ParseError) {
                } else if (error instanceof NoConnectionError) {
                } else if (error instanceof TimeoutError) {
                    if (callBack != null) callBack.failed("网络连接超时!");

                    return;
                }
                if (callBack != null) callBack.failed(error.getMessage());

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }

            @Override
            protected String getParamsEncoding() {
                return super.getParamsEncoding();
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                if (headers != null && headers.size() > 0) {
                    // headers.put("Accept", "application/json");
                    // headers.put("Content-Type",
                    // "application/json; charset=UTF-8");
                    return headers;
                } else {
                    return super.getHeaders();
                }
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(500000,// 默认超时时间，应设置一个稍微大点儿的，例如本处的500000
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,// 默认最大尝试次数
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // if (headers != null && headers.size() > 0) {
        // try {
        // request.getHeaders().putAll(headers);
        // } catch (AuthFailureError authFailureError) {
        // authFailureError.printStackTrace();
        // }
        // }

        // request.setRetryPolicy(new DefaultRetryPolicy(4 * 1000, 1, 1.0f));
        if (copy) {
            isCopy = copy;
            retry = request;
        }
        mQueue.add(request);
        //实例化时候就启动了，不需要手动启动，手动启动有可能导致抛异常
        //mQueue.start();
    }

    public static String getBaseUrl() {
        return BASE_URL;
    }

    /**
     * 参数组合
     *
     * @param params
     * @return
     */
    public static String getReqPama(Map<String, String> params) {
        if (params == null || params.size() == 0) return "";
        StringBuffer sBuffer = new StringBuffer();
        sBuffer.append("?");
        Iterator<?> iterator = params.keySet().iterator();
        while (iterator.hasNext()) {
            String key = (String) iterator.next();
            sBuffer.append(key + "=" + params.get(key) + "&");
        }
        String reqString = sBuffer.toString();
        return reqString.substring(0, reqString.length() - 1);
    }

    /**
     * 根据url获取图片流
     *
     * @param path
     * @param params
     * @return
     */
    public static Bitmap getHttpBitmap(String path, final Map<String, String> params) {
        Bitmap bitmap = null;
        try {
            String url = BASE_URL + path + getReqPama(params);
            URL picurl = new URL(url);
            // 获得连接
            HttpURLConnection conn = (HttpURLConnection) picurl.openConnection();
            conn.setConnectTimeout(1000);// 设置超时
            conn.setDoInput(true);
            conn.setUseCaches(false);// 不缓存
            conn.connect();
            InputStream is = conn.getInputStream();// 获得图片的数据流
            bitmap = BitmapFactory.decodeStream(is);
            is.close();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    /**
     * 根据url获取图片流
     *
     * @param url
     * @return
     */
    public static Bitmap getHttpBitmapByUrl(String url) {
        Bitmap bitmap = null;
        try {
            URL picurl = new URL(url);
            // 获得连接
            HttpURLConnection conn = (HttpURLConnection) picurl.openConnection();
            conn.setConnectTimeout(1000);// 设置超时
            conn.setDoInput(true);
            conn.setUseCaches(false);// 不缓存
            conn.connect();
            InputStream is = conn.getInputStream();// 获得图片的数据流
            bitmap = BitmapFactory.decodeStream(is);
            is.close();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public static void post(Context context, final String url, final String header, final int type, final String jsonData,
                            final HttpCallBack callback) {
        if (!Utils.isNetworkConnected(context)) {
            if (callback != null) callback.result("网络异常!", 0);
            return;
        }
        new AsyncTask<String, Void, String>() {
            private StringBuffer result = new StringBuffer();

            @Override
            protected String doInBackground(String... params) {
                try {
                    System.out.println("post请求地址：" + url + "\n数据:" + jsonData);

                    URL my_url = new URL(BASE_URL + url);
                    HttpURLConnection connection = (HttpURLConnection) my_url.openConnection();
                    connection.setDoOutput(true);
                    connection.setDoInput(true);
                    connection.setRequestMethod("POST");
                    connection.setUseCaches(false);
                    connection.setInstanceFollowRedirects(true);
                    connection.setRequestProperty("Content-Type", "application/json");
                    connection.setRequestProperty("token", header);
                    connection.connect();
                    DataOutputStream out = new DataOutputStream(connection.getOutputStream());
                    byte[] content = jsonData.getBytes("utf-8");
                    out.write(content, 0, content.length);
                    out.flush();
                    out.close();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
                    String line = "";
                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }
                    reader.close();
                    connection.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("Http post请求错误：");
                }
                publishProgress();
                return null;
            }

            @Override
            protected void onProgressUpdate(Void... values) {
                super.onProgressUpdate(values);
                System.out.println("收到服务器返回结果：\n" + result.toString() + "\n");
                if (callback != null) callback.result(result.toString(), type);
                result.delete(0, result.length());
                cancel(true);
            }
        }.execute(url);
    }

    public interface HttpCallBack {
        void result(String result, int type);
    }
}
