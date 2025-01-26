package com.flowery.flowerydbserver.model.command

data class CreateSectorCommand(
    val fid: String,   // Flower ID
    val gfid: String,  // GardenerFlower ID
    val date: String?  // "2025-02-01" 등
)

data class UpdateSectorCommand(
    val sectorId: String,
    val fid: String?,  // 변경될 Flower ID
    val gfid: String?, // 변경될 GardenerFlower ID
    val date: String?
)

data class DeleteSectorCommand(
    val sectorId: String
)