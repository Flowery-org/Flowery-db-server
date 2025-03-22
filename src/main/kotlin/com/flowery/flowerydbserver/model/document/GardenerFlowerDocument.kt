package com.flowery.flowerydbserver.model.document

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDate

@Document(collection = "gardener_flower")
data class GardenerFlowerDocument(
    @Id
    val id: String,
    val gardenerId: String,
    val flowerId: String,
    val createdAt: LocalDate,
    val updatedAt: LocalDate,
    val isBlossom: Boolean,
    
    // Sector 기능 통합
    val gardenId: String? = null,
    val sectorDate: LocalDate? = null  // 별도 날짜가 지정된 경우 사용 (기본은 createdAt으로 날짜 구분)
)
