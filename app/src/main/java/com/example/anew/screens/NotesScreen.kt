package com.example.anew.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.anew.Repository.NoteRepo
import com.example.anew.data.AppDatabase
import com.example.anew.data.Constants.userID
import com.example.anew.viewmodel.NotesViewModel
import com.example.anew.viewmodel.NotesViewModelFactory
@Composable
fun NotesScreen(navHostController: NavHostController, userId: Int, username: String) {
    val context = LocalContext.current
    val appDatabase = AppDatabase.getInstance(context)
    val notesDao = appDatabase.notesDao()
    val notesrepo = NoteRepo(notesDao)
    val viewModel: NotesViewModel = viewModel(factory = NotesViewModelFactory(notesrepo))
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    val notes by viewModel.getNotesForUser(userId).collectAsState(initial = emptyList())

    Column(modifier = Modifier.fillMaxSize()) {
        Text("Notes App")
        Spacer(modifier = Modifier.height(16.dp))
         Text("Welcome, $username")
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = title, onValueChange = { title = it }, label = { Text("Title") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = content, onValueChange = { content = it }, label = { Text("Content") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            if (title.isNotBlank() && content.isNotBlank()) {
                viewModel.addNote(userId, title, content)

                title = ""
                content = ""
            }
        }) {
            Text("Save")

        }
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(notes) { note ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Row(modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                    )
                    {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(note.title, style = MaterialTheme.typography.titleMedium)
                            Spacer(Modifier.height(4.dp))
                            Text(note.content, style = MaterialTheme.typography.bodyMedium)
                        }
                        Button(onClick = {
                            navHostController.navigate("Info/${note.id}")
                        }) {
                            Text("Change")
                        }
                    }
                }

            }

        }


    }
}