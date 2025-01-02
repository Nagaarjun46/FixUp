package com.example.fixup.apicalls;

import com.example.fixup.OtpVerificationActivity;
import com.example.fixup.apicalls.models.LoginRequestModel;
import com.example.fixup.apicalls.models.NotificationResponse;
import com.example.fixup.apicalls.models.NotificationUserModel;
import com.example.fixup.apicalls.models.OTPRequestModel;
import com.example.fixup.models.Issue;
import com.example.fixup.models.IssueDetailModel;
import com.example.fixup.models.IssueResponse;
import com.example.fixup.models.NotificationModel;
import com.example.fixup.models.ProfileModel;
import com.example.fixup.models.UserDetailsModel;

import java.util.HashMap;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiService {
    @POST("auth/register")
    Call<Void> register(@Body HashMap<String, String> map);
    @POST("auth/sendOTP")
    Call<OTPRequestModel> sendOTP(@Body HashMap<String, String> map);
    @Multipart
    @POST("issue/addPost")
    Call<Void> addPost(
            @Part("title") RequestBody title,
            @Part("description") RequestBody description,
            @Part MultipartBody.Part image,
            @Part("city") RequestBody city,
            @Part("area") RequestBody area,
            @Part("priority") RequestBody priority,
            @Part("volunteersNeeded") RequestBody volunteersNeeded,
            @Part("email") RequestBody email);
    @POST("auth/loginUser")
    Call<LoginRequestModel> loginUser(@Body HashMap<String, String> map);
    @POST("issue/fetchIssuesByCity")
    Call<IssueResponse> fetchPostByUserCity(@Body HashMap<String, String> map);
    @POST("issue/addVolunteerToIssue")
    Call<Void> addVolunteerToIssue(@Body HashMap<String, String> map);
    @POST("auth/fetchUserDetailsForProfile")
    Call<ProfileModel> fetchUserDetailsForProfile(@Body HashMap<String, String> map);
    @POST("updates/updateUserName")
    Call<Void> updateUserName(@Body HashMap<String, String> map);
    @POST("updates/updateUserContactNo")
    Call<Void> updateUserContactNo(@Body HashMap<String, String> map);
    @POST("updates/updateUserState")
    Call<Void> updateUserState(@Body HashMap<String, String> map);
    @POST("updates/updateUserCity")
    Call<Void> updateUserCity(@Body HashMap<String, String> map);
    @POST("auth/sendNotificationToUsersByEmail")
    Call<NotificationUserModel> sendNotificationToUsersByEmail(@Body HashMap<String, String> map);
    @POST("notifications/fetchNotificationsByEmail")
    Call<NotificationResponse> fetchNotifications(@Body HashMap<String, String> map);
}