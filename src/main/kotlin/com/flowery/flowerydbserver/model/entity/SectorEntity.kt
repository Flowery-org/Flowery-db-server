package com.flowery.flowerydbserver.model.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
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

    @Column(name = "gid", length = 255, nullable = false)
    var gid: String, // Gardener ID

    @Column(name = "fid", length = 255)
    var fid: String? = null, // flower ID

    @Column(name = "date")
    var date: LocalDate? = null
)