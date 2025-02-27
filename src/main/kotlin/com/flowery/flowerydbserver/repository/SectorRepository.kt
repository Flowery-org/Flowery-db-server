package com.flowery.flowerydbserver.repository

import com.flowery.flowerydbserver.model.document.SectorDocument
import com.flowery.flowerydbserver.model.entity.SectorEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface SectorWriteRepository : JpaRepository<SectorEntity, String>
interface SectorReadRepository : MongoRepository<SectorDocument, String>