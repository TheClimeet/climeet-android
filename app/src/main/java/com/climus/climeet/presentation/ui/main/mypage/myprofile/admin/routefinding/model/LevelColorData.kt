package com.climus.climeet.presentation.ui.main.mypage.myprofile.admin.routefinding.model

object LevelColorData {
    private val colorNamesAndHexes = listOf(
        "하양" to "#FFFFFF",
        "빨강" to "#F34040",
        "주황" to "#FF9000",
        "노랑" to "#FDDA16",
        "초록" to "#63B75D",
        "하늘" to "#74D5FF",
        "파랑" to "#0094FF",
        "남색" to "#393FD6",
        "보라" to "#A259FF",
        "핑크" to "#FF74E9",
        "갈색" to "#6E4C41",
        "회색" to "#8B8B8B",
        "검정" to "#000000",
        "컴피" to "#BEDF22"
    )

    val COLORS = colorNamesAndHexes.map { (name, hex) -> RouteColor(name, hex) }

    val LEVELS = listOf(
        "VB",
        "V1",
        "V2",
        "V3",
        "V4",
        "V5",
        "V6",
        "V7",
        "V8",
        "V9+",
        "C"
    )

}