package com.flowery.flowerydbserver.model.request.gardener

data class CreateGardenerRequest(
    val ident: String,
    val password: String,
    val email: String,
    val name: String,
    val nickname: String
)