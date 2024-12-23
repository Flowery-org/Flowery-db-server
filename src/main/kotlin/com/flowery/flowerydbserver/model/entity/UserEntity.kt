package com.flowery.flowerydbserver.model.entity

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "users")
class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    lateinit var id: UUID
    lateinit var name: String
}