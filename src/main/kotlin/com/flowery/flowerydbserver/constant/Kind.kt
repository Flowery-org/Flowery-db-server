package com.flowery.flowerydbserver.constant

enum class Kind(val content: String) {
    ROSE("Love and Passion"),
    SUNFLOWER("Adoration and Loyalty");

    companion object {
        fun random(): Kind {
            return values().random()
        }
    }
}