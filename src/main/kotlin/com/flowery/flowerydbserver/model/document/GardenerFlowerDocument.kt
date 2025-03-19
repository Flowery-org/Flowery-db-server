package com.flowery.flowerydbserver.model.document

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDate

@Document(collection = "gardener_flower")
data class GardenerFlowerDocument(
    @Id
    val id: String,
    val uid: String,
    val fid: String,
    // 유지: 날짜 필드를 사용하여 구분
    val createdAt: LocalDate,
    val updatedAt: LocalDate,
    val isBlossom: Boolean
)