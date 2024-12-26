package com.flowery.flowerydbserver.model.entity

import jakarta.persistence.*
import org.hibernate.annotations.DynamicInsert
import org.hibernate.annotations.DynamicUpdate


@Entity
@Table(name = "users")
@DynamicUpdate
@DynamicInsert
class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    lateinit var id: String

    @Column(nullable = false)
    lateinit var name: String
}