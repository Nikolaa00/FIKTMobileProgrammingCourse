package com.example.pmpzadacha1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import com.example.pmpzadacha1.ui.theme.FavoriteTwitterSearchesTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FavoriteTwitterSearchesTheme {
                // Ја повикуваме главната функција
                FavoriteSearchesScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoriteSearchesScreen() {
    // Отстранет "context" бидејќи не се користи
    var searchQuery by remember { mutableStateOf("") }
    var tagQuery by remember { mutableStateOf("") }

    val searches = remember {
        mutableStateListOf("AndroidFP", "Deitel", "Google", "iPhoneFP", "JavaFP", "JavaHTP")
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Favorite Searches", fontWeight = FontWeight.SemiBold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Color.Transparent
                ),
                shape = MaterialTheme.shapes.medium
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = { Text("Search Query") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp),
                        leadingIcon = {
                            Icon(
                                Icons.Default.Search,
                                contentDescription = "Search Icon",
                                tint = Color.Black
                            )
                        },
                        shape = MaterialTheme.shapes.large,
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.Black,
                            unfocusedBorderColor = Color.Black,
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black,
                            focusedLabelColor = Color.Black,
                            unfocusedLabelColor = Color.Black,
                            cursorColor = Color.Black
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = tagQuery,
                            onValueChange = { tagQuery = it },
                            placeholder  = { Text("Tag") },
                            modifier = Modifier
                                .weight(1f)
                                .height(60.dp),
                            shape = MaterialTheme.shapes.large,
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color.Black,
                                unfocusedBorderColor = Color.Black,
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White,
                                focusedTextColor = Color.Black,
                                unfocusedTextColor = Color.Black,
                                focusedLabelColor = Color.Black,
                                unfocusedLabelColor = Color.Black,
                                cursorColor = Color.Black
                            )
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Button(
                            onClick = {
                                val entry = tagQuery.trim().ifEmpty { searchQuery.trim() }
                                if (entry.isNotEmpty()) {
                                    if (!searches.contains(entry)) {
                                        searches.add(0, entry)
                                    }
                                    searchQuery = ""
                                    tagQuery = ""
                                }
                            },
                            modifier = Modifier.height(56.dp),
                            shape = MaterialTheme.shapes.large
                        ) {
                            Text("Save")
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Tagged Searches",
                style = MaterialTheme.typography.labelLarge,
                color = Color(0xFF015966)
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(searches) { tag ->
                    SearchItem(
                        tag = tag,
                        onEdit = {
                            searchQuery = tag
                            tagQuery = tag
                        }
                    )
                }
            }

            TextButton(
                onClick = { searches.clear() },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.textButtonColors(
                    contentColor = MaterialTheme.colorScheme.error
                )
            ) {
                Icon(Icons.Default.Delete, contentDescription = "Delete All")
                Spacer(modifier = Modifier.width(8.dp))
                Text("Clear All Tags")
            }
        }
    }
}

@Composable
fun SearchItem(tag: String, onEdit: () -> Unit) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = tag,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodyLarge
            )

            IconButton(onClick = onEdit) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit Tag",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}