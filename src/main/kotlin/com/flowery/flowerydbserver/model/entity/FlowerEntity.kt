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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "florography_id", nullable = false)
    val kid: FlorographyEntity, // Florography ID

    @Enumerated(EnumType.STRING)
    @Column(name = "color", nullable = false)
    val color: FlowerColor,

    @OneToMany(mappedBy = "gardener_flower",cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    val gardenerFlowers: List<GardenerFlowerEntity> = mutableListOf()

) {
    enum class FlowerColor {
        RED, YELLOW, BLUE, WHITE // 필요에 따라 추가
    }
}


