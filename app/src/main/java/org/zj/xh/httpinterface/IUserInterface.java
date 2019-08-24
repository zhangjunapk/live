package org.zj.xh.httpinterface;


import android.database.Observable;

import org.zj.xh.bean.Result;

import retrofit2.http.GET;
import retrofit2.http.POST;

public interface IUserInterface {
    /**
     * 登录的接口定义
     * @param username
     * @param password
     * @return
     */
    @POST("login")
    Observable<Result<Object>> login(String username, String password);

    /**
     * 获得直播间列表
     * @return
     */
    @GET("getLiveList")
    Observable<Result<Object>> getLiveRoomList();

    /**
     * 开直播的接口定义
     * @return
     */
    @POST("openRoom")
    Observable<Result<Object>> openRoom();

    /**
     * 关闭直播间
     * @return
     */
    @POST("closeRoom")
    Observable<Result<Object>> closeRoom();
}
