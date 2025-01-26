package com.flowery.flowerydbserver.model.request.gardener
data class UpdateGardenerStatusRequest(
    val id: String,
    val status: String
)

data class UpdateGardenerPasswordRequest(
    val id: String,
    val ident: String,
    val password: String,
    val newPassword: String
)

data class UpdateGardenerNameRequest(
    val id: String,
    val name: String
)

data class UpdateGardenerNicknameRequest(
    val id: String,
    val nickname: String
)

data class DeleteGardenerRequest(
    val id: String
)

