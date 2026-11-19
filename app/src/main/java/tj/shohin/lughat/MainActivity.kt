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
import androidx.compose.material.icons.filled.Menu
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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

data class Word(val number: Int, val en: String, val tg: String, val example: String = "")

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
    var isLoading by remember { mutableStateOf(true) }
    var loadError by remember { mutableStateOf(false) }
    val preferences = remember { context.getSharedPreferences("shohin_lughat", 0) }
    var favorites by remember {
        mutableStateOf(preferences.getStringSet("favorites", emptySet())?.toSet() ?: emptySet())
    }
    LaunchedEffect(Unit) {
        try {
            words = withContext(Dispatchers.IO) {
                val raw = context.assets.open("dictionary/words.json").bufferedReader().use { it.readText() }
                val array = JSONArray(raw)
                List(array.length()) { index ->
                    val item = array.getJSONObject(index)
                    Word(item.getInt("number"), item.getString("en"), item.getString("tg"), item.optString("example", ""))
                }
            }
        } catch (_: Exception) {
            loadError = true
        } finally {
            isLoading = false
        }
    }
    var query by remember { mutableStateOf("") }
    var englishFirst by remember { mutableStateOf(true) }
    var showFavorites by remember { mutableStateOf(false) }
    var menuExpanded by remember { mutableStateOf(false) }
    var showPrivacy by remember { mutableStateOf(false) }

    val filtered = words.filter { word ->
        val matchesQuery = query.isBlank() ||
            word.en.contains(query.trim(), ignoreCase = true) ||
            word.tg.contains(query.trim(), ignoreCase = true)
        val matchesFavorites = !showFavorites || favorites.contains(word.en)
        matchesQuery && matchesFavorites
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
                    Box {
                        IconButton(
                            onClick = { menuExpanded = true },
                            modifier = Modifier.size(48.dp).background(Emerald, CircleShape)
                        ) {
                            Icon(Icons.Default.Menu, contentDescription = "Меню", tint = Gold, modifier = Modifier.size(27.dp))
                        }
                        DropdownMenu(
                            expanded = menuExpanded,
                            onDismissRequest = { menuExpanded = false },
                            modifier = Modifier.background(SurfaceDark)
                        ) {
                            DropdownMenuItem(
                                text = { Text("Политика конфиденциальности", color = Ivory) },
                                onClick = { menuExpanded = false; showPrivacy = true }
                            )
                        }
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
                    Text(if (showFavorites) "ИЗБРАННОЕ" else "СЛОВАРЬ", color = Gold, fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.2.sp)
                    TextButton(onClick = { showFavorites = !showFavorites }) {
                        Icon(if (showFavorites) Icons.Default.Bookmark else Icons.Default.BookmarkBorder, contentDescription = null, tint = Gold, modifier = Modifier.size(17.dp))
                        Spacer(Modifier.width(5.dp))
                        Text(if (showFavorites) "Ҳамаи калимаҳо" else "Избранное", color = Ivory, fontSize = 12.sp)
                    }
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

            if (isLoading) {
                Box(
                    modifier = Modifier.weight(1f).fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Gold)
                }
            } else if (loadError) {
                Box(
                    modifier = Modifier.weight(1f).fillMaxWidth().padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Луғатро бор кардан нашуд. Барномаро аз нав кушоед.", color = Ivory)
                }
            } else LazyColumn(
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
                                Text("№ ${word.number}", color = Gold, fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 5.dp))
                            }
                            IconButton(onClick = {
                                val updated = if (favorites.contains(word.en)) favorites - word.en else favorites + word.en
                                favorites = updated
                                preferences.edit().putStringSet("favorites", updated).apply()
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

        if (showPrivacy) {
            AlertDialog(
                onDismissRequest = { showPrivacy = false },
                containerColor = SurfaceDark,
                title = { Text("Политика конфиденциальности", color = Gold, fontWeight = FontWeight.Bold) },
                text = {
                    Column(
                        modifier = Modifier.heightIn(max = 420.dp).verticalScroll(rememberScrollState())
                    ) {
                        Text(
                            "SHOHIN LUGHAT — офлайн-словарь английского и таджикского языков. " +
                            "Приложение не требует подключения к интернету и не отправляет поисковые запросы на сервер.\n\n" +
                            "СБОР ДАННЫХ\n" +
                            "Приложение не собирает, не получает и не передаёт разработчику персональные данные, " +
                            "контакты, местоположение или историю поиска.\n\n" +
                            "ЛОКАЛЬНОЕ ХРАНЕНИЕ\n" +
                            "Список избранных слов сохраняется локально на устройстве. Эти данные используются " +
                            "только для работы функции избранного. Они не передаются разработчику. " +
                            "Резервное копирование Android может зависеть от настроек устройства и системы.\n\n" +
                            "СТОРОННИЕ СЕРВИСЫ\n" +
                            "В текущей версии нет рекламы, аналитики, регистрации и сторонних сервисов сбора данных.\n\n" +
                            "БЕЗОПАСНОСТЬ И УДАЛЕНИЕ\n" +
                            "Вы можете удалить сохранённые данные, очистив данные приложения или удалив приложение.\n\n" +
                            "ИЗМЕНЕНИЯ\n" +
                            "Если функции приложения или правила обработки данных изменятся, эта политика будет обновлена.\n\n" +
                            "Разработчик: SHOHIN TAJIKISTAN\n" +
                            "Контакт: через официальный канал SHOHIN TAJIKISTAN.",
                            color = Ivory,
                            lineHeight = 20.sp
                        )
                    }
                },
                confirmButton = {
                    TextButton(onClick = { showPrivacy = false }) {
                        Text("Закрыть", color = Gold)
                    }
                }
            )
        }
}
