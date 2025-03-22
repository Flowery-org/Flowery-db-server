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

    // Sector 기능을 통합: Garden 연결
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "garden_id")
    var garden: GardenEntity? = null,

    // Sector의 date 기능 통합 (기본적으로 createdAt 사용)
    @Column(name = "sector_date")
    var sectorDate: LocalDate? = null,

    // 수정: mappedBy 값이 ChoreEntity의 gardenerFlower 필드와 일치하는지 확인
    @OneToMany(mappedBy = "gardenerFlower", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    var chores: List<ChoreEntity> = mutableListOf()
)
