package com.mahmood.notesappusingfirebase

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.firebase.firestore.FirebaseFirestore
import com.mahmood.notesappusingfirebase.ui.theme.ColorBlack
import com.mahmood.notesappusingfirebase.ui.theme.ColorGrey
import com.mahmood.notesappusingfirebase.ui.theme.ColorLightGrey
import com.mahmood.notesappusingfirebase.ui.theme.ColorWhite

@Composable
fun InsertScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    id: String?
) {
    val db = FirebaseFirestore.getInstance()
    val notesCollection = db.collection("notes")
    val context = LocalContext.current

    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var loading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        if (id != "defaultId") {
            notesCollection.document(id!!).get().addOnSuccessListener {
                val singleNote = it.toObject(Note::class.java)
                title = singleNote?.title.orEmpty()
                description = singleNote?.description.orEmpty()
                loading = false
            }.addOnFailureListener {
                Toast.makeText(context, "Failed to fetch data", Toast.LENGTH_SHORT).show()
                loading = false
            }
        } else {
            loading = false
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionBtn(
                onClick = {
                    if (title.isEmpty() || description.isEmpty()) {
                        Toast.makeText(context, "Please fill all the fields", Toast.LENGTH_SHORT).show()
                    } else {
                        val noteId = if (id != "defaultId") id.toString() else notesCollection.document().id
                        val note = Note(
                            id = noteId,
                            title = title,
                            description = description
                        )
                        notesCollection.document(noteId).set(note).addOnCompleteListener {
                            if (it.isSuccessful) {
                                Toast.makeText(context, "Note saved successfully", Toast.LENGTH_SHORT).show()
                                navController.popBackStack()
                            } else {
                                Toast.makeText(context, "Something went wrong, please try again later", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                },
                icon = Icons.Filled.Check
            )
        },
        containerColor = ColorBlack
    ) { innerPadding ->
        if (loading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = ColorWhite)
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp)
            ) {
                Text(
                    text = if (id == "defaultId") "Insert Screen" else "Update Screen",
                    fontSize = 25.sp,
                    color = ColorWhite,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    placeholder = {
                        Text(
                            "Enter Your Title",
                            color = ColorLightGrey
                        )
                    },
                    maxLines = 1,
                    shape = RoundedCornerShape(corner = CornerSize(15.dp)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    colors = TextFieldDefaults.colors(
                        disabledContainerColor = ColorGrey,
                        focusedContainerColor = ColorGrey,
                        unfocusedContainerColor = ColorGrey,
                        unfocusedIndicatorColor = ColorGrey,
                        focusedIndicatorColor = ColorGrey,
                        cursorColor = ColorWhite,
                        focusedTextColor = ColorWhite,
                        unfocusedTextColor = ColorWhite
                    )
                )

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    placeholder = {
                        Text(
                            "Enter Your Description",
                            color = ColorLightGrey
                        )
                    },
                    shape = RoundedCornerShape(corner = CornerSize(15.dp)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.6f)
                        .padding(top = 16.dp),
                    colors = TextFieldDefaults.colors(
                        disabledContainerColor = ColorGrey,
                        focusedContainerColor = ColorGrey,
                        unfocusedContainerColor = ColorGrey,
                        unfocusedIndicatorColor = ColorGrey,
                        focusedIndicatorColor = ColorGrey,
                        cursorColor = ColorWhite,
                        focusedTextColor = ColorWhite,
                        unfocusedTextColor = ColorWhite
                    )
                )
            }
        }
    }
}