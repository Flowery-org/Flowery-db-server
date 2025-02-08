package com.flowery.flowerydbserver.model.document

import com.flowery.flowerydbserver.constant.GardenerStatus
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDate
import java.util.*

@Document(collection = "gardener")
data class GardenerDocument(

    @Id
    val id: String,
    val ident: String,
    val password: String,
    val email: String,
    val name: String,
    val nickname: String,
    val token: String? = null,
    val status: GardenerStatus,
    val createdAt: LocalDate,
    val updatedAt: LocalDate,
)
