package com.flowery.flowerydbserver.projection

import com.flowery.flowerydbserver.model.document.GardenerDocument
import com.flowery.flowerydbserver.model.document.UserDocument
import com.flowery.flowerydbserver.repository.GardenerReadRepository
import com.flowery.flowerydbserver.repository.UserReadRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class GardenerProjection (
    @Autowired private val gardenerReadRepository: GardenerReadRepository
) {
    fun queryGardener(uid: String): GardenerDocument? {
        try{
            val ret = this.gardenerReadRepository.findById(uid)
            return ret.get()
        }catch (e: Exception){
            //* TODO: Need to add exception class
            println("HI")
            println(e.message)
        }
        return null
    }
}