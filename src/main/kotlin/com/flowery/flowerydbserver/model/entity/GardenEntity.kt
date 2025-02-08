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
    var gardener: GardenerEntity, // Gardener ID

    @Column(name = "key", length = 255)
    var key: String? = null,

    @OneToMany(mappedBy = "garden", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    var sectors: List<SectorEntity> = mutableListOf()

)
