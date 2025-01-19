package com.flowery.flowerydbserver.model.entity

import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "sector")
data class SectorEntity(
    @Id
    @Column(name = "id", length = 36, nullable = false)
    var id: String = UUID.randomUUID().toString(),

    @Column(name = "gid", length = 255, nullable = false)
    var gid: String, // Gardener ID

    @Column(name = "fid", length = 255)
    var fid: String? = null, // flower ID

    @Column(name = "date")
    var date: LocalDateTime? = null
)