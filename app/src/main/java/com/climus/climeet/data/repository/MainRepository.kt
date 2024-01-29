package com.climus.climeet.data.repository

import com.climus.climeet.data.model.BaseState
import com.climus.climeet.data.model.response.BestClearClimberSimpleResponse
import com.climus.climeet.data.model.response.BestFollowGymSimpleResponse
import com.climus.climeet.data.model.response.BestLevelCimberSimpleResponse
import com.climus.climeet.data.model.response.BestRouteSimpleResponse
import com.climus.climeet.data.model.response.BestTimeClimberSimpleResponse
import com.climus.climeet.data.model.response.SearchGymResponse
import com.climus.climeet.data.model.response.ShortsSimpleResponse
import com.climus.climeet.data.model.response.UploadImgResponse
import okhttp3.MultipartBody
import retrofit2.http.Header
import retrofit2.http.Query

interface MainRepository {
    suspend fun uploadImage(
        image: MultipartBody.Part
    ): BaseState<UploadImgResponse>

    suspend fun searchGym(
        gymName: String
    ): BaseState<List<SearchGymResponse>>

    suspend fun findClimberRankingOrderClearCount(@Header("Authorization") accessToken : String): BaseState<List<BestClearClimberSimpleResponse>>

    suspend fun findClimberRankingOrderTime(@Header("Authorization") accessToken : String): BaseState<List<BestTimeClimberSimpleResponse>>

    suspend fun findClimberRankingOrderLevel(@Header("Authorization") accessToken : String):  BaseState<List<BestLevelCimberSimpleResponse>>

    suspend fun findPopularShorts(
        @Header("Authorization") accessToken : String,
        @Query("page") page : Int,
        @Query("size") size : Int
    ): BaseState<List<ShortsSimpleResponse>>

    suspend fun findGymRankingOrderFollowCount(@Header("Authorization") accessToken : String) : BaseState<List<BestFollowGymSimpleResponse>>

    suspend fun findRouteRankingOrderSelectionCount(@Header("Authorization") accessToken : String) : BaseState<List<BestRouteSimpleResponse>>
}