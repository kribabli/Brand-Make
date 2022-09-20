package com.festivalbanner.digitalposterhub.Retrofit;


import com.festivalbanner.digitalposterhub.ModelRetrofit.CompanyDetails;
import com.festivalbanner.digitalposterhub.ModelRetrofit.List_of_Categories_name;
import com.festivalbanner.digitalposterhub.ModelRetrofit.List_of_Video_Category_Name;
import com.festivalbanner.digitalposterhub.ModelRetrofit.ResponseLogin;
import com.festivalbanner.digitalposterhub.ModelRetrofit.Response_PaymntPlan;
import com.festivalbanner.digitalposterhub.ModelRetrofit.UserRegister;
import com.festivalbanner.digitalposterhub.ModelRetrofit.VideoHomeModel;
import com.festivalbanner.digitalposterhub.Utills.BusinessesResponse;


import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface Api {

    @POST("user/forget_password")
    Call<ResponseLogin> forgetPassword(@Query("contact") String contact);

    @POST("user/reset_password")
    Call<ResponseLogin> resetPassword(@Query("contact") String contact, @Query("reset_otp") String reset_otp, @Query("password") String password);

    @POST("slider/slider_list")
    Call<VideoHomeModel> bannerImageList();

    @POST("user/business_list")
    Call<UserRegister> businessList();

    @POST("festival_category/festival_image_category_list")
//    @POST("category/category_list")
    Call<List_of_Categories_name> getCategoriesNameList(@Query("api_token") String api_token);


    @POST("festival_category/festival_images_list")
//    @POST("category/category_images_list")
    Call<List_of_Categories_name> getCategoriesImageList(@Query("api_token") String api_token, @Query("festival_image_category_id") String category_id, @Query("language") String language);


    @POST("user/feedback")
    Call<UserRegister> feedback(@Query("api_token") String api_token, @Query("name") String name,
                                @Query("contact") String contact,
                                @Query("description") String description);


    @Multipart
    @POST("user/register")
    Call<ResponseLogin> signUp(@Part("name") RequestBody name,
//    Call<UserRegister> signUp(@Part("name") RequestBody name,
                              @Part("email") RequestBody email,
                              @Part("password") RequestBody password,
                              @Part("city") RequestBody city,
                              @Part MultipartBody.Part image,
                              @Part("business") RequestBody business,
                              @Part("contact") RequestBody contact);

    @FormUrlEncoded
    @POST("setting/plan")
    Call<Response_PaymntPlan> getplan(@Field("api_token") String api_token);

    @POST("userapi/listbusiness.php")
    Call<BusinessesResponse> get_businesses();

    @FormUrlEncoded
    @POST("setting/contactus")
    Call<Response_PaymntPlan> getcontactus(@Field("api_token") String api_token);


    @FormUrlEncoded
    @POST("user/login")
    Call<ResponseLogin> signIn(
            @Field("email") String email,
            @Field("password") String password);

    @FormUrlEncoded
    @POST("user/check_otp")
    Call<ResponseLogin> sendOTP(
            @Field("contact") String contact,
            @Field("sms_otp") String password);

    @FormUrlEncoded
    @POST("user/profile")
    Call<ResponseLogin> getProfileDetails(@Field("api_token") String api_token);

    @Multipart
    @POST("user/profile")
    Call<UserRegister> updateProfile(@Part("api_token") RequestBody api_token,
                                     @Part("name") RequestBody name,
                                     @Part("email") RequestBody email,
                                     @Part MultipartBody.Part image,
                                     @Part("city") RequestBody city);

    @FormUrlEncoded
    @POST("user/profile")
    Call<ResponseLogin> getBusinessDetails(@Field("api_token") String api_token);


    @Multipart
    @POST("festival_category/company_detail_add")
//    @POST("category/company_detail_add")
    Call<CompanyDetails> addBusinessDetails(@Part("api_token") RequestBody api_token,
                                            @Part("name") RequestBody name,
                                            @Part("email") RequestBody email,
                                            @Part MultipartBody.Part logo,
                                            @Part("address") RequestBody address,
                                            @Part("contact") RequestBody contact,
                                            @Part("website") RequestBody website);

    @Multipart
    @POST("festival_category/company_detail_add")
    Call<CompanyDetails> updateBusinessDetails(@Part("api_token") RequestBody api_token,
                                               @Part("name") RequestBody name,
                                               @Part("email") RequestBody email,
                                               @Part MultipartBody.Part logo,
                                               @Part("address") RequestBody address,
                                               @Part("contact") RequestBody contact,
                                               @Part("company_id") RequestBody company_id,
                                               @Part("website") RequestBody website);


    @POST("video_category/video_category_list")
    Call<VideoHomeModel> getVideoCategoriesNameList(@Query("api_token") String api_token);

    @POST("video_category/category_video_list")
    Call<List_of_Video_Category_Name> getCatVideoList(@Query("api_token") String api_token, @Query("video_category_id") String video_category_id, @Query("language") String language);

    @POST("festival_category/festival_video_category_list")
//    @POST("category/category_list")
    Call<VideoHomeModel> festivaslVideoCatList(@Query("api_token") String api_token);


    @POST("festival_category/festival_video_list")
//    @POST("category/festival_video_list")
    Call<List_of_Video_Category_Name> festivalVideoList(@Query("api_token") String api_token, @Query("festival_video_category_id") String festival_id, @Query("language") String language);


    @POST("business_category/business_video_list")
//    @POST("business_category/business_video_list")
    Call<List_of_Video_Category_Name> businessVideoList(@Query("api_token") String api_token, @Query("business_video_category_id") String business_id, @Query("language") String language);


    @POST("business_category/business_image_category_list")
//    @POST("business_category/business_category_list")
    Call<VideoHomeModel> getBusinessCategoriesList(@Query("api_token") String api_token);

    @POST("business_category/business_video_category_list")
//    @POST("business_category/business_category_list")
    Call<VideoHomeModel> getBusinessCategoriesVideoList(@Query("api_token") String api_token);


    @POST("business_category/business_images_list")
//    @POST("business_category/business_category_images_list")
    Call<VideoHomeModel> getBusinessList(@Query("api_token") String api_token, @Query("business_image_category_id") String business_category_id, @Query("language") String language);


    @POST("greeting_category/greeting_category_list")
//    @POST("greeting_category/greeting_category_list")
    Call<VideoHomeModel> getGreetingCategoriesList(@Query("api_token") String api_token);


    @POST("greeting_category/greeting_images_list")
//    @POST("greeting_category/greeting_images_list")
    Call<VideoHomeModel> getGreetingImageList(@Query("api_token") String api_token, @Query("greeting_id") String greeting_id, @Query("language") String language);


}


