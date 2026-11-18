package tj.shohin.lughat

import android.os.Bundle
import org.json.JSONArray
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import androidx.compose.ui.platform.LocalContext
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Midnight = Color(0xFF071C16)
private val Emerald = Color(0xFF087B53)
private val EmeraldBright = Color(0xFF08A56C)
private val Gold = Color(0xFFC9A646)
private val Ivory = Color(0xFFF5EBD5)
private val Muted = Color(0xFF9AAB9F)
private val SurfaceDark = Color(0xFF102820)

data class Word(val en: String, val tg: String, val category: String, val example: String = "")

private val categories = listOf(
    "Ҳама", "Муошират", "Оила", "Ғизо", "Хона", "Маориф", "Кор",
    "Тиҷорат", "Сафар", "Саломатӣ", "Табиат", "Технология", "Ҷомеа"
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { ShohinDictionaryApp() }
    }
}

@Composable
fun ShohinDictionaryApp() {
    val context = LocalContext.current
    var words by remember { mutableStateOf(emptyList<Word>()) }
    LaunchedEffect(Unit) {
        words = withContext(Dispatchers.IO) {
            val raw = context.assets.open("dictionary/words.json").bufferedReader().use { it.readText() }
            val array = JSONArray(raw)
            List(array.length()) { index ->
                val item = array.getJSONObject(index)
                Word(item.getString("en"), item.getString("tg"), item.getString("category"), item.optString("example", ""))
            }
        }
    }
    var query by remember { mutableStateOf("") }
    var englishFirst by remember { mutableStateOf(true) }
    var selectedCategory by remember { mutableStateOf("Ҳама") }
    var favorites by remember { mutableStateOf(setOf<String>()) }
    var showFavorites by remember { mutableStateOf(false) }

    val filtered = words.filter { word ->
        val matchesQuery = query.isBlank() ||
            word.en.contains(query.trim(), ignoreCase = true) ||
            word.tg.contains(query.trim(), ignoreCase = true)
        val matchesCategory = selectedCategory == "Ҳама" || word.category == selectedCategory
        val matchesFavorites = !showFavorites || favorites.contains(word.en)
        matchesQuery && matchesCategory && matchesFavorites
    }

    MaterialTheme(
        colorScheme = darkColorScheme(
            primary = EmeraldBright,
            secondary = Gold,
            background = Midnight,
            surface = SurfaceDark,
            onBackground = Ivory,
            onSurface = Ivory
        )
    ) {
        Column(
            modifier = Modifier.fillMaxSize().background(Midnight)
                .statusBarsPadding().navigationBarsPadding()
        ) {
            Column(Modifier.padding(horizontal = 20.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(top = 14.dp, bottom = 20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text("SHOHIN", color = Gold, fontSize = 12.sp, fontWeight = FontWeight.Bold, letterSpacing = 3.sp)
                        Text("LUGHAT", color = Ivory, fontSize = 27.sp, fontWeight = FontWeight.ExtraBold, letterSpacing = 1.sp)
                        Text("English  ↔  Тоҷикӣ", color = Muted, fontSize = 13.sp)
                    }
                    Box(
                        modifier = Modifier.size(48.dp).background(Emerald, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Translate, contentDescription = "Dictionary", tint = Gold, modifier = Modifier.size(25.dp))
                    }
                }

                Card(
                    colors = CardDefaults.cardColors(containerColor = SurfaceDark),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Text("ТАРҶУМАИ ДУҶОНИБА", color = Gold, fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.5.sp)
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(top = 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(if (englishFirst) "English" else "Тоҷикӣ", color = Ivory, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                                Text(if (englishFirst) "Англисӣ" else "Тоҷикӣ", color = Muted, fontSize = 12.sp)
                            }
                            IconButton(
                                onClick = { englishFirst = !englishFirst },
                                modifier = Modifier.background(Emerald, CircleShape).size(42.dp)
                            ) {
                                Icon(Icons.Default.SwapVert, contentDescription = "Change direction", tint = Gold)
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text(if (englishFirst) "Тоҷикӣ" else "English", color = Ivory, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                                Text(if (englishFirst) "Тоҷикӣ" else "Англисӣ", color = Muted, fontSize = 12.sp)
                            }
                        }
                    }
                }

                OutlinedTextField(
                    value = query,
                    onValueChange = { query = it },
                    modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                    placeholder = { Text("Калима нависед / Search...", color = Muted) },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = EmeraldBright) },
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Gold,
                        unfocusedBorderColor = Color(0xFF294238),
                        focusedTextColor = Ivory,
                        unfocusedTextColor = Ivory,
                        cursorColor = Gold
                    )
                )

                Row(
                    modifier = Modifier.fillMaxWidth().padding(top = 16.dp, bottom = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(if (showFavorites) "ИЗБРАННОЕ" else "КАТЕГОРИЯ", color = Gold, fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.2.sp)
                    TextButton(onClick = { showFavorites = !showFavorites }) {
                        Icon(if (showFavorites) Icons.Default.Bookmark else Icons.Default.BookmarkBorder, contentDescription = null, tint = Gold, modifier = Modifier.size(17.dp))
                        Spacer(Modifier.width(5.dp))
                        Text(if (showFavorites) "Ҳамаи калимаҳо" else "Избранное", color = Ivory, fontSize = 12.sp)
                    }
                }
            }

            androidx.compose.foundation.lazy.LazyRow(
                contentPadding = PaddingValues(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(categories) { category ->
                    FilterChip(
                        selected = selectedCategory == category,
                        onClick = { selectedCategory = category },
                        label = { Text(category, fontSize = 12.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Emerald,
                            selectedLabelColor = Ivory,
                            labelColor = Muted
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true,
                            selected = selectedCategory == category,
                            borderColor = Color(0xFF294238),
                            selectedBorderColor = Gold
                        )
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("КАЛИМАҲО", color = Muted, fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.4.sp)
                Text("${filtered.size} натиҷа", color = Gold, fontSize = 12.sp)
            }

            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(start = 20.dp, end = 20.dp, bottom = 20.dp),
                verticalArrangement = Arrangement.spacedBy(9.dp)
            ) {
                items(filtered, key = { it.en }) { word ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = SurfaceDark)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 15.dp, vertical = 14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(Modifier.weight(1f)) {
                                Text(
                                    if (englishFirst) word.en else word.tg,
                                    color = Ivory, fontSize = 17.sp, fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    if (englishFirst) word.tg else word.en,
                                    color = EmeraldBright, fontSize = 15.sp, fontWeight = FontWeight.Medium,
                                    modifier = Modifier.padding(top = 3.dp)
                                )
                                Text(word.category, color = Muted, fontSize = 10.sp, modifier = Modifier.padding(top = 5.dp))
                            }
                            IconButton(onClick = {
                                favorites = if (favorites.contains(word.en)) favorites - word.en else favorites + word.en
                            }) {
                                Icon(
                                    if (favorites.contains(word.en)) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                                    contentDescription = "Favorite",
                                    tint = if (favorites.contains(word.en)) Gold else Muted
                                )
                            }
                        }
                    }
                }
                if (filtered.isEmpty()) {
                    item {
                        Text("Калима ёфт нашуд. Дигар навиштро санҷед.", color = Muted, modifier = Modifier.padding(20.dp))
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth().background(Color(0xFF0B211A)).padding(vertical = 12.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("SHOHIN TAJIKISTAN", color = Gold, fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 2.sp)
                Text("  •  OFFLINE DICTIONARY", color = Muted, fontSize = 9.sp, letterSpacing = 1.sp)
            }
        }
    }
}
