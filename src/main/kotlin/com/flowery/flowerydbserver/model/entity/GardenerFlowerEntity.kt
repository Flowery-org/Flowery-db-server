package com.flowery.flowerydbserver.model.entity

import jakarta.persistence.*
import org.hibernate.annotations.DynamicInsert
import org.hibernate.annotations.DynamicUpdate
import java.time.LocalDate
import java.util.*

@Entity
@Table(name = "gardener_flower")
@DynamicUpdate
@DynamicInsert
data class GardenerFlowerEntity(
    @Id
    @Column(name = "id", nullable = false, unique = true)
    val id: String = UUID.randomUUID().toString(),

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gardener_id", nullable = false)
    val gardener: GardenerEntity, // Gardener ID

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "flower_id")
    val flower: FlowerEntity, // Flower ID

    // createdAt 필드를 이용하여 꽃을 구분 (Sector 대체)
    @Column(name = "created_at", nullable = false)
    val createdAt: LocalDate = LocalDate.now(),

    @Column(name = "updated_at", nullable = false)
    var updatedAt: LocalDate = LocalDate.now(),

    @Column(name = "is_blossom", nullable = false)
    var isBlossom: Boolean = false,

    @OneToMany(mappedBy = "garden", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    var chores: List<ChoreEntity> = mutableListOf(),
)