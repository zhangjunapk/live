package org.zj.xh.httpinterface;

import org.zj.xh.bean.Result;
import org.zj.xh.bean.RoomBean;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ILiveInterface {
    /**
     * 开始直播，房间名，房间号
     * @param
     * @param
     * @return
     */
    @POST("openRoom")
    Observable<Result<Object>> startLive(@Body RoomBean roomBean);

    /**
     * 关闭房间
     * @param roomBean
     * @return
     */
    @POST("closeRoom")
    Observable<Result<Object>> closeLive(@Body RoomBean roomBean);

    /**
     * 获得直播间列表
     * @return
     */
    @GET("getRoomList")
    Observable<Result<List<RoomBean>>> getRoomList();
}
