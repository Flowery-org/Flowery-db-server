package com.flowery.flowerydbserver.model.dto

import org.springframework.data.annotation.Id


data class UserDto (
    @Id val id: String,
    val name: String,
)