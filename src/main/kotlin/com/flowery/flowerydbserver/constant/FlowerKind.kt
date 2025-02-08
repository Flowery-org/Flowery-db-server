package com.flowery.flowerydbserver.constant

enum class FlowerKind(val content: String) {
    ROSE("Love and Passion"),
    SUNFLOWER("Adoration and Loyalty");

    companion object {
        fun random(): FlowerKind {
            return values().random()
        }
    }
}