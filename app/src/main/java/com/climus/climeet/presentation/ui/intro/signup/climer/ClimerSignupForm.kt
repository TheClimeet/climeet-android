package com.climus.climeet.presentation.ui.intro.signup.climer

import com.climus.climeet.data.model.request.ClimbingLevel
import com.climus.climeet.data.model.request.ClimerSignupRequest
import com.climus.climeet.data.model.request.DiscoveryChannel

object ClimerSignupForm {

    var token = ""
        private set

    var socialType = ""
        private set

    var nickName = ""
        private set

    private val gymList: List<String> = emptyList()
    private var imageUrl = ""
    private var level: ClimbingLevel = ClimbingLevel.BEGINNER
    private var way: DiscoveryChannel = DiscoveryChannel.INSTAGRAM_FACEBOOK
    private var noticePermission: Boolean = false

    fun setToken(data: String) {
        token = "Bearer $data"
    }

    fun setSocialType(data: String) {
        socialType = data
    }

    fun setNickName(data: String) {
        nickName = data
    }

    fun setImageUrl(url: String) {
        imageUrl = url
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
            profileImgUrl = this.imageUrl,
            gymFollowList = this.gymList,
            isAllowFollowNotification = this.noticePermission,
            isAllowLikeNotification = this.noticePermission,
            isAllowCommentNotification = this.noticePermission,
            isAllowAdNotification = this.noticePermission,
        )
    }
}