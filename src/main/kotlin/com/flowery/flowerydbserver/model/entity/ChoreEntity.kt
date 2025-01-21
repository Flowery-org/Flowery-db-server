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

    @Column(name = "uid", length = 255, nullable = false)
    var uid: String, // Gardener ID

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sector_id", nullable = false)
    var sid: SectorEntity,

    @Column(name = "fid", length = 255)
    var fid: String? = null, // flower ID

    @Column(name = "content", length = 255)
    var content: String? = null,

    @Column(name = "finished", nullable = false)
    var finished: Boolean = false,

    @Column(name = "created_at", nullable = false)
    var createdAt: LocalDate = LocalDate.now(),

    @Column(name = "updated_at")
    var updatedAt: LocalDate? = null
)