package com.flowery.flowerydbserver.model.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.DynamicInsert
import org.hibernate.annotations.DynamicUpdate
import java.util.*

@Entity
@Table(name = "garden")
@DynamicUpdate
@DynamicInsert
data class GardenEntity(
    @Id
    @Column(name = "id", length = 36, nullable = false)
    var id: String = UUID.randomUUID().toString(),

    @Column(name = "uid", length = 255, nullable = false)
    var uid: String, // Gardener ID

    @Column(name = "key", length = 255)
    var key: String? = null
)