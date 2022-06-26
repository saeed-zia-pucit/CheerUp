package com.app.cheerthemup.notifications.api;

import com.app.cheerthemup.notifications.MyResponse;
import com.app.cheerthemup.notifications.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {


     @Headers({
             "Content-Type:application/json",
             "Authorization:key=AAAAuKDI26g:APA91bE0qqO2f8nzyHZDeWILVffVdDGGOMCQisEoQhwHnFdIqeIA5V3Kr2qosUiM35Pg9lHKIlkAhEnnC-wsGvtMWhKMiwircV5Mw7dY_oRBoIAbxUlr8u_SxhkoxNVrI17J4vvMA0_D"
     })
    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);

}
