package com.example.zadacha2

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.zadacha2.ui.theme.FavoriteTwitterSearchesTheme
import java.io.BufferedReader
import java.io.InputStreamReader

data class DictionaryEntry(val english: String, val macedonian: String)

fun loadDictionary(context: Context): List<DictionaryEntry> {
    val entries = mutableListOf<DictionaryEntry>()
    try {
        val reader =
            BufferedReader(InputStreamReader(context.assets.open("en_mk_recnik.txt"), "UTF-8"))
        reader.forEachLine { line ->
            val parts = line.split(",")
            if (parts.size >= 2) {
                entries.add(
                    DictionaryEntry(
                        english = parts[0].trim(), macedonian = parts[1].trim()
                    )
                )
            }
        }
        reader.close()
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return entries
}

fun searchDictionary(query: String, dictionary: List<DictionaryEntry>): List<DictionaryEntry> {
    if (query.isBlank()) return emptyList()
    val q = query.trim().lowercase()
    return dictionary.filter {
        it.english.lowercase().contains(q) || it.macedonian.lowercase().contains(q)
    }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FavoriteTwitterSearchesTheme {
                MainScreen()
            }
        }
    }
}

enum class AppTab { SEARCHES, DICTIONARY }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    var selectedTab by remember { mutableStateOf(AppTab.SEARCHES) }

    Scaffold(topBar = {
        TopAppBar(
            title = {
                Text(
                    if (selectedTab == AppTab.SEARCHES) "Favorite Searches" else "МК-EN Dictionary",
                    fontWeight = FontWeight.SemiBold
                )
            }, colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
            )
        )
    }, bottomBar = {
        NavigationBar {
            NavigationBarItem(
                selected = selectedTab == AppTab.SEARCHES,
                onClick = { selectedTab = AppTab.SEARCHES },
                icon = { Icon(Icons.Default.Star, contentDescription = null) },
                label = { Text("Searches") })
            NavigationBarItem(
                selected = selectedTab == AppTab.DICTIONARY,
                onClick = { selectedTab = AppTab.DICTIONARY },
                icon = { Icon(Icons.Default.Search, contentDescription = null) },
                label = { Text("Dictionary") })
        }
    }) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            when (selectedTab) {
                AppTab.SEARCHES -> FavoriteSearchesScreen()
                AppTab.DICTIONARY -> DictionaryScreen()
            }
        }
    }
}

@Composable
fun FavoriteSearchesScreen() {
    var searchQuery by remember { mutableStateOf("") }
    var tagQuery by remember { mutableStateOf("") }

    val searches = remember {
        mutableStateListOf("AndroidFP", "Deitel", "Google", "iPhoneFP", "JavaFP", "JavaHTP")
    }

    val textFieldColors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = Color.Black,
        unfocusedBorderColor = Color.Black,
        focusedContainerColor = Color.White,
        unfocusedContainerColor = Color.White,
        focusedTextColor = Color.Black,
        unfocusedTextColor = Color.Black,
        cursorColor = Color.Black
    )

    val textKeyboardOptions = KeyboardOptions(
        keyboardType = KeyboardType.Text, capitalization = KeyboardCapitalization.None
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.Transparent)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Search Query") },
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = {
                        Icon(Icons.Default.Search, contentDescription = null, tint = Color.Black)
                    },
                    shape = MaterialTheme.shapes.large,
                    singleLine = true,
                    keyboardOptions = textKeyboardOptions,
                    colors = textFieldColors
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = tagQuery,
                        onValueChange = { tagQuery = it },
                        placeholder = { Text("Tag") },
                        modifier = Modifier.weight(1f),
                        shape = MaterialTheme.shapes.large,
                        singleLine = true,
                        keyboardOptions = textKeyboardOptions,
                        colors = textFieldColors
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(
                        onClick = {
                            val entry = tagQuery.trim().ifEmpty { searchQuery.trim() }
                            if (entry.isNotEmpty() && !searches.contains(entry)) {
                                searches.add(0, entry)
                            }
                            searchQuery = ""
                            tagQuery = ""
                        }, modifier = Modifier.height(56.dp), shape = MaterialTheme.shapes.large
                    ) {
                        Text("Save")
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        Text(
            text = "Tagged Searches",
            style = MaterialTheme.typography.labelLarge,
            color = Color(0xFF015966)
        )

        HorizontalDivider(
            modifier = Modifier.padding(vertical = 15.dp),
            thickness = 3.dp,
            color = Color(0xFFC6FCD6)
        )
        Spacer(modifier = Modifier.width(15.dp))
        LazyColumn(
            modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(searches) { tag ->
                SearchItem(
                    tag = tag, onEdit = {
                        searchQuery = tag
                        tagQuery = tag
                    })
            }
        }

        TextButton(
            onClick = { searches.clear() },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.textButtonColors(
                contentColor = MaterialTheme.colorScheme.error
            )
        ) {
            Icon(Icons.Default.Delete, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Clear All Tags")
        }
    }
}

@Composable
fun SearchItem(tag: String, onEdit: () -> Unit) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(), shape = MaterialTheme.shapes.medium
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
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

@Composable
fun DictionaryScreen() {
    val context = LocalContext.current
    val dictionary = remember { loadDictionary(context) }

    var query by remember { mutableStateOf("") }
    val results by remember(query) {
        derivedStateOf { searchDictionary(query, dictionary) }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            placeholder = { Text("Search in English or Macedonian...") },
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = {
                Icon(Icons.Default.Search, contentDescription = null, tint = Color.Black)
            },
            trailingIcon = {
                if (query.isNotEmpty()) {
                    IconButton(onClick = { query = "" }) {
                        Icon(Icons.Default.Clear, contentDescription = "Clear", tint = Color.Gray)
                    }
                }
            },
            shape = MaterialTheme.shapes.large,
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text, capitalization = KeyboardCapitalization.None
            ),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Black,
                unfocusedBorderColor = Color.Black,
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black,
                cursorColor = Color.Black
            )
        )

        Spacer(modifier = Modifier.height(20.dp))

        HorizontalDivider(
            thickness = 3.dp, color = Color(0xFFC6FCD6)
        )

        Spacer(modifier = Modifier.height(20.dp))

        when {
            query.isBlank() -> {
                Text(
                    "Full dictionary (${dictionary.size} words)",
                    style = MaterialTheme.typography.labelMedium,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(8.dp))
                LazyColumn(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    items(dictionary) { entry ->
                        DictionaryEntryCard(entry = entry)
                    }
                }
            }

            results.isEmpty() -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            Icons.Default.SearchOff,
                            contentDescription = null,
                            modifier = Modifier.size(56.dp),
                            tint = Color.LightGray
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            "No results for \"$query\"",
                            color = Color.Gray,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }

            else -> {
                Text(
                    "${results.size} result(s)",
                    style = MaterialTheme.typography.labelMedium,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(8.dp))
                LazyColumn(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    items(results) { entry ->
                        DictionaryEntryCard(entry = entry)
                    }
                }
            }
        }
    }
}

@Composable
fun DictionaryEntryCard(entry: DictionaryEntry) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = Color.White
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "EN",
                    fontSize = 10.sp,
                    color = Color(0xFF009688),
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
                Text(
                    text = entry.english,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF1A1A2E)
                )
            }

            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(
                        color = Color(0xFF009688).copy(alpha = 0.1f),
                        shape = RoundedCornerShape(8.dp)
                    ), contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "=",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF009688)
                )
            }

            Column(
                modifier = Modifier.weight(1f), horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = "МК",
                    fontSize = 10.sp,
                    color = Color(0xFFFF005E),
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
                Text(
                    text = entry.macedonian,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF1A1A2E)
                )
            }
        }
    }
}