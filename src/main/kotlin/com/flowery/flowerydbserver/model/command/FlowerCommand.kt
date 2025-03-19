package com.flowery.flowerydbserver.model.command

import com.flowery.flowerydbserver.constant.FlowerColor
import com.flowery.flowerydbserver.constant.FlowerKind

data class CreateFlowerCommand(
    val color: FlowerColor,
    val kind: FlowerKind,
    val content: String,
    val uid: String
)
data class DeleteFlowerCommand(
    val uid: String,
    val fid: String
)