package com.flowery.flowerydbserver.model.entity

import com.flowery.flowerydbserver.constant.GardenerStatus
import jakarta.persistence.*
import org.hibernate.annotations.DynamicInsert
import org.hibernate.annotations.DynamicUpdate
import java.time.LocalDate
import java.util.*
@Entity
@Table(name = "gardener")
@DynamicUpdate
@DynamicInsert
data class GardenerEntity(
    @Id
    @Column(name = "id", length = 36, nullable = false)
    var id: String = UUID.randomUUID().toString(),
    
    @Column(name = "ident", length = 255, nullable = false)
    var ident: String,
    
    @Column(name = "password", length = 255, nullable = false)
    var password: String, // Hash Value
    
    @Column(name = "email", length = 255, nullable = false, unique = true)
    var email: String,
    
    @Column(name = "name", length = 255, nullable = false)
    var name: String,
    
    @Column(name = "nickname", length = 20, nullable = false, unique = true)
    var nickname: String,
    
    @Column(name = "token", length = 255)
    var token: String? = null,
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 255, nullable = false)
    var status: GardenerStatus = GardenerStatus.UNFINISHED,
    
    @Column(name = "created_at", nullable = false)
    var createdAt: LocalDate = LocalDate.now(),

    @Column(name = "updated_at", nullable = false)
    var updatedAt: LocalDate = LocalDate.now(),

    @OneToMany(mappedBy = "gardener", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    var gardenerFlowers: List<GardenerFlowerEntity> = mutableListOf(),
    
    // 수정: mappedBy 값을 "gardener"에서 "uid"로 변경
    @OneToMany(mappedBy = "uid", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    var chores: List<ChoreEntity> = mutableListOf(),
    
    // 수정: mappedBy 필드 추가
    @OneToOne(mappedBy = "uid", cascade = [CascadeType.ALL], fetch = FetchType.LAZY, orphanRemoval = true)
    var garden: GardenEntity? = null
)