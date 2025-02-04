package com.flowery.flowerydbserver.model.entity

import FlowerEntity
import jakarta.persistence.*
import org.hibernate.annotations.DynamicInsert
import org.hibernate.annotations.DynamicUpdate
import java.time.LocalDate
import java.util.*

@Entity
@Table(name = "sector")
@DynamicUpdate
@DynamicInsert
data class SectorEntity(
    @Id
    @Column(name = "id", length = 36, nullable = false)
    var id: String = UUID.randomUUID().toString(),

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "garden_id")
    var garden: GardenEntity,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gardenerFlower_id", nullable = false)
    var gardenerFlower: GardenerFlowerEntity,

    @Column(name = "date")
    var date: LocalDate = LocalDate.now()
)
