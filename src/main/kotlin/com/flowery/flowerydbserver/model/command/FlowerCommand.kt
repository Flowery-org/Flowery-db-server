package com.flowery.flowerydbserver.model.command

import FlowerEntity

data class CreateFlowerCommand(
    val color: FlowerEntity.FlowerColor,
    val kind: FlowerEntity.Kind,
    val content: String
)
data class DeleteFlowerCommand(
    val id: String
)