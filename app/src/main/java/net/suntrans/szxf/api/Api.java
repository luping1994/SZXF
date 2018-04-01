package net.suntrans.szxf.api;

import net.suntrans.szxf.bean.AddSceneChannelResult;
import net.suntrans.szxf.bean.Ameter3Entity;
import net.suntrans.szxf.bean.AmmeterInfos;
import net.suntrans.szxf.bean.ChannelEditorInfo;
import net.suntrans.szxf.bean.DeviceDetailResult;
import net.suntrans.szxf.bean.FreshChannelEntity;
import net.suntrans.szxf.bean.HisEntity;
import net.suntrans.szxf.bean.RespondBody;
import net.suntrans.szxf.bean.SampleResult;
import net.suntrans.szxf.bean.Ammeter3Eneity;
import net.suntrans.szxf.bean.AreaDetailEntity;
import net.suntrans.szxf.bean.AreaEntity;
import net.suntrans.szxf.bean.ChangedPasswordEntity;
import net.suntrans.szxf.bean.ControlEntity;
import net.suntrans.szxf.bean.DeviceEntity;
import net.suntrans.szxf.bean.DeviceInfoResult;
import net.suntrans.szxf.bean.EnergyEntity;
import net.suntrans.szxf.bean.EnvDetailEntity;
import net.suntrans.szxf.bean.LoginResult;
import net.suntrans.szxf.bean.SceneChannelResult;
import net.suntrans.szxf.bean.SceneEdit;
import net.suntrans.szxf.bean.SceneEntity;
import net.suntrans.szxf.bean.SceneTimeResult;
import net.suntrans.szxf.bean.SensusEntity;
import net.suntrans.szxf.bean.UpLoadImageMessage;
import net.suntrans.szxf.bean.UserInfo;
import net.suntrans.szxf.bean.YichangEntity;
import net.suntrans.szxf.uiv2.bean.ChannelEntity;
import net.suntrans.szxf.uiv2.bean.ChannelInfo;
import net.suntrans.szxf.uiv2.bean.MonitorEntity;
import net.suntrans.szxf.uiv2.bean.NoticeEntity;
import net.suntrans.szxf.uiv2.bean.SceneEntityV2;
import net.suntrans.szxf.uiv2.bean.SceneImage;
import net.suntrans.szxf.uiv2.bean.SceneItemlEntity;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import rx.Observable;

/**
 * Created by Looney on 2017/1/4.
 */

public interface Api {

    /**
     * 登录api
     *
     * @param username      账号
     * @param password      密码
     * @return
     */
    @FormUrlEncoded
    @POST("login/token")
    Observable<LoginResult> login(
                                  @Field("username") String username,
                                  @Field("password") String password);

    @POST("home/scene")
    Observable<SceneEntity> getHomeScene();

    @FormUrlEncoded
    @POST("scene/show")
    Observable<SceneChannelResult> getSceneChannel(@Field("id") String id);

    @FormUrlEncoded
    @POST("switch/slcChannel")
    Observable<ControlEntity> switchChannel(@Field("din") String id,
                                            @Field("cmd") String datapoint,
                                            @Field("number") String din);


    @POST("home/light")
    Observable<DeviceEntity> getAllDevice();

    @FormUrlEncoded
    @POST("switch/scene")
    Observable<ControlEntity> switchScene(@Field("id") String id);



    @FormUrlEncoded
    @POST("house/houseChannel")
    Observable<AreaDetailEntity> getRoomChannel(@Field("house_id") String id);

    @FormUrlEncoded
    @POST("energy/ammeter3")
    Observable<Ammeter3Eneity> getAmmeter3Detail(@Field("sno") String sno);

    @POST("energy/index")
    Observable<EnergyEntity> getEnergyIndex();

    @POST("home/sensus")
    Observable<SensusEntity> getHomeSceneNew();


    @POST("device/index")
    Observable<DeviceInfoResult> getDevicesInfo();

    @FormUrlEncoded
    @POST("sensus/show")
    Observable<EnvDetailEntity> getEnvDetail(@Field("id") String id);


    @POST("user/info")
    Observable<UserInfo> getUserInfo();


    @FormUrlEncoded
    @POST("user/password")
    Observable<ChangedPasswordEntity> changedPassword(@Field("oldpassword") String oldPassword,
                                                      @Field("newpassword") String newPassword);

    @FormUrlEncoded
    @POST("user/guestbook")
    Observable<ChangedPasswordEntity> commitGusetBook(@Field("contents") String oldPassword);


//    @FormUrlEncoded
//    @POST("user/info")
//    Observable<EnergyUsedEntity> getEnergyUsed(@Field("time") String date, @Field("type") String type);

    @FormUrlEncoded
    @POST("scene/add")
    Observable<SampleResult> addScene(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("scene/delete")
    Observable<SampleResult> deleteScene(@Field("id") String id);

    @FormUrlEncoded
    @POST("scene/update")
    Observable<SampleResult> updateScene(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("scene/addchannel")
    Observable<AddSceneChannelResult> addChannel(@Field("scene_id") String scene_id,
                                                 @Field("channel_id") String channel_id,
                                                 @Field("cmd") String cmd);

    @FormUrlEncoded
    @POST("scene/deletechannel")
    Observable<SampleResult> deleteChannel(@Field("id") String scene_id);

    @FormUrlEncoded
    @POST("scene/setchannel")
    Observable<SampleResult> setChannel(@Field("id") String id, @Field("cmd") String cmd);

    @FormUrlEncoded
    @POST("house/add")
    Observable<SampleResult> addFloor(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("house/add_area")
    Observable<SampleResult> addArea(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("house/delete_area")
    Observable<SampleResult> deleteArea(@Field("id") String id);

    @FormUrlEncoded
    @POST("house/delete_channel")
    Observable<SampleResult> deleteAreaChannel(@Field("id") String id);

    @FormUrlEncoded
    @POST("scene/edit")
    Observable<SceneEdit> getSceneInfo(@Field("id") String id);

    @FormUrlEncoded
    @POST("energy/more")
    Observable<AmmeterInfos> getAmmeterInfo(@Field("sno") String sno);


    @POST("house/freshchannel")
    Observable<FreshChannelEntity> getFreshChannel();

    @FormUrlEncoded
    @POST("user/profile")
    Observable<SampleResult> updateProfile(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("device/updatechannel")
    Observable<SampleResult> updateChannel(@FieldMap Map<String, String> map);


    @FormUrlEncoded
    @POST("energy/ammeter3")
    Observable<Ameter3Entity> getAmmeter3Data(@Field("sno") String sno, @Field("date") String date);

    @FormUrlEncoded
    @POST("house/add_channel")
    Observable<AddSceneChannelResult> addAreaChannel(@Field("area_id") String area_id,
                                                     @Field("channel_id") String channel_id,
                                                     @Field("show_sort") String show_sort);

    @Multipart
    @POST("upload/images")
    Observable<UpLoadImageMessage> upload(
            @Part MultipartBody.Part image);

    @FormUrlEncoded
    @POST("device/abnormal")
    Observable<YichangEntity> getYichang(@Field("page") String page);

    @FormUrlEncoded
    @POST("house/delete")
    Observable<SampleResult> deleteFloor(@Field("id") String id);

    @FormUrlEncoded
    @POST("device/abnormal_delete")
    Observable<SampleResult> deleteLog(@Field("log_id") String log_id);

    @FormUrlEncoded
    @POST("scene/updatetimer")
    Observable<RespondBody> setSceneTiming(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("scene/gettimer")
    Observable<RespondBody<List<SceneTimeResult>>> getSceneTiming(@Field("scene_id") String scene_id);

    @FormUrlEncoded
    @POST("scene/deletetimer")
    Observable<RespondBody> deleteTimmer(@Field("id") String id);

    @FormUrlEncoded
    @POST("scene/addtimer")
    Observable<RespondBody> addTimmer(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("device/channel_list")
    Observable<DeviceDetailResult> getDeviceDetail(@Field("dev_id") String dev_id);

    @FormUrlEncoded
    @POST("device/channel_edit")
    Observable<ChannelEditorInfo> getChannelEditor(@Field("channel_id") String channel_id);


    /**
     * 查询三相电表实时历史数据
     *
     * @param
     * @return
     */
    @FormUrlEncoded
    @POST("energy/history")
    Observable<HisEntity> getZHCurHis(@FieldMap Map<String, String> map);


    //=========================================================================
    //2018-3-27



    //用电状态接口
    @POST("monitor/index")
    Observable<AreaEntity> loadMonitorIndex();

    @FormUrlEncoded
    @POST("monitor/channel")
    Observable<RespondBody<List<MonitorEntity>>> loadMonitorRoomChannel(@Field("id") String id);

    @FormUrlEncoded
    @POST("switch/slc/area")
    Observable<RespondBody<Map<String, String>>> switchSlcArea(@FieldMap Map<String, String> map);

    //消息公告接口
    @POST("notice/index")
    Observable<RespondBody<NoticeEntity>> loadNoticeList();

    @FormUrlEncoded
    @POST("notice/store")
    Observable<RespondBody> storeNotice(@FieldMap Map<String, String> map);

    @Multipart
    @POST("notice/upload")
    Observable<RespondBody<Map<String, String>>> uploadNoticeFile(
            @Part MultipartBody.Part image);

    //能耗接口
    @POST("energy/index")
    Observable<AreaEntity> getEnergyIndexNewApi();

    //场景相关
    @POST("scene/index")
    Observable<RespondBody<SceneEntityV2>> getSceneV2();

    @POST("scene/image")
    Observable<RespondBody<SceneImage>> getSceneDefaultImg();

    @POST("scene/myChannels")
    Observable<RespondBody<List<ChannelInfo>>> getScebeChooseChannel();

    @FormUrlEncoded
    @POST("scene/channel")
    Observable<RespondBody<SceneItemlEntity>> getSceneChannelV2(@Field("id") String id);

    @FormUrlEncoded
    @POST("scene/channel/store")
    Observable<RespondBody> addSceneChannel(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("scene/update")
    Observable<RespondBody> updateSceneInfo(@FieldMap Map<String, String> map);
    @FormUrlEncoded
    @POST("scene/channel/update")
    Observable<RespondBody> updateSceneChannel(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("scene/delete")
    Observable<RespondBody> deleteSceneV2(@Field("ids") String id);

    @FormUrlEncoded
    @POST("scene/channel/delete")
    Observable<RespondBody> deleteSceneChannel(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("scene/store")
    Observable<RespondBody> createScene(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("switch/slc/scene")
    Observable<RespondBody> switchSceneV2(@Field("id") String id);


    @POST("houseList")
    Observable<AreaEntity> getHomeHouse();

    @POST("sensus/sensusList")
    Observable<AreaEntity> getSensusList();
}
