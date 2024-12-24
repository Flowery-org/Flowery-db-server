package com.flowery.flowerydbserver.model.document

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "users")
data class UserDocument (
    @Id val id: String,
    val name: String,
)