package com.flowery.flowerydbserver.model.document

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.UUID

@Document(collection = "flower")
data class FlowerDocument(
    @Id
    val id: String,
    val kind: Kind,
    val color: Color,
    val content: String = kind.content
) {
    enum class Kind(val content: String) {
        ROSE("Love and Passion"),
        SUNFLOWER("Adoration and Loyalty");

        companion object {
            fun random(): Kind = values().random()
        }
    }

    enum class Color {
        RED, YELLOW, BLUE, WHITE;

        companion object {
            fun random(): Color = values().random()
        }
    }
}