package com.flowery.flowerydbserver.repository

import com.flowery.flowerydbserver.model.document.GardenerDocument
import com.flowery.flowerydbserver.model.entity.GardenerEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface GardenerWriteRepository : JpaRepository<GardenerEntity, String> {
    abstract fun existsByIdent(ident: String): Boolean
    abstract fun existsByNickname(nickname: String): Boolean
    abstract fun existsByEmail(email: String): Boolean
}

interface GardenerReadRepository : MongoRepository<GardenerDocument, String>
