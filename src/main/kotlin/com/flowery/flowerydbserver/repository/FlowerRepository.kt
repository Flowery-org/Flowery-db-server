package com.flowery.flowerydbserver.repository


import FlowerEntity
import com.flowery.flowerydbserver.model.document.FlowerDocument
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface FlowerWriteRepository : JpaRepository<FlowerEntity, String>
interface FlowerReadRepository : MongoRepository<FlowerDocument, String>
