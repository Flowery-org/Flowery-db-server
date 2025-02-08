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
    val isBlossom: Boolean
)