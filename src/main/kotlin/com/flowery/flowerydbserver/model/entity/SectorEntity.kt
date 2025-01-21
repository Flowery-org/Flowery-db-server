package com.flowery.flowerydbserver.model.entity

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
    @JoinColumn(name = "garden_id", nullable = false)
    var gid: GardenEntity,

    @Column(name = "fid", length = 255)
    var fid: String? = null, // flower ID

    @Column(name = "date")
    var date: LocalDate? = null,

    @OneToMany(mappedBy = "sector", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    var chores: MutableList<ChoreEntity> = mutableListOf()
)