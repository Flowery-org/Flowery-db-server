import com.flowery.flowerydbserver.model.entity.*
import jakarta.persistence.*
import org.hibernate.annotations.DynamicInsert
import org.hibernate.annotations.DynamicUpdate
import java.time.LocalDate
import java.util.UUID

@Entity
@Table(name = "flower")
@DynamicUpdate
@DynamicInsert
data class FlowerEntity(

    @Id
    @Column(name = "id", nullable = false, unique = true)
    val id: String = UUID.randomUUID().toString(),

    @Enumerated(EnumType.STRING)
    @Column(name = "color", nullable = false)
    val color: FlowerColor,

    @Enumerated(EnumType.STRING)
    @Column(name = "kind", nullable = false, unique = true)
    val kind: Kind,

    @Column(name = "content", nullable = false, unique = true)
    val content: String = kind.content,

    @OneToMany(mappedBy = "gardener_flower",cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    val gardenerFlowers: List<GardenerFlowerEntity> = mutableListOf()

) {
    enum class FlowerColor {
        RED, YELLOW, BLUE, WHITE; // 필요에 따라 추가
        companion object {
            fun random(): FlowerColor {
                return values().random()
            }
        }
    }

    enum class Kind(val content: String) {
        ROSE("Love and Passion"),
        SUNFLOWER("Adoration and Loyalty");

        companion object {
            fun random(): Kind {
                return values().random()
            }
        }
    }
}


