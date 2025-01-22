package com.flowery.flowerydbserver.model.entity

import jakarta.persistence.*
import org.hibernate.annotations.DynamicInsert
import org.hibernate.annotations.DynamicUpdate
import java.util.*

@Entity
@Table(name = "florography")
@DynamicUpdate
@DynamicInsert
data class FlorographyEntity(

    @Id
    @Column(name = "id", length = 36, nullable = false)
    var id: String = UUID.randomUUID().toString(),

    @Enumerated(EnumType.STRING)
    @Column(name = "kind", nullable = false)
    var kind: FlowerKind,

    @Column(name = "content", length = 255, nullable = false)
    var content: String,

    ) {
    enum class FlowerKind {
        // 필요에 따라 추가
        ROSE,
        TULIP,
        SUNFLOWER
    }
}