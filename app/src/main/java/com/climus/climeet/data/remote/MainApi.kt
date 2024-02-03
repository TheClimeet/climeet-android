package com.climus.climeet.data.remote

import com.climus.climeet.data.model.response.RefreshTokenResponse
import com.climus.climeet.data.model.response.SearchAvailableGymResponse
import com.climus.climeet.data.model.response.SearchGymResponse
import com.climus.climeet.data.model.response.ShortsListResponse
import com.climus.climeet.data.model.response.ShortsUpdatedFollowResponse
import com.climus.climeet.data.model.response.UploadImgResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface MainApi {

    @Multipart
    @POST("api/file")
    suspend fun uploadImage(@Part file: MultipartBody.Part): Response<UploadImgResponse>

    @GET("api/gym/search/all")
    suspend fun searchGym(
        @Query("gymname") gymName: String,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): Response<SearchGymResponse>

    @GET("api/gym/search")
    suspend fun searchAvailableGym(
        @Query("gymname") gymName: String,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): Response<SearchAvailableGymResponse>

    @GET("refresh-token")
    suspend fun refreshToken(
        @Query("refreshToken") refreshToken: String
    ): Response<RefreshTokenResponse>

    @GET("api/shorts/latest")
    suspend fun getRecentShorts(
        @Query("page") page: Int,
        @Query("size") size: Int
    ): Response<ShortsListResponse>

    @GET("api/shorts/popular")
    suspend fun getPopularShorts(
        @Query("page") page: Int,
        @Query("size") size: Int
    ): Response<ShortsListResponse>

    @GET("api/shorts/profile")
    suspend fun getShortsUpdatedFollow(): Response<List<ShortsUpdatedFollowResponse>>

}