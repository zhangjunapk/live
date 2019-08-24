package org.zj.xh.conf;

import android.os.Build;
import android.util.Log;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class HeaderInterceptor implements Interceptor {
    //保存cookie,登录成功后设置这个字段的值
    public static String cookie=null;
    @Override
    public Response intercept(Interceptor.Chain chain) throws IOException {
        //可以在这里设置拦截，如果访问了需要登录的接口，那就开启登录activity
        Request request = chain.request();
        Request.Builder builder = request.newBuilder();
        if(cookie!=null) {
            builder.addHeader("Cookie", cookie);
            if (Build.VERSION.SDK != null && Build.VERSION.SDK_INT > 13) {
                builder.addHeader("Connection", "close");
            }
        }
        else{
            Log.e("Cookie","Cookie not found");
        }
        return chain.proceed(builder.build());
    }
}