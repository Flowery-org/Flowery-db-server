package com.flowery.flowerydbserver.model.command

data class CreateSectorCommand(
    val gid: String,   // Garden ID
    val gfid: String,  // GardenerFlower ID
    val date: String?  // 예: "2025-02-01"
)

data class UpdateSectorCommand(
    val sectorId: String,
    val gid: String?,   // Garden 교체 가능
    val gfid: String?,  // GardenerFlower 교체 가능
    val date: String?
)

data class DeleteSectorCommand(
    val sectorId: String
)
