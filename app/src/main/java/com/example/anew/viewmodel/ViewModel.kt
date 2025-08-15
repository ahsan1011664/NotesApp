package com.example.anew.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.anew.Repository.NoteRepo
import com.example.anew.Repository.Repository
import com.example.anew.data.Note
import com.example.anew.data.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AuthViewModel(private val repository: Repository) : ViewModel() {
    // null = nothing yet, error message = failure, "Registration successful" = success
    private val _registration = mutableStateOf<String?>(null)
    private val _login = mutableStateOf<String?>(null)
    val login = _login
    val registration = _registration

    fun registerUser(user: String, password: String, email: String) {
        viewModelScope.launch {
            val result = repository.registerUser(user, password, email)
            _registration.value = result ?: "Registration successful"
        }
    }
    fun login(email: String, password: String, onSuccessfulLogin: (User) -> Unit) {
        viewModelScope.launch {
            val result = repository.LoginUser(email, password)
            if (result != null) {
                _login.value = "Login successful"
                onSuccessfulLogin(result)
            } else {
                _login.value = "Invalid Credentials"
            }
        }
    }

    fun clearRegistration() {
        _registration.value = null
    }
}

class NotesViewModel(private val repository: NoteRepo) : ViewModel() {
    var allNotes: StateFlow<List<Note>> = repository.allNotes
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()

        )
    fun addNote(UserId: Int, title: String, content: String){
        viewModelScope.launch {
            val newNote = Note(userId = UserId, title = title, content = content)
            repository.insertNote(newNote)
        }

    }

    fun getNotesForUser(userId: Int) = repository.getNotesForUser(userId)
    fun deleteNote(note: Note){
        viewModelScope.launch {
            repository.deleteNote(note)
        }
    }
    fun getNoteById(id: Int): Flow<Note?> {
        return repository.getNoteById(id)
    }
    fun updateNote(note: Note) {
        viewModelScope.launch {
            repository.updateNote(note)
        }
    }

}



class AuthViewModelFactory(
    private val repository: Repository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            return AuthViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
class NotesViewModelFactory(
    private val repository: NoteRepo
) : androidx.lifecycle.ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NotesViewModel::class.java)) {
            return NotesViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
