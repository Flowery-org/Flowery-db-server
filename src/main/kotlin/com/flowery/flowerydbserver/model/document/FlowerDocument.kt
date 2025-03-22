package com.flowery.flowerydbserver.model.document

import com.flowery.flowerydbserver.constant.FlowerColor
import com.flowery.flowerydbserver.constant.FlowerKind
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "flower")
data class FlowerDocument(
    @Id
    val id: String,
    val kind: FlowerKind,
    val color: FlowerColor,
    val content: String = kind.content
)