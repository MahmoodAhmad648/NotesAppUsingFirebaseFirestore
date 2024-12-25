package com.mahmood.notesappusingfirebase

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.PopupProperties
import androidx.navigation.NavHostController
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.mahmood.notesappusingfirebase.navigation.Screens
import com.mahmood.notesappusingfirebase.ui.theme.ColorBlack
import com.mahmood.notesappusingfirebase.ui.theme.ColorGrey
import com.mahmood.notesappusingfirebase.ui.theme.ColorRed
import com.mahmood.notesappusingfirebase.ui.theme.ColorWhite

@Composable
fun NotesScreen(modifier: Modifier = Modifier, navController: NavHostController) {


    val db = FirebaseFirestore.getInstance()
    val notesCollection = db.collection("notes")
    val notesList = remember { mutableStateListOf<Note>() }
    var loading by remember { mutableStateOf(false) }


    LaunchedEffect(Unit) {
        notesCollection.addSnapshotListener { snapshot, error ->
            if (error == null) {
                val data = snapshot?.toObjects(Note::class.java)
                loading = true
                if (data != null) {
                    notesList.clear()
                    notesList.addAll(data)
                }
                loading = false

            } else {
                loading = false
            }

        }
    }


    Scaffold(
        floatingActionButton = {

            FloatingActionBtn(
                onClick = {
                    navController.navigate(Screens.InsertScreen.route + "/defaultId")
                },
                icon = Icons.Filled.Add
            )


        },
        containerColor = ColorBlack
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Text(
                text = "Notes Screen",
                fontSize = 25.sp,
                color = ColorWhite,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))

            if (loading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = ColorWhite
                    )
                }
            } else {
                LazyColumn {
                    items(notesList) {
                        NotesItem(
                            note = it,
                            notesCollection = notesCollection,
                            navController = navController
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }


        }


    }

}

@Composable
fun NotesItem(
    modifier: Modifier = Modifier,
    note: Note,
    notesCollection: CollectionReference,
    navController: NavHostController
) {
    val context = LocalContext.current
    var expanded by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape = RoundedCornerShape(20.dp))
            .background(color = ColorGrey)
            .padding(16.dp)
    ) {

        DropdownMenu(
            modifier = Modifier.background(color = ColorWhite),
            properties = PopupProperties(clippingEnabled = true),
            offset = DpOffset(x = (-40).dp, y = 0.dp),
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            },

            ) {
            DropdownMenuItem(

                text = { Text("Update") },
                onClick = {
                navController.navigate(Screens.InsertScreen.route + "/${note.id}")
                    expanded = false
                },

                )
            DropdownMenuItem(
                text = { Text("Delete") },
                onClick = {
                    notesCollection.document(note.id).delete()
                    expanded = false
                    Toast.makeText(context, "Note Deleted", Toast.LENGTH_SHORT).show()
                },

                )
        }


        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                note.title,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = ColorWhite

            )



            Icon(
                painter = painterResource(R.drawable.ic_more),
                contentDescription = "",
                tint = ColorWhite,
                modifier = Modifier.clickable {
                    expanded = true
                }

            )


        }

        Text(
            note.description,
            fontSize = 15.sp,
            color = ColorWhite,
            modifier = Modifier.fillMaxWidth(),
            maxLines = 3,
            overflow = TextOverflow.Ellipsis

        )

    }


}


@Composable
fun FloatingActionBtn(
    onClick: () -> Unit,
    icon: ImageVector
) {
    FloatingActionButton(
        onClick = { onClick() },
        containerColor = ColorRed,
        contentColor = ColorWhite,
        shape = CircleShape,
    ) {
        Icon(
            icon, "Small floating action button.",
        )
    }
}





