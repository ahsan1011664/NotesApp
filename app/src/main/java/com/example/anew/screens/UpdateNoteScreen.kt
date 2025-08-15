package com.example.anew.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.anew.Repository.NoteRepo
import com.example.anew.data.AppDatabase
import com.example.anew.viewmodel.NotesViewModel
import com.example.anew.viewmodel.NotesViewModelFactory


@Composable
fun UpdateNoteScreen(navController: NavHostController, noteId: Int) {
    val context = LocalContext.current
    val appDatabase = AppDatabase.getInstance(context)
    val notesDao = appDatabase.notesDao()
    val noteRepo = NoteRepo(notesDao)
    val viewModel: NotesViewModel = viewModel(factory = NotesViewModelFactory(noteRepo))
    val note = viewModel.getNoteById(noteId).collectAsState(initial = null).value
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    LaunchedEffect(note) {
        if (note != null) {
            title = note.title
            content = note.content
        }

    }
    Column(
        modifier = Modifier.fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)

    ) {
        Text("Update Note", style = MaterialTheme.typography.headlineLarge)

        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Title") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = content,
            onValueChange = { content = it },
            label = { Text("Content") },
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp)
        )
        Button(
            onClick = {
                val updatedNote = note!!.copy(title = title, content = content)
               viewModel.updateNote(updatedNote)

            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save Changes")
        }

    }

}