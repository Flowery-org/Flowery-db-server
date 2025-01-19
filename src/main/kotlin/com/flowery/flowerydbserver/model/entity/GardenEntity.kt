package com.flowery.flowerydbserver.model.entity

import jakarta.persistence.*
import java.util.UUID

@Entity
@Table(name = "garden")
data class GardenEntity(
    @Id
    @Column(name = "id", length = 36, nullable = false)
    var id: String = UUID.randomUUID().toString(),

    @Column(name = "uid", length = 255, nullable = false)
    var uid: String, // Gardener ID

    @Column(name = "key", length = 255)
    var key: String? = null
)