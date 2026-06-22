package com.example.latihanlistbuah

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.latihanlistbuah.ui.theme.LatihanlistbuahTheme

/**
 * Data class untuk menyimpan informasi buah.
 * imageRes diisi dengan ID resource dari folder drawable.
 */
data class Fruit(
    val name: String,
    val origin: String,
    val taste: String,
    val description: String,
    val benefits: String,
    val imageRes: Int,
    val color: Color = Color(0xFF2E7D32) // Warna hijau default seperti di gambar
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LatihanlistbuahTheme {
                FruitApp()
            }
        }
    }
}

@Composable
fun FruitApp() {
    var selectedFruit by remember { mutableStateOf<Fruit?>(null) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFF5F5F5) // Background abu-abu sangat muda
    ) {
        if (selectedFruit == null) {
            FruitListScreen(onFruitClick = { selectedFruit = it })
        } else {
            FruitDetailScreen(fruit = selectedFruit!!, onBack = { selectedFruit = null })
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FruitListScreen(onFruitClick: (Fruit) -> Unit) {
    val fruits = remember { getFruitData() }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Daftar Buah-buahan", fontWeight = FontWeight.Bold, color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF212121) // Top bar hitam/gelap sesuai gambar
                )
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color.White)
        ) {
            items(fruits) { fruit ->
                FruitItem(fruit = fruit, onClick = { onFruitClick(fruit) })
                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), thickness = 0.5.dp, color = Color.LightGray)
            }
        }
    }
}

@Composable
fun FruitItem(fruit: Fruit, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = fruit.imageRes),
            contentDescription = fruit.name,
            modifier = Modifier
                .size(70.dp)
                .padding(4.dp),
            contentScale = ContentScale.Fit
        )
        Spacer(modifier = Modifier.width(20.dp))
        Text(
            text = fruit.name,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Black
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FruitDetailScreen(fruit: Fruit, onBack: () -> Unit) {
    BackHandler { onBack() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detail: ${fruit.name}", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF212121)
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .background(Color(0xFFF5F5F5))
        ) {
            // Header Area dengan background hijau
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(fruit.color)
                    .padding(vertical = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    modifier = Modifier.size(200.dp),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Image(
                        painter = painterResource(id = fruit.imageRes),
                        contentDescription = fruit.name,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        contentScale = ContentScale.Fit
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = fruit.name,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Column(modifier = Modifier.padding(16.dp)) {
                // Baris Info: Asal & Rasa
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    InfoCard(label = "Asal", value = fruit.origin, icon = "🌍", modifier = Modifier.weight(1f))
                    InfoCard(label = "Rasa", value = fruit.taste, icon = "😋", modifier = Modifier.weight(1f))
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Section Deskripsi
                SectionTitle(title = "Deskripsi", icon = "📖")
                DetailContentBox(content = fruit.description)

                Spacer(modifier = Modifier.height(20.dp))

                // Section Manfaat Kesehatan
                SectionTitle(title = "Manfaat Kesehatan", icon = "✅")
                DetailContentBox(content = fruit.benefits)
            }
        }
    }
}

@Composable
fun InfoCard(label: String, value: String, icon: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = icon, fontSize = 12.sp)
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = label, fontSize = 12.sp, color = Color.Gray)
            }
            Text(text = value, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color.Black)
        }
    }
}

@Composable
fun SectionTitle(title: String, icon: String) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(bottom = 8.dp)) {
        Text(text = icon, fontSize = 18.sp)
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = title,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF2E7D32)
        )
    }
}

@Composable
fun DetailContentBox(content: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Text(
            text = content,
            modifier = Modifier.padding(16.dp),
            fontSize = 14.sp,
            lineHeight = 20.sp,
            color = Color.DarkGray
        )
    }
}

fun getFruitData(): List<Fruit> {
    // Pastikan nama file di drawable sesuai: apel, jeruk, pisang, dst.
    return listOf(
        Fruit("Apel", "Asia Tengah", "Manis-asam segar", 
            "Apel adalah buah pomaceous dari pohon Malus domestica. Buah ini merupakan salah satu buah paling populer di dunia dengan ratusan varietas berbeda.", 
            "• Menjaga kesehatan jantung\n• Menurunkan risiko diabetes\n• Membantu pencernaan", R.drawable.apel),
        Fruit("Jeruk", "Asia Tenggara", "Asam manis", "Buah yang kaya vitamin C dan memiliki aroma yang segar.", "• Meningkatkan imun\n• Menjaga kesehatan kulit", R.drawable.jeruk),
        Fruit("Pisang", "Asia Tenggara", "Manis lembut", "Buah yang kaya akan kalium, sangat baik untuk energi cepat.", "• Menjaga tekanan darah\n• Sumber energi", R.drawable.pisang),
        Fruit("Mangga", "India", "Manis legit", "Dikenal sebagai raja buah tropis, kaya akan vitamin A.", "• Kesehatan mata\n• Pencernaan", R.drawable.mangga),
        Fruit("Anggur", "Timur Tengah", "Manis segar", "Kaya akan antioksidan resveratrol.", "• Anti-penuaan\n• Kesehatan jantung", R.drawable.anggur),
        Fruit("Stroberi", "Eropa", "Asam manis", "Buah berwarna merah terang dengan bintik kecil di kulitnya.", "• Antioksidan tinggi\n• Membantu diet", R.drawable.stroberi),
        Fruit("Nanas", "Amerika Selatan", "Asam manis", "Memiliki tekstur yang unik dan kaya akan bromelain.", "• Anti inflamasi\n• Pencernaan", R.drawable.nanas),
        Fruit("Semangka", "Afrika", "Manis berair", "Mengandung kadar air yang sangat tinggi.", "• Menjaga hidrasi\n• Kesehatan ginjal", R.drawable.semangka),
        Fruit("Melon", "Persia", "Manis wangi", "Memiliki tekstur daging yang lembut dan menyegarkan.", "• Hidrasi tubuh\n• Kesehatan kulit", R.drawable.melon),
        Fruit("Pepaya", "Meksiko", "Manis lembut", "Sangat baik untuk pencernaan karena enzim papain.", "• Melancarkan BAB\n• Kesehatan usus", R.drawable.pepaya),
        Fruit("Durian", "Asia Tenggara", "Manis legit", "Raja buah yang memiliki aroma sangat kuat.", "• Penambah energi\n• Kesehatan tulang", R.drawable.durian),
        Fruit("Rambutan", "Asia Tenggara", "Manis segar", "Buah yang kulitnya memiliki rambut halus.", "• Meningkatkan imun\n• Menjaga kolesterol", R.drawable.rambutan),
        Fruit("Leci", "Tiongkok", "Manis aromatik", "Buah dengan daging putih bening yang wangi.", "• Kesehatan darah\n• Vitamin C", R.drawable.leci),
        Fruit("Salak", "Indonesia", "Manis sepat", "Dikenal sebagai snake fruit karena kulitnya bersisik.", "• Menjaga kesehatan mata\n• Mengatasi diare", R.drawable.salak),
        Fruit("Jambu Biji", "Amerika Tropis", "Manis segar", "Memiliki kandungan vitamin C tertinggi di antara buah umum.", "• Kesehatan gusi\n• Anti kanker", R.drawable.jambubiji),
        Fruit("Kiwi", "Tiongkok", "Asam manis", "Daging buah berwarna hijau dengan biji hitam kecil.", "• Pencernaan protein\n• Tidur nyenyak", R.drawable.kiwi),
        Fruit("Buah Naga", "Amerika Tengah", "Manis tawar", "Memiliki kulit unik dan daging buah berbintik.", "• Detoks tubuh\n• Rendah kalori", R.drawable.buahnaga),
        Fruit("Alpukat", "Meksiko", "Gurih lemak", "Kaya akan lemak sehat yang baik untuk jantung.", "• Lemak baik\n• Kesehatan otak", R.drawable.alpukat),
        Fruit("Sirsak", "Karibia", "Asam manis", "Daging buah berwarna putih dengan tekstur berserat.", "• Anti kanker alami\n• Penurun demam", R.drawable.sirsak),
        Fruit("Markisa", "Brasil", "Asam segar", "Sering digunakan untuk sirup karena aromanya kuat.", "• Relaksasi\n• Kesehatan usus", R.drawable.markisa)
    )
}
