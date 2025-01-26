package com.flowery.flowerydbserver.model.entity

import jakarta.persistence.*
import org.hibernate.annotations.DynamicInsert
import org.hibernate.annotations.DynamicUpdate
import java.time.LocalDate
import java.util.*

@Entity
@Table(name = "chores")
@DynamicUpdate
@DynamicInsert
data class ChoreEntity(
    @Id
    @Column(name = "id", length = 36, nullable = false)
    var id: String = UUID.randomUUID().toString(),

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gardener_id", nullable = false)
    var garden: GardenEntity, // Gardener ID

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gardenerflower_id", nullable = false)
    var gardenerFlower: GardenerFlowerEntity,

    @Column(name = "content", length = 255)
    var content: String,

    @Column(name = "finished", nullable = false)
    var finished: Boolean = false,

    @Column(name = "created_at", nullable = false)
    var createdAt: LocalDate = LocalDate.now(),

    @Column(name = "updated_at")
    var updatedAt: LocalDate = LocalDate.now()
)