package com.flowery.flowerydbserver.model.entity

import jakarta.persistence.*
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

    @OneToOne
    @JoinColumn(name = "gardener_id", nullable = false)
    var uid: GardenerEntity, // Gardener ID

    @Column(name = "key", length = 255)
    var key: String? = null,
    
    // 제거: sectors 리스트 (Sector 엔티티가 삭제됨)
    // 추가: 정원에 있는 모든 꽃들을 직접 참조하는 리스트
    @OneToMany(mappedBy = "uid", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    var gardenerFlowers: MutableList<GardenerFlowerEntity> = mutableListOf()
)