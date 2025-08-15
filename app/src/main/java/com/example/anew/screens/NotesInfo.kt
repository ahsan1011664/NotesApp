package com.example.anew.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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

fun NotesInfo(navHostController: NavHostController, noteId: Int) {
     val context = LocalContext.current
     val appDatabase = AppDatabase.getInstance(context)
     val notesDao = appDatabase.notesDao()
     val noteRepo = NoteRepo(notesDao)
     val viewModel: NotesViewModel = viewModel(factory = NotesViewModelFactory(noteRepo))

     val note = viewModel.getNoteById(noteId).collectAsState(initial = null).value
     Text("Notes Info Screen", style = MaterialTheme.typography.headlineLarge)
     Spacer(modifier = Modifier.height(16.dp))

          Card(modifier = Modifier
               .fillMaxWidth()
               .padding(vertical = 4.dp)) {
               Row(
                    modifier = Modifier
                         .fillMaxWidth()
                         .padding(8.dp),
                    horizontalArrangement = Arrangement.End
               ) {
                    Column(modifier = Modifier.weight(1f)) {
                         if (note != null) {
                              Text(
                                   "Title: ${note.title}",
                                   style = MaterialTheme.typography.titleMedium
                              )
                              Spacer(Modifier.height(4.dp))
                              Text(
                                   "Content: ${note.content}",
                                   style = MaterialTheme.typography.bodyMedium
                              )
                         } else {
                              Text("Note not found")
                         }
                    }

                    Column(modifier = Modifier.wrapContentWidth(),
                         verticalArrangement = Arrangement.spacedBy(8.dp)) {
                         Button(onClick = {
                              navHostController.navigate("Update/${note?.id}")
                         }) {
                              Text("Update")
                         }
                         Spacer(modifier = Modifier.width(8.dp))
                         Button(onClick = {
                              // deletenot action
                              viewModel.deleteNote(note!!)
                         }) {
                              Text("Delete")
                         }
                    }
               }
          }
          }
