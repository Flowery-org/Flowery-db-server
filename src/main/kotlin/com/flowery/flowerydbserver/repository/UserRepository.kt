package com.flowery.flowerydbserver.repository

import com.flowery.flowerydbserver.model.document.UserDocument
import com.flowery.flowerydbserver.model.entity.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.mongodb.repository.MongoRepository

interface UserWriteRepository: JpaRepository<UserEntity, String>
interface UserReadRepository: MongoRepository<UserDocument, String>