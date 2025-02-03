package com.flowery.flowerydbserver.constant
enum class FlowerColor {
    RED, YELLOW, BLUE, WHITE; // 필요에 따라 추가
    companion object {
        fun random(): FlowerColor {
            return values().random()
        }
    }
}