package tj.shohin.lughat

import android.os.Bundle
import android.content.ClipData
import android.content.ClipboardManager
import android.speech.tts.TextToSpeech
import android.widget.Toast
import java.util.Locale
import java.util.Calendar
import java.text.Normalizer
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
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.VolumeUp
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


private data class ConversationTopic(val title: String, val englishTitle: String, val phrases: List<Pair<String, String>>)

private val conversationTopics = listOf(
    ConversationTopic("Шиносоӣ", "Introductions", listOf(
        "Hello!" to "Салом!",
        "My name is Farid." to "Номи ман Фарид аст.",
        "Where are you from?" to "Шумо аз куҷо ҳастед?",
        "Nice to meet you." to "Аз шиносоӣ бо шумо шодам."
    )),
    ConversationTopic("Оила", "Family", listOf(
        "How many people are in your family?" to "Дар оилаи шумо чанд нафар ҳаст?",
        "I have two sisters." to "Ман ду хоҳар дорам.",
        "My parents live here." to "Падару модари ман дар ин ҷо зиндагӣ мекунанд.",
        "Do you have children?" to "Шумо фарзанд доред?"
    )),
    ConversationTopic("Дӯстӣ", "Friendship", listOf(
        "Would you like to be friends?" to "Мехоҳед дӯст шавем?",
        "You can count on me." to "Шумо метавонед ба ман такя кунед.",
        "Let's keep in touch." to "Биёед дар тамос бошем.",
        "Thank you for your friendship." to "Барои дӯстиятон ташаккур."
    )),
    ConversationTopic("Дар мағоза", "At the shop", listOf(
        "How much does this cost?" to "Ин чанд пул аст?",
        "Can I try it on?" to "Метавонам онро пӯшида бинам?",
        "Do you have another colour?" to "Ранги дигар доред?",
        "Could I have a receipt?" to "Метавонам расид гирам?"
    )),
    ConversationTopic("Дар кор", "At work", listOf(
        "What time does your shift begin?" to "Навбати кори шумо соати чанд оғоз мешавад?",
        "I have a meeting at ten." to "Ман соати даҳ маҷлис дорам.",
        "Could you help me with this task?" to "Метавонед дар ин вазифа ба ман ёрӣ диҳед?",
        "The report is ready." to "Ҳисобот омода аст."
    )),
    ConversationTopic("Дар мактаб", "At school", listOf(
        "Which subject do you like most?" to "Кадом фанро бештар дӯст медоред?",
        "Please explain this rule again." to "Лутфан, ин қоидаро боз фаҳмонед.",
        "When is the next lesson?" to "Дарси навбатӣ кай мешавад?",
        "We have an exam tomorrow." to "Мо пагоҳ имтиҳон дорем."
    )),
    ConversationTopic("Дар донишгоҳ", "At university", listOf(
        "What are you studying?" to "Шумо чӣ мехонед?",
        "I study computer science." to "Ман илми компютериро меомӯзам.",
        "Where is the library?" to "Китобхона дар куҷост?",
        "Have you finished your assignment?" to "Супориши худро тамом кардед?"
    )),
    ConversationTopic("Сафар", "Travel", listOf(
        "I would like to book a room." to "Ман мехоҳам ҳуҷра фармоиш диҳам.",
        "When does the bus leave?" to "Автобус кай меравад?",
        "Could you show me the way?" to "Метавонед роҳро нишон диҳед?",
        "I have lost my passport." to "Ман шиносномаи худро гум кардам."
    )),
    ConversationTopic("Фурудгоҳ", "At the airport", listOf(
        "Where is the check-in desk?" to "Ҷойи бақайдгирӣ дар куҷост?",
        "Here is my passport." to "Ин шиносномаи ман аст.",
        "Has the flight been delayed?" to "Парвоз ба таъхир афтодааст?",
        "Where can I collect my luggage?" to "Бағоҷамро аз куҷо гирифта метавонам?"
    )),
    ConversationTopic("Дар меҳмонхона", "At a hotel", listOf(
        "I have a reservation." to "Ман ҳуҷра фармоиш додаам.",
        "Is Wi-Fi available?" to "Wi-Fi ҳаст?",
        "Could I have an extra towel?" to "Метавонед як дастмоли иловагӣ диҳед?",
        "What time is check-out?" to "Соати баромадан чанд аст?"
    )),
    ConversationTopic("Дар тарабхона", "At a restaurant", listOf(
        "Could I see the menu, please?" to "Лутфан, менюро нишон диҳед.",
        "What is today's special?" to "Таоми махсуси имрӯз чист?",
        "I would like a glass of water." to "Ман як истакон об мехоҳам.",
        "Could we have the bill?" to "Метавонем ҳисобро гирем?"
    )),
    ConversationTopic("Дар беморхона", "At the hospital", listOf(
        "I need to see a doctor." to "Ман бояд ба духтур муроҷиат кунам.",
        "Where does it hurt?" to "Дар куҷоятон дард мекунад?",
        "I have had a headache since yesterday." to "Аз дирӯз сарам дард мекунад.",
        "Please call an ambulance." to "Лутфан, ёрии таъҷилиро даъват кунед."
    )),
    ConversationTopic("Дар дорухона", "At the pharmacy", listOf(
        "Do you have this medicine?" to "Ин доруро доред?",
        "How should I take it?" to "Ман онро чӣ тавр истеъмол кунам?",
        "Is it available without a prescription?" to "Он бе дорухат фурӯхта мешавад?",
        "Please read the instructions." to "Лутфан, дастурамалро хонед."
    )),
    ConversationTopic("Муоширати телефонӣ", "Phone conversations", listOf(
        "May I speak to Mr Smith?" to "Метавонам бо ҷаноби Смит суҳбат кунам?",
        "Who is calling, please?" to "Лутфан, кӣ занг мезанад?",
        "Could you call me back later?" to "Метавонед баъдтар ба ман занг занед?",
        "I will send you a message." to "Ман ба шумо паём мефиристам."
    )),
    ConversationTopic("Интернет ва технология", "Internet and technology", listOf(
        "What is the Wi-Fi password?" to "Рамзи Wi-Fi чист?",
        "My phone battery is low." to "Батареяи телефонам кам мондааст.",
        "Please send me the link." to "Лутфан, пайвандро ба ман фиристед.",
        "I cannot connect to the internet." to "Ман ба интернет пайваст шуда наметавонам."
    )),
    ConversationTopic("Вақт ва нақшаҳо", "Time and plans", listOf(
        "Are you free this evening?" to "Имшаб вақти холӣ доред?",
        "Let's meet at six." to "Биёед соати шаш вохӯрем.",
        "I may be a little late." to "Шояд каме дер кунам.",
        "What are your plans for tomorrow?" to "Нақшаҳои шумо барои пагоҳ чист?"
    )),
    ConversationTopic("Обу ҳаво", "Weather", listOf(
        "It looks like rain." to "Ба назар мерасад, ки борон меборад.",
        "The weather is lovely today." to "Имрӯз ҳаво хеле хуб аст.",
        "It is getting colder." to "Ҳаво хунуктар шуда истодааст.",
        "Do not forget your umbrella." to "Чатрро фаромӯш накунед."
    )),
    ConversationTopic("Варзиш", "Sports", listOf(
        "Do you play football?" to "Шумо футбол бозӣ мекунед?",
        "Our team won the match." to "Дастаи мо бозиро бурд.",
        "I go jogging every morning." to "Ман ҳар саҳар медавам.",
        "Would you like to join us?" to "Мехоҳед ба мо ҳамроҳ шавед?"
    )),
    ConversationTopic("Харидани либос", "Buying clothes", listOf(
        "What size do you wear?" to "Шумо кадом андоза мепӯшед?",
        "This jacket fits me well." to "Ин куртка ба ман мувофиқ аст.",
        "Do you have it in blue?" to "Инро бо ранги кабуд доред?",
        "Can I exchange it?" to "Метавонам онро иваз кунам?"
    )),
    ConversationTopic("Ҷустуҷӯи хона", "Finding a home", listOf(
        "I am looking for a flat to rent." to "Ман барои иҷора хона меҷӯям.",
        "How much is the monthly rent?" to "Иҷораи моҳона чанд пул аст?",
        "Are utilities included?" to "Пардохти хизматрасониҳо дохил аст?",
        "When can I see the apartment?" to "Кай метавонам хонаро бинам?"
    )),
    ConversationTopic("Мусоҳибаи корӣ", "Job interview", listOf(
        "Thank you for inviting me." to "Барои даъват кардан ташаккур.",
        "Tell me about your experience." to "Дар бораи таҷрибаи худ нақл кунед.",
        "I am willing to learn." to "Ман барои омӯхтан омодаам.",
        "What are the working hours?" to "Соатҳои корӣ чӣ гунаанд?"
    )),
    ConversationTopic("Муоширати расмӣ", "Formal conversation", listOf(
        "Could you please clarify this point?" to "Лутфан, ин нуктаро равшан карда метавонед?",
        "I appreciate your time." to "Ман вақти шуморо қадр мекунам.",
        "Please accept my apologies." to "Лутфан, узри маро қабул кунед.",
        "I look forward to your reply." to "Ман мунтазири ҷавоби шумо ҳастам."
    )),
    ConversationTopic("Ҳалли мушкилот", "Solving a problem", listOf(
        "There seems to be a problem." to "Ба назар мерасад, ки мушкилӣ вуҷуд дорад.",
        "Let us find a solution." to "Биёед роҳи ҳалро пайдо кунем.",
        "Could you explain what happened?" to "Метавонед фаҳмонед, ки чӣ рӯй дод?",
        "We can solve this together." to "Мо метавонем инро якҷоя ҳал кунем."
    )),
    ConversationTopic("Муоширати ҳаррӯза", "Daily conversation", listOf(
        "What have you been doing lately?" to "Вақтҳои охир чӣ кор карда истодаед?",
        "I am glad to see you." to "Аз дидани шумо шодам.",
        "Could you speak more slowly?" to "Метавонед каме оҳистатар гап занед?",
        "Have a nice day!" to "Рӯзи хуб дошта бошед!"
    ))
)

private data class ClassicStory(val title: String, val author: String, val english: String, val tajik: String)

private val classicStories = listOf(
    ClassicStory("The Happy Prince", "Oscar Wilde", "High above the city stood a statue called the Happy Prince. A little Swallow helped the Prince give the gold and jewels from the statue to poor people. The bird stayed with him through the cold winter. Their kindness was remembered by the people.", "Дар баландии шаҳр ҳайкале меистод, ки онро Шоҳзодаи Хушбахт меномиданд. Парастуи хурде ба Шоҳзода ёрӣ дод, то тилло ва ҷавоҳироти ҳайкалро ба одамони камбағал диҳад. Парранда тамоми зимистон назди ӯ монд. Меҳрубонии онҳо дар ёди мардум боқӣ монд."),
    ClassicStory("A Christmas Carol", "Charles Dickens", "Ebenezer Scrooge cared only about money and disliked Christmas. Three mysterious visitors showed him his past, present and possible future. He understood that he had become lonely and unkind. The next morning he changed his behaviour, helped a poor family and shared happiness.", "Эбенезер Скруҷ танҳо дар бораи пул фикр мекард ва Мавлуди Исоро дӯст намедошт. Се меҳмони асроромез гузаштаи ӯ, замони ҳозира ва ояндаи эҳтимолиро нишон доданд. Ӯ фаҳмид, ки танҳо ва бераҳм шудааст. Субҳи дигар рафторашро дигар кард, ба оилаи камбағал ёрӣ дод ва хушбахтиро тақсим намуд."),
    ClassicStory("Alice in Wonderland", "Lewis Carroll", "Alice followed a White Rabbit and fell into a strange underground world. There she met talking animals and unusual characters, including the Cheshire Cat and the Mad Hatter. Her journey was full of puzzles and surprises. Alice learned to stay curious and brave.", "Алис аз паси Харгӯши Сафед рафт ва ба ҷаҳони аҷиби зеризаминӣ афтод. Дар он ҷо бо ҳайвоноти сухангӯ ва қаҳрамонҳои ғайриодӣ, аз ҷумла Гурбаи Чешир ва Кулоҳдӯзи Девона вохӯрд. Сафари ӯ пур аз муаммо ва ҳайрат буд. Алис ҷасур ва кунҷков монданро омӯхт."),
    ClassicStory("The Jungle Book", "Rudyard Kipling", "Mowgli grew up in the jungle among wolves. Baloo the bear taught him the laws of the jungle, and Bagheera the panther protected him. The tiger Shere Khan was dangerous, so Mowgli had to be brave and clever. With his friends, he faced many challenges.", "Маугли дар ҷангал дар байни гургҳо калон шуд. Хирс Балу ба ӯ қонунҳои ҷангалро меомӯхт ва пантера Багира ӯро муҳофизат мекард. Паланг Шер-Хон хатарнок буд, бинобар ин Маугли бояд ҷасуру доно мешуд. Ӯ бо дӯстонаш бо мушкилоти зиёд рӯ ба рӯ шуд."),
    ClassicStory("Treasure Island", "Robert Louis Stevenson", "Jim Hawkins found a map showing a hidden treasure. He sailed to a distant island, but some sailors planned a mutiny. The clever Long John Silver was among them. Jim used courage and good judgement to survive. After many adventures, he returned home with an unforgettable story.", "Ҷим Ҳокинс харитаеро ёфт, ки ганҷи пинҳоншударо нишон медод. Ӯ ба ҷазираи дур шино кард, аммо баъзе маллоҳон исён омода мекарданд. Лонг Ҷон Силвери маккор низ дар байни онҳо буд. Ҷим бо ҷасорат ва ақли солим зинда монд. Пас аз саргузаштҳои зиёд ӯ бо қиссаи фаромӯшнашаванда ба хона баргашт.")
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
    var showConversation by remember { mutableStateOf(false) }
    var showStories by remember { mutableStateOf(false) }
    var selectedStory by remember { mutableIntStateOf(-1) }
    var selectedConversation by remember { mutableIntStateOf(-1) }
    var showHistory by remember { mutableStateOf(false) }
    var searchHistory by remember {
        mutableStateOf(preferences.getString("search_history", "")?.split("\n")?.filter { it.isNotBlank() } ?: emptyList())
    }
    var ttsReady by remember { mutableStateOf(false) }
    val speech = remember {
        TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) ttsReady = true
        }
    }
    DisposableEffect(Unit) {
        onDispose { speech.shutdown() }
    }

    fun normalizeSearch(value: String): String =
        Normalizer.normalize(value.trim(), Normalizer.Form.NFD)
            .replace(Regex("\\p{Mn}+"), "")
            .lowercase(Locale.ROOT)

    val normalizedQuery = normalizeSearch(query)
    val filtered = words.filter { word ->
        val matchesQuery = normalizedQuery.isBlank() ||
            normalizeSearch(word.en).contains(normalizedQuery) ||
            normalizeSearch(word.tg).contains(normalizedQuery)
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
                            DropdownMenuItem(text = { Text("Главное", color = Ivory) }, onClick = { menuExpanded = false; showConversation = false; showStories = false })
                            DropdownMenuItem(text = { Text("Луғат", color = Ivory) }, onClick = { menuExpanded = false; showConversation = false })
                            DropdownMenuItem(text = { Text("Муошират", color = Ivory) }, onClick = { menuExpanded = false; selectedConversation = -1; showConversation = true; showStories = false })
                            DropdownMenuItem(text = { Text("Ҳикояҳо", color = Ivory) }, onClick = { menuExpanded = false; selectedStory = -1; showStories = true; showConversation = false })
                            DropdownMenuItem(text = { Text("Политика использования", color = Ivory) }, onClick = { menuExpanded = false; Toast.makeText(context, "SHOHIN LUGHAT барои омӯзиши забон пешбинӣ шудааст.", Toast.LENGTH_LONG).show() })
                            DropdownMenuItem(text = { Text("Политика конфиденциальности", color = Ivory) }, onClick = { menuExpanded = false; showPrivacy = true })
                            DropdownMenuItem(text = { Text("БАРНОМАИ МО", color = Gold, fontWeight = FontWeight.Bold) }, onClick = { menuExpanded = false; Toast.makeText(context, "SHOHIN TAJIKISTAN — таҳиягари барномаҳои омӯзишӣ ва муфид. Дар оянда барномаҳои нави мо дар ин бахш муаррифӣ мешаванд.", Toast.LENGTH_LONG).show() })
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

                val wordOfDay = words.getOrNull(
                    if (words.isEmpty()) 0 else (Calendar.getInstance().get(Calendar.DAY_OF_YEAR) - 1) % words.size
                )
                if (wordOfDay != null) {
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
                        colors = CardDefaults.cardColors(containerColor = Emerald),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(Modifier.padding(14.dp)) {
                            Text("КАЛИМАИ РӮЗ", color = Gold, fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.4.sp)
                            Text(wordOfDay.en, color = Ivory, fontSize = 19.sp, fontWeight = FontWeight.Bold)
                            Text(wordOfDay.tg, color = Ivory, fontSize = 15.sp)
                            if (wordOfDay.example.isNotBlank()) {
                                Text(wordOfDay.example, color = Ivory, fontSize = 12.sp, modifier = Modifier.padding(top = 5.dp))
                            }
                            TextButton(onClick = {
                                query = wordOfDay.en
                                showFavorites = false
                            }) { Text("Дар луғат дидан", color = Gold) }
                        }
                    }
                }

                OutlinedTextField(
                    value = query,
                    onValueChange = {
                        query = it
                        val exact = words.firstOrNull { word ->
                            word.en.equals(it.trim(), ignoreCase = true) ||
                            word.tg.equals(it.trim(), ignoreCase = true)
                        }
                        if (exact != null) {
                            val updated = (listOf(exact.en) + searchHistory.filterNot { old -> old == exact.en }).take(25)
                            searchHistory = updated
                            preferences.edit().putString("search_history", updated.joinToString("\n")).apply()
                        }
                    },
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
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                IconButton(onClick = {
                                    val clipboard = context.getSystemService(android.content.Context.CLIPBOARD_SERVICE) as ClipboardManager
                                    clipboard.setPrimaryClip(ClipData.newPlainText("SHOHIN LUGHAT", "${word.en} — ${word.tg}"))
                                    Toast.makeText(context, "Калима нусхабардорӣ шуд", Toast.LENGTH_SHORT).show()
                                }) {
                                    Icon(Icons.Default.ContentCopy, contentDescription = "Copy", tint = Muted)
                                }
                                IconButton(onClick = {
                                    if (ttsReady) { speech.language = Locale.US; speech.speak(word.en, TextToSpeech.QUEUE_FLUSH, null, "shohin_${word.number}") }
                                    else Toast.makeText(context, "Овоз ҳоло омода нест", Toast.LENGTH_SHORT).show()
                                }) {
                                    Icon(Icons.Default.VolumeUp, contentDescription = "Listen", tint = EmeraldBright)
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
                        if (word.example.isNotBlank()) {
                            Text(
                                word.example,
                                color = Muted,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(start = 15.dp, end = 15.dp, bottom = 12.dp)
                            )
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

        if (showConversation) {
            AlertDialog(
                onDismissRequest = { showConversation = false },
                containerColor = SurfaceDark,
                title = { Text(if (selectedConversation < 0) "Муошират · English ↔ Тоҷикӣ" else conversationTopics[selectedConversation].title, color = Gold, fontWeight = FontWeight.Bold) },
                text = {
                    Column(modifier = Modifier.heightIn(max = 500.dp).verticalScroll(rememberScrollState())) {
                        if (selectedConversation < 0) {
                            Text("Мавзӯъро интихоб кунед. Ҷумлаҳо бо тарҷумаи тоҷикӣ нишон дода мешаванд.", color = Muted, lineHeight = 20.sp)
                            Spacer(Modifier.height(12.dp))
                            conversationTopics.forEachIndexed { index, topic ->
                                Card(
                                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp).clickable { selectedConversation = index },
                                    colors = CardDefaults.cardColors(containerColor = Midnight),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Column(Modifier.padding(13.dp)) {
                                        Text(topic.title, color = Ivory, fontWeight = FontWeight.Bold)
                                        Text(topic.englishTitle, color = Muted, fontSize = 12.sp)
                                        Text("${topic.phrases.size} ҷумла", color = Gold, fontSize = 11.sp)
                                    }
                                }
                            }
                        } else {
                            TextButton(onClick = { selectedConversation = -1 }) { Text("← Ҳамаи мавзӯъҳо", color = Gold) }
                            conversationTopics[selectedConversation].phrases.forEachIndexed { index, phrase ->
                                Card(
                                    modifier = Modifier.fillMaxWidth().padding(vertical = 5.dp),
                                    colors = CardDefaults.cardColors(containerColor = Midnight),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Column(Modifier.padding(12.dp)) {
                                        Text("${index + 1}. ${phrase.first}", color = Ivory, fontWeight = FontWeight.SemiBold)
                                        Text(phrase.second, color = EmeraldBright, modifier = Modifier.padding(top = 5.dp))
                                        TextButton(onClick = {
                                            if (ttsReady) { speech.language = Locale.US; speech.speak(phrase.first, TextToSpeech.QUEUE_FLUSH, null, "conversation_${index}") }
                                        }) { Icon(Icons.Default.VolumeUp, contentDescription = null, tint = Gold); Spacer(Modifier.width(5.dp)); Text("Шунидан", color = Gold) }
                                    }
                                }
                            }
                        }
                    }
                },
                confirmButton = { TextButton(onClick = { showConversation = false }) { Text("Пӯшидан", color = Gold) } }
            )
        }


        if (showStories) {
            AlertDialog(
                onDismissRequest = { showStories = false },
                containerColor = SurfaceDark,
                title = { Text(if (selectedStory < 0) "Ҳикояҳои англисӣ" else classicStories[selectedStory].title, color = Gold, fontWeight = FontWeight.Bold) },
                text = {
                    Column(modifier = Modifier.heightIn(max = 500.dp).verticalScroll(rememberScrollState())) {
                        if (selectedStory < 0) {
                            Text("Ҳикояҳои омӯзишӣ аз асарҳои классикии нависандагони Британия. Матнҳо бозгӯии кӯтоҳшуда барои омӯзиши забон мебошанд.", color = Muted)
                            Spacer(Modifier.height(10.dp))
                            classicStories.forEachIndexed { index, story ->
                                Card(
                                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp).clickable { selectedStory = index },
                                    colors = CardDefaults.cardColors(containerColor = Midnight),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Column(Modifier.padding(13.dp)) {
                                        Text(story.title, color = Ivory, fontWeight = FontWeight.Bold)
                                        Text(story.author, color = Gold, fontSize = 12.sp)
                                    }
                                }
                            }
                        } else {
                            TextButton(onClick = { selectedStory = -1 }) { Text("← Ҳамаи ҳикояҳо", color = Gold) }
                            val story = classicStories[selectedStory]
                            Text(story.author, color = Gold, fontSize = 12.sp)
                            Text("English", color = Gold, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 10.dp))
                            Text(story.english, color = Ivory, lineHeight = 21.sp)
                            Text("Тоҷикӣ", color = Gold, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 16.dp))
                            Text(story.tajik, color = Ivory, lineHeight = 21.sp)
                            TextButton(onClick = {
                                if (ttsReady) { speech.language = Locale.UK; speech.speak(story.english, TextToSpeech.QUEUE_FLUSH, null, "story_$selectedStory") }
                            }) { Icon(Icons.Default.VolumeUp, contentDescription = null, tint = Gold); Spacer(Modifier.width(5.dp)); Text("Шунидан", color = Gold) }
                        }
                    }
                },
                confirmButton = { TextButton(onClick = { showStories = false }) { Text("Пӯшидан", color = Gold) } }
            )
        }

        if (showHistory) {
            AlertDialog(
                onDismissRequest = { showHistory = false },
                containerColor = SurfaceDark,
                title = { Text("История поиска", color = Gold, fontWeight = FontWeight.Bold) },
                text = {
                    Column(modifier = Modifier.heightIn(max = 360.dp).verticalScroll(rememberScrollState())) {
                        if (searchHistory.isEmpty()) {
                            Text("Шумо ҳоло калимаҳоро ҷустуҷӯ накардаед.", color = Ivory)
                        } else {
                            searchHistory.forEach { item ->
                                TextButton(onClick = {
                                    query = item
                                    showHistory = false
                                }) { Text(item, color = Ivory) }
                            }
                        }
                    }
                },
                confirmButton = {
                    Row {
                        TextButton(onClick = {
                            searchHistory = emptyList()
                            preferences.edit().remove("search_history").apply()
                        }) { Text("Тоза кардан", color = Muted) }
                        TextButton(onClick = { showHistory = false }) { Text("Пӯшидан", color = Gold) }
                    }
                }
            )
        }

        if (showPrivacy) {
            MaterialTheme(colorScheme = darkColorScheme(
                primary = EmeraldBright,
                secondary = Gold,
                background = Midnight,
                surface = SurfaceDark,
                onBackground = Ivory,
                onSurface = Ivory
            )) {
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
                            "Избранные слова и недавняя история поиска сохраняются локально на устройстве. " +
                            "Они используются только внутри приложения и не передаются разработчику. " +
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
}
