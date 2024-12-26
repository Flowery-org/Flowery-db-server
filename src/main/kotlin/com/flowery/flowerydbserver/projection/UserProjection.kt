package com.flowery.flowerydbserver.projection

import com.flowery.flowerydbserver.model.document.UserDocument
import com.flowery.flowerydbserver.model.query.GetUserQuery
import com.flowery.flowerydbserver.repository.UserReadRepository
import com.google.gson.Gson
import org.springframework.amqp.core.Message
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UserProjection(
    @Autowired private val userReadRepository: UserReadRepository
) {
    fun queryUser(uid: String): UserDocument? {
        try{
            val ret = this.userReadRepository.findById(uid)
            return ret.get()
        }catch (e: Exception){
            //* TODO: Need to add exception class
            println(e.message)
        }

        return null
    }
}