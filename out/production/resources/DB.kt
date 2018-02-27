import com.kotlinmeetup.repositories.ArticlesRepository
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

fun dbCreate() {
    transaction {
        SchemaUtils.create (ArticlesRepository.Articles)
    }
}

fun dbDrop() {
    SchemaUtils.drop(ArticlesRepository.Articles)
}