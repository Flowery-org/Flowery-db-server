package com.flowery.flowerydbserver.repository

import com.flowery.flowerydbserver.model.document.GardenerFlowerDocument
import com.flowery.flowerydbserver.model.entity.GardenerFlowerEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface GardenerFlowerWriteRepository : JpaRepository<GardenerFlowerEntity, String>
interface GardenerFlowerReadRepository : MongoRepository<GardenerFlowerDocument, String>
