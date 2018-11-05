package ru.rpuxa.bomjserver.server

import ru.rpuxa.bomjserver.News
import ru.rpuxa.bomjserver.Review
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.time.LocalDateTime
import java.util.*

internal object DataBase {
    private const val STATISTIC_FILE = "statistic.bomj"

    private var data = HashMap<Long, Map<Int, Any>>()
    private var reviews = ArrayList<Review>()
    internal var news = ArrayList<News>()

    fun saveStatistic(statistic: Map<Int, Any>) {
        val id = statistic[0] as Long
        data[id] = statistic
    }

    fun saveReview(review: Review) {
        reviews.add(review)
    }

    fun addNews(text: String) {
        val date = LocalDateTime.now()
        val d = date.year or (date.month.value shl 14) or (date.dayOfMonth shl 21)
        news.add(0, News(text, d))
    }

    @Suppress("UNCHECKED_CAST")
    fun loadFromFile() {
        try {
            ObjectInputStream(FileInputStream(STATISTIC_FILE)).use {
                data = it.readObject() as HashMap<Long, Map<Int, Any>>
                reviews = it.readObject() as ArrayList<Review>
                news = it.readObject() as ArrayList<News>
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun saveToFile() {
        try {
            ObjectOutputStream(FileOutputStream(STATISTIC_FILE)).use { out ->
                out.writeObject(data)
                out.writeObject(reviews)
                out.writeObject(news)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
}
