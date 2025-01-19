package com.flowery.flowerydbserver.model.document

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDate

@Document(collection = "sector")
data class SectorDocument(
    @Id
    val id: String,
    val gid: String,
    val fid: String?,
    val date: LocalDate?
)