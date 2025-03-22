package com.flowery.flowerydbserver.model.command

// 기존 커맨드
data class CreateGardenerFlowerCommand(
    val uid: String,
    val fid: String,
    val isBlossom: Boolean
)

data class UpdateGardenerFlowerBlossomCommand(
    val uid: String, // gardener ID
    val fid: String,
    val isBlossom: Boolean
)

data class DeleteGardenerFlowerCommand(
    val uid: String, // gardener ID
    val fid: String, // flower ID
)

// 새로운 커맨드 - Sector 기능 대체

// 정원사의 꽃을 정원에 배치하는 커맨드
data class AssignGardenerFlowerToGardenCommand(
    val gfid: String,  // GardenerFlower ID
    val gid: String,   // Garden ID
    val date: String?  // 선택적 날짜 (null인 경우 createdAt 사용)
)

// 정원사의 꽃의 정원 배치를 업데이트하는 커맨드
data class UpdateGardenerFlowerGardenCommand(
    val gfid: String,  // GardenerFlower ID
    val gid: String?,  // Garden ID (변경할 Garden)
    val date: String?  // 선택적 날짜 (변경할 날짜)
)

// 정원사의 꽃을 정원에서 제거하는 커맨드
data class RemoveGardenerFlowerFromGardenCommand(
    val gfid: String   // GardenerFlower ID
)