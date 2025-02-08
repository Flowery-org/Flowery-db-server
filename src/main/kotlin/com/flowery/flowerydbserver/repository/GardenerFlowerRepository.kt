package com.flowery.flowerydbserver.repository

import com.flowery.flowerydbserver.model.document.FlowerDocument
import com.flowery.flowerydbserver.model.document.GardenerDocument
import com.flowery.flowerydbserver.model.document.GardenerFlowerDocument
import com.flowery.flowerydbserver.model.entity.FlowerEntity
import com.flowery.flowerydbserver.model.entity.GardenerEntity
import com.flowery.flowerydbserver.model.entity.GardenerFlowerEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface GardenerFlowerWriteRepository : JpaRepository<GardenerFlowerEntity, String> {
    fun findByGardenerAndFlower(gardener: GardenerEntity, flower: FlowerEntity): GardenerFlowerEntity?
    fun existsByGardenerAndFlower(gardener: GardenerEntity, flower: FlowerEntity): Boolean
    fun deleteByGardenerAndFlower(gardener: GardenerEntity, flower: FlowerEntity)

}
interface GardenerFlowerReadRepository : MongoRepository<GardenerFlowerDocument, String> {
    fun findByGardenerIdAndFlowerId(gardenerId: String, flowerId: String): GardenerFlowerDocument?
    fun existsByGardenerIdAndFlowerId(gardenerId: String, flowerId: String): Boolean
    fun deleteByGardenerIdAndFlowerId(gardenerId: String, flowerId: String)
}
