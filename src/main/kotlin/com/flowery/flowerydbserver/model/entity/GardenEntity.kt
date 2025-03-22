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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gardener_id", nullable = false)
    var gardener: GardenerEntity, // Gardener ID

    @Column(name = "key", length = 255)
    var key: String? = null,
    
    // GardenerFlower와의 관계 (Sector 대체)
    @OneToMany(mappedBy = "garden", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    var gardenerFlowers: MutableList<GardenerFlowerEntity> = mutableListOf()
)