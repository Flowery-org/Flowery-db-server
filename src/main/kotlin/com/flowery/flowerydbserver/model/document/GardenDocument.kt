package com.flowery.flowerydbserver.model.document

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "garden")
data class GardenDocument(
    @Id
    val id: String,
    val gardenerId: String,  // GardenerEntity ID
    val key: String?
)
