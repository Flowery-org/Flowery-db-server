package com.flowery.flowerydbserver.model.entity

import FlowerEntity
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
    val uid: GardenerEntity, // Gardener ID

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "flower_id")
    val fid: FlowerEntity, // Flower ID

    @Column(name = "created_at", nullable = false)
    val createdAt: LocalDate = LocalDate.now(),

    @Column(name = "updated_at", nullable = false)
    val updatedAt: LocalDate = LocalDate.now(),

    @Column(name = "is_blossom", nullable = false)
    val isBlossom: Boolean = false,

    @OneToMany(mappedBy = "gardener_flower", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    var chores: List<ChoreEntity> = mutableListOf(),

    @OneToOne(fetch = FetchType.LAZY)
    val sector: SectorEntity

)