package com.example.anew.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface NotesDao {
@Query("SELECT * FROM notes  ORDER BY id DESC")
fun getallnotes() : Flow<List<Note>>
@Insert
suspend fun insertNote(note: Note)
@Delete
suspend fun deleteNote(note: Note)

@Update
suspend fun updateNote(note: Note)
@Query("SELECT * FROM notes WHERE id = :id")
fun getNoteById(id: Int): Flow<Note>

@Query("SELECT * FROM notes WHERE userId = :userId ORDER BY id DESC")
    fun getNotesForUser(userId: Int): Flow<List<Note>>

}

