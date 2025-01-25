import com.flowery.flowerydbserver.constant.FlowerColor
import com.flowery.flowerydbserver.constant.Kind
import com.flowery.flowerydbserver.model.entity.*
import jakarta.persistence.*
import org.hibernate.annotations.DynamicInsert
import org.hibernate.annotations.DynamicUpdate
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
}


