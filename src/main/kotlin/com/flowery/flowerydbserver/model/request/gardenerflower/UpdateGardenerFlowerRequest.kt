package com.flowery.flowerydbserver.model.request.gardenerflower

// 기존 Request 클래스
data class UpdateGardenerFlowerBlossomRequest(
    val uid: String,
    val fid: String,
    val isBlossom: Boolean
)

// 새로운 Request 클래스 - Sector 기능 대체

// 정원사의 꽃을 정원에 배치하는 요청
data class AssignGardenerFlowerToGardenRequest(
    val gfid: String,  
    val gid: String,   
    val date: String?  
)

// 정원사의 꽃의 정원 배치를 업데이트하는 요청
data class UpdateGardenerFlowerGardenRequest(
    val gid: String?,  
    val date: String?  
)