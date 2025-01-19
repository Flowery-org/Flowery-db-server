package com.flowery.flowerydbserver.model.entity

import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "chore")
data class ChoreEntity(
    @Id
    @Column(name = "id", length = 36, nullable = false)
    var id: String = UUID.randomUUID().toString(),

    @Column(name = "uid", length = 255, nullable = false)
    var uid: String, // Gardener ID

    @Column(name = "sid", length = 255, nullable = false)
    var sid: String, // sector ID

    @Column(name = "fid", length = 255)
    var fid: String? = null, // flower ID

    @Column(name = "content", length = 255)
    var content: String? = null,

    @Column(name = "finished", nullable = false)
    var finished: Boolean = false,

    @Column(name = "created_at", nullable = false)
    var createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "updated_at")
    var updatedAt: LocalDateTime? = null
)