package com.flowery.flowerydbserver.repository

import com.flowery.flowerydbserver.constant.FlowerColor
import com.flowery.flowerydbserver.constant.FlowerKind
import com.flowery.flowerydbserver.model.document.FlowerDocument
import com.flowery.flowerydbserver.model.entity.FlowerEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface FlowerWriteRepository : JpaRepository<FlowerEntity, String> {
    fun findByColorAndKind(color: FlowerColor, kind: FlowerKind): FlowerEntity?
}
interface FlowerReadRepository : MongoRepository<FlowerDocument, String>
