package com.flowery.flowerydbserver.repository

import com.flowery.flowerydbserver.model.document.ChoreDocument
import com.flowery.flowerydbserver.model.entity.ChoreEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface ChoreWriteRepository : JpaRepository<ChoreEntity, String>
interface ChoreReadRepository : MongoRepository<ChoreDocument, String>
