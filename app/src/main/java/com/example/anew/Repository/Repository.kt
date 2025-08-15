package com.example.anew.Repository

import com.example.anew.data.Note
import com.example.anew.data.NotesDao
import com.example.anew.data.User
import com.example.anew.data.UserDao
import kotlinx.coroutines.flow.Flow

class Repository(private val userDao: UserDao) {
    suspend fun registerUser(user: String, password: String, email: String): String? {
        if (user.isBlank() || password.isBlank() || email.isBlank()) {
            return "All Field Required"
        }
        if (userDao.getUserByUsername(user) != null) {
            return "Username Already Exist"
        }
        if (userDao.getUserByEmail(email) != null) {
            return "Email Already Exist"
        }
        val newUser = User(username = user, password = password, email = email)
        userDao.insertUser(newUser)
        return null
    }
    suspend fun LoginUser(email: String, password: String): User? {
        val user = userDao.getUserByEmail(email) ?: return null
        return if (user.password == password) user else null
    }

}
class NoteRepo(private val notesDao: NotesDao){
    val allNotes = notesDao.getallnotes()
    suspend fun insertNote(note: Note) = notesDao.insertNote(note)
    fun getNotesForUser(userId: Int) = notesDao.getNotesForUser(userId)
    suspend fun deleteNote(note: Note) = notesDao.deleteNote(note)
    suspend fun updateNote(note: Note) = notesDao.updateNote(note)
    fun getNoteById(id: Int): Flow<Note?> {
        return notesDao.getNoteById(id)
    }


}