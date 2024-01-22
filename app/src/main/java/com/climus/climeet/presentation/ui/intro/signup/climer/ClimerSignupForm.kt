package com.climus.climeet.presentation.ui.intro.signup.climer

import android.net.Uri
import com.climus.climeet.data.model.request.ClimbingLevel
import com.climus.climeet.data.model.request.ClimerSignupRequest
import com.climus.climeet.data.model.request.DiscoveryChannel
import com.climus.climeet.presentation.ui.intro.signup.climer.model.FollowCrag

object ClimerSignupForm {

    //todo
    // - 회원가입시, 모든 데이터를 임시 저장후, 최종단계에서 한번의 API 통신으로 회원가입
    // - 회원가입 데이터를 임시 저장하기 위한 싱글톤 Object

    val gymList: List<String> = emptyList()

    var token = ""
        private set

    var socialType = ""
        private set

    var nickName = ""
        private set

    var imageUri = ""
        private set

    var level: ClimbingLevel = ClimbingLevel.BEGINNER
        private set

    var way: DiscoveryChannel = DiscoveryChannel.INSTAGRAM_FACEBOOK
        private set

    var noticePermission: Boolean = false
        private set

    fun setToken(data: String) {
        token = data
    }

    fun setSocialType(data: String) {
        socialType = data
    }

    fun setNickName(data: String) {
        nickName = data
    }

    fun setImageUri(uri: String?) {
        imageUri = uri?.let{
            uri
        } ?: run{
            ""
        }
    }

    fun setLevel(climerLevel: Int) {
        when (climerLevel) {
            0 -> level = ClimbingLevel.BEGINNER
            1 -> level = ClimbingLevel.NOVICE
            2 -> level = ClimbingLevel.INTERMEDIATE
            3 -> level = ClimbingLevel.ADVANCED
            4 -> level = ClimbingLevel.EXPERT
        }
    }

    fun setWay(howToKnow: Int) {
        when (howToKnow) {
            0 -> way = DiscoveryChannel.INSTAGRAM_FACEBOOK
            1 -> way = DiscoveryChannel.YOUTUBE
            2 -> way = DiscoveryChannel.FRIEND_RECOMMENDATION
            3 -> way = DiscoveryChannel.BLOG_CAFE_COMMUNITY
            4 -> way = DiscoveryChannel.OTHER
        }
    }

    fun setNotice(permit: Boolean) {
        noticePermission = permit
    }

    fun toSignupRequest(): ClimerSignupRequest {
        return ClimerSignupRequest(
            nickName = this.nickName,
            climbingLevel = this.level,
            discoveryChannel = this.way,
            profileImgUrl = this.imageUri,
            gymFollowList = this.gymList,
            isAllowFollowNotification = this.noticePermission,
            isAllowLikeNotification = this.noticePermission,
            isAllowCommentNotification = this.noticePermission,
            isAllowAdNotification = true
        )
    }
}