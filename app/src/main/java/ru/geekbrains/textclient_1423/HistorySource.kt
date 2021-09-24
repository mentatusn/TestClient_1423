package ru.geekbrains.textclient_1423

import android.content.*
import android.database.Cursor
import android.net.Uri
import android.provider.ContactsContract

class HistorySource(
    private val contentResolver: ContentResolver // Работаем с Content Provider через этот класс
) {

    private var cursor: Cursor? = null

    // Получаем запрос
    fun query() {
        cursor = contentResolver.query(HISTORY_URI,
            null,
            null,
            null,
            null)
    }

    fun getHistory() {
        // Отправляем запрос на получение таблицы с историей запросов и получаем ответ в виде Cursor
        cursor?.let { cursor ->
            for (i in 0..cursor.count) {
                // Переходим на позицию в Cursor
                if (cursor.moveToPosition(i)) {
                    // Берём из Cursor строку
                    HistoryEntityMapper.toEntity(cursor)
                }
            }
        }
        cursor?.close()
    }

    // Получаем данные о запросе по позиции
    fun getCityByPosition(position: Int): HistoryEntity {
        return if (cursor == null) {
            HistoryEntity()
        } else {
            cursor?.moveToPosition(position)
            HistoryEntityMapper.toEntity(cursor!!)
        }
    }

    // Добавляем новый город
    fun insert(entity: HistoryEntity) {
        contentResolver.insert(HISTORY_URI, HistoryEntityMapper.toContentValues(entity))
        query() // Снова открываем Cursor для повторного чтения данных
    }

    // Редактируем данные
    fun update(entity: HistoryEntity) {
        val uri: Uri = ContentUris.withAppendedId(HISTORY_URI, entity.id)
        contentResolver.update(uri, HistoryEntityMapper.toContentValues(entity), null, null)
        query() // Снова открываем Cursor для повторного чтения данных
    }

    // Удалить запись в истории запросов
    fun delete(entity: HistoryEntity) {
        val uri: Uri = ContentUris.withAppendedId(HISTORY_URI, entity.id)
        contentResolver.delete(uri, null, null)
        query() // Снова открываем Cursor для повторного чтения данных
    }

    companion object {
        // URI для доступа к Content Provider
        private val HISTORY_URI: Uri =
            Uri.parse("content://ru.geekbrains.lesson_1423_2_2_main.provider/HistoryEntity")
    }
}


object HistoryEntityMapper {
    private const val ID = "id"
    private const val NAME = "name"
    private const val TEMPERATURE = "temperature"

    fun toEntity(cursor: Cursor): HistoryEntity {
        return HistoryEntity(
            cursor.getLong(cursor.getColumnIndex(ID)),
            cursor.getString(cursor.getColumnIndex(NAME)),
            cursor.getInt(cursor.getColumnIndex(TEMPERATURE))
        )
    }

    fun toContentValues(entity: HistoryEntity): ContentValues {
        return ContentValues().apply {
            put(ID, entity.id)
            put(NAME, entity.name)
            put(TEMPERATURE, entity.temperature)
        }
    }
}



data class HistoryEntity(
    val id: Long=0,
    val name: String="",
    val temperature: Int = 0
)