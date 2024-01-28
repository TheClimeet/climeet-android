package com.climus.climeet.data.remote

import com.climus.climeet.data.model.response.GetSelectDateRecordResponse
import com.climus.climeet.data.model.response.SearchGymResponse
import com.climus.climeet.data.model.response.UploadImgResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query
import java.time.LocalDate

interface MainApi {

    @Multipart
    @POST("/file")
    suspend fun uploadImage(@Part file: MultipartBody.Part): Response<UploadImgResponse>

    @GET("/gym/search/all")
    suspend fun searchGym(
        @Query("gymname") gymName: String
    ): Response<List<SearchGymResponse>>

    @GET("/api/climbing-record/between-dates")
    suspend fun getSelectDateRecord(
        @Query("startDate") startDate: String,
        @Query("endDate") endDate: String
    ): Response<List<GetSelectDateRecordResponse>>
}