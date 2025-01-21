import com.flowery.flowerydbserver.model.entity.FlorographyEntity
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

    @Column(name = "uid", nullable = false)
    val uid: String,

    @OneToOne(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    @JoinColumn(name = "florography_id", nullable = false)
    val fid: FlorographyEntity, // Florography ID

    @Enumerated(EnumType.STRING)
    @Column(name = "color", nullable = false)
    val color: FlowerColor,

    @Column(name = "created_at", nullable = false)
    val createdAt: LocalDate = LocalDate.now(),

    @Column(name = "updated_at", nullable = false)
    val updatedAt: LocalDate = LocalDate.now()
) {
    enum class FlowerColor {
        RED, YELLOW, BLUE, WHITE // 필요에 따라 추가
    }
}


