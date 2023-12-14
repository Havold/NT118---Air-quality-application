package com.example.natural.api;

import com.example.natural.model.MapResponse;
import com.example.natural.model.MapResponse;
import com.example.natural.model.UserResponse;
import com.example.natural.model.WeatherResponse;
import com.example.natural.model.token;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface apiService_token {
    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

    apiService_token api_token = new Retrofit.Builder()
            .baseUrl("https://uiot.ixxc.dev")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(apiService_token.class);

    @FormUrlEncoded
    @Headers("Content-Type:application/x-www-form-urlencoded")
    @POST("auth/realms/master/protocol/openid-connect/token")
    Call<token> getLoginToken(@Field("client_id") String client_id,
                              @Field("username") String username,
                              @Field("password") String password,
                              @Field("grant_type") String grantType);


    @GET("auth/realms/master/protocol/openid-connect/logout")
    Call<token> logout(@Query("post_logout_redirect_uri") String url,
                       @Query("id_token_hint") String token);

    @GET("api/master/map")
    Call<MapResponse> getMapData(
            @Header("Authorization") String authorization
    );

    @GET("api/master/asset/{assetID}")
    Call<WeatherResponse> getAsset(
            @Path("assetID") String assetID,
            @Header("Authorization") String authorization);//, @Header("Authorization") String auth);

    @GET("api/master/user/user")
    Call<UserResponse> getUser(
            @Header("Authorization") String authorization);//, @Header("Authorization") String auth);

}
