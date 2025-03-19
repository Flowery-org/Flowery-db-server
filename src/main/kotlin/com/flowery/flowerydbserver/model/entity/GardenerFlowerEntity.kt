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

    // createdAt 필드를 이용하여 꽃을 구분 (Sector 대체)
    @Column(name = "created_at", nullable = false)
    val createdAt: LocalDate = LocalDate.now(),

    @Column(name = "updated_at", nullable = false)
    val updatedAt: LocalDate = LocalDate.now(),

    @Column(name = "is_blossom", nullable = false)
    val isBlossom: Boolean = false,

    // mappedBy 값 수정하여 ChoreEntity와의 관계 정의
    @OneToMany(mappedBy = "gfid", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    var chores: List<ChoreEntity> = mutableListOf()
    
    // Sector 엔티티 참조 제거
)