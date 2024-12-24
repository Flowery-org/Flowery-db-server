package com.flowery.flowerydbserver.model.entity

import jakarta.persistence.*


@Entity
@Table(name = "users")
class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    lateinit var id: String
    lateinit var name: String

}