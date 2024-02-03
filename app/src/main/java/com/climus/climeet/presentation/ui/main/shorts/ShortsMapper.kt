package com.climus.climeet.presentation.ui.main.shorts

import com.climus.climeet.data.model.response.SearchAvailableGymItem
import com.climus.climeet.data.model.response.ShortsItem
import com.climus.climeet.presentation.ui.intro.signup.admin.model.SearchCragUiData
import com.climus.climeet.presentation.ui.main.shorts.model.ShortsThumbnailUiData
import com.climus.climeet.presentation.ui.main.shorts.model.ShortsUiData

fun SearchAvailableGymItem.toSearchCragUiData(
    keyword: String,
    onClickListener: (Long, String, String) -> Unit
) = SearchCragUiData(
    id = id,
    imgUrl = profileImageUrl,
    name = name,
    keyword = keyword,
    onClickListener = onClickListener
)

fun ShortsItem.toShortsThumbnailUiData(
    onClickListener: (Long) -> Unit
) = ShortsThumbnailUiData(
    shortsId = shortsId,
    thumbnailImg = thumbnailImageUrl,
    gymName = gymName,
    originLevelColor = shortsDetailInfo.gymDifficultyColor,
    climeetLevelColor = "#DDDDDD",
    onClickListener = onClickListener
)

fun ShortsItem.toShortsUiData() = ShortsUiData(
    shortsId = shortsId,
    thumbnailImg = thumbnailImageUrl,
    gymId = shortsDetailInfo.gymId,
    gymName = gymName,
    climeetLevelColor = "#FFFFFF",
    videoUrl = shortsDetailInfo.videoUrl,
    profileImgUrl = shortsDetailInfo.userShortsSimpleInfo.profileImgUrl,
    userName = shortsDetailInfo.userShortsSimpleInfo.profileName,
    description = shortsDetailInfo.description,
    sectorId = shortsDetailInfo.sectorId,
    sectorName = shortsDetailInfo.sectorName,
    sectorImgUrl = shortsDetailInfo.routeImageUrl,
    likeCount = shortsDetailInfo.likeCount,
    commentCount = shortsDetailInfo.commentCount,
    bookMarkCount = shortsDetailInfo.bookmarkCount,
    sharedCount = shortsDetailInfo.shareCount,
    isSoundEnabled = shortsDetailInfo.isSoundEnabled,
    isLiked = shortsDetailInfo.liked,
    isBookMarked = shortsDetailInfo.bookmarked
)