package com.kotlinmeetup.repositories

import com.kotlinmeetup.models.Article
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class ArticlesRepository {

    init {
        Setup()
    }

    object Articles : Table() {
        val id = long("id").autoIncrement().primaryKey()
        val title = varchar("title", 512)
        val content = varchar( "content", 512)
    }

    fun ResultRow.toUser() : Article {

        return Article(
            title = this[Articles.title],
            content = this[Articles.content]
        )
    }

    fun get(id: Long) : Article = transaction {
        Articles.select({ Articles.id eq id }).limit(1).map { it.toUser() }.first()
    }

    fun index(): ArrayList<Article> {
        val articles: ArrayList<Article> = arrayListOf()
        transaction {
            Articles.selectAll().map {
                articles.add(Article(id = it[Articles.id], title = it[Articles.title], content = it[Articles.content])) }
        }
        return articles
    }

    fun create(article: Article): Article {
        val articleId = transaction {
            Articles.insert {
                it[Articles.title] = article.title
                it[Articles.content] = article.content
            }.generatedKey
        }
        return article.copy(id = articleId!!.toLong())
    }

    fun show(id: Long): Article {
        return transaction {
            Articles.select { Articles.id eq id }
                    .map { Article(id = it[Articles.id], title = it[Articles.title], content = it[Articles.content]) }
                    .first()
        }
    }

    fun update(id: Long, newArticle:Article): Article {
        transaction {
            Articles.update({ Articles.id eq id }) {
                it[title] = newArticle.title
                it[content] = newArticle.content
            }
        }
        return Article(id = id, title = newArticle.title, content = newArticle.content)
    }

    fun delete(id: Int) {
        transaction {
            Articles.deleteWhere { Articles.id eq id }
        }
    }
}