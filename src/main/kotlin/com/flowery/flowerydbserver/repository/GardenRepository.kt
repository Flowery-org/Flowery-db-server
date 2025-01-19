package com.flowery.flowerydbserver.repository

import com.flowery.flowerydbserver.model.entity.GardenEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface GardenWriteRepository : JpaRepository<GardenEntity, String>
interface GardenREadRepository : MongoRepository<GardenEntity, String>