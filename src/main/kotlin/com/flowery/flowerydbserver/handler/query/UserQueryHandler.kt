package com.flowery.flowerydbserver.handler.query

import com.flowery.flowerydbserver.model.document.UserDocument
import com.flowery.flowerydbserver.model.query.GetUserQuery
import com.flowery.flowerydbserver.repository.UserReadRepository
import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UserQueryHandler(
    @Autowired private val userReadRepository: UserReadRepository
) {
//    @QueryHandler
//    fun handle(query: GetUserQuery): UserDocument? {
//        return this.userReadRepository.findById(query.id).orElse(null)
//    }
}