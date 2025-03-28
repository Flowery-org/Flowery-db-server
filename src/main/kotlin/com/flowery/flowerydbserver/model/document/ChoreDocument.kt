package com.flowery.flowerydbserver.model.document

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDate

@Document(collection = "chore")
data class ChoreDocument(
    @Id
    val id: String,
    val gid: String,   // GardenEntity ID
    val gfid: String,  // GardenerFlowerEntity ID
    val content: String,
    val finished: Boolean,
    val createdAt: LocalDate,
    val updatedAt: LocalDate
)
