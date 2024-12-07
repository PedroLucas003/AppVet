package com.example.appvet

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.RoomDatabase
import com.example.appvet.ui.theme.appvetTheme
import com.google.firebase.auth.FirebaseAuth


data class Tip(
    val title: String,
    val description: String,
    val imageResId: Int,
    val location: String,
    val rating: Float,
    val category: String
)

class MainActivity : ComponentActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()

        val currentUser = auth.currentUser
        if (currentUser == null) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        setContent {
            appvetTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CombinedScreen()
                }
            }
        }
    }

    @Composable
    fun CombinedScreen() {
        val tips = listOf(
            Tip("Petshop Amigão", "Banho e tosa completos.", R.drawable.petshop, "São Paulo, SP", 4.5f, "Petshop"),
            Tip("Veterinária Central", "Consultas e urgências.", R.drawable.veterinaria, "Rio de Janeiro, RJ", 4.8f, "Veterinária"),
            Tip("Dog Walker Express", "Passeios diários.", R.drawable.dogwlaker, "Belo Horizonte, MG", 4.7f, "Dog Walker"),
            Tip("Hotel Pet Férias", "Hospedagem confortável.", R.drawable.hotal, "Curitiba, PR", 4.6f, "Hotel")
        )

        var selectedCategory by remember { mutableStateOf<String?>(null) }

        val filteredTips = remember(selectedCategory) {
            if (selectedCategory == null) tips else tips.filter { it.category.equals(selectedCategory, ignoreCase = true) }
        }

        val duplicatedTips = filteredTips.flatMap { listOf(it, it, it, it) } // Duplicando 4 vezes

        Column(modifier = Modifier.fillMaxSize()) {
            Text(
                text = "Serviços",
                modifier = Modifier.padding(8.dp),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .padding(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(horizontal = 8.dp)
            ) {
                items(listOf("Petshop", "Veterinária", "Dog Walker", "Hotel")) { category ->
                    val imageResId = when (category) {
                        "Petshop" -> R.drawable.petshop
                        "Veterinária" -> R.drawable.veterinaria
                        "Dog Walker" -> R.drawable.dogwlaker
                        "Hotel" -> R.drawable.hotal
                        else -> R.drawable.placeholder
                    }

                    CategoryCard(
                        category = category,
                        imageResId = imageResId,
                        onClick = { selectedCategory = category }
                    )
                }
            }

            Text(
                text = "Locais Encontrados",
                modifier = Modifier.padding(8.dp),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(duplicatedTips) { tip ->
                    var showDetails by remember { mutableStateOf(false) }
                    CatalogCard(
                        tip = tip,
                        showDetails = showDetails,
                        onDetailsClick = { showDetails = !showDetails }
                    )
                }
            }
        }
    }


    //internet/api
    @Entity(tableName = "example_table")
    data class ExampleEntity(
        @PrimaryKey(autoGenerate = true) val id: Int,
        val name: String
    )
    @Dao
    interface ExampleDao {
        @Query("SELECT * FROM example_table")
        fun getAll(): List<ExampleEntity>
    }

    @Database(entities = [ExampleEntity::class], version = 1)
    abstract class ExampleDatabase : RoomDatabase() {
        abstract fun exampleDao(): ExampleDao
    }



    @Composable
    fun CategoryCard(category: String, imageResId: Int, onClick: () -> Unit) {
        Card(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .width(200.dp)
                .height(180.dp)
                .clickable { onClick() },
            colors = CardDefaults.cardColors(containerColor = Color(0xFF0288D1))
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = imageResId),
                    contentDescription = category,
                    modifier = Modifier
                        .size(100.dp)
                        .padding(bottom = 8.dp)
                )
                Text(
                    text = category,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    color = Color.White
                )
            }
        }
    }

    @Composable
    fun CatalogCard(tip: Tip, showDetails: Boolean, onDetailsClick: () -> Unit) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Row {
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = if (showDetails) 8.dp else 0.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF01579B))
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(id = tip.imageResId),
                            contentDescription = tip.title,
                            modifier = Modifier.size(100.dp)
                        )
                        Text(
                            text = tip.title,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White
                        )
                        Text("Local: ${tip.location}", color = Color.LightGray)
                        Text("Avaliação: ${tip.rating} ★", color = Color(0xFFFFD700))
                        Button(onClick = onDetailsClick) {
                            Text(
                                if (showDetails) "Fechar Detalhes" else "Ver Detalhes",
                                color = Color.White
                            )
                        }
                    }
                }

                if (showDetails) {
                    Card(
                        modifier = Modifier
                            .width(250.dp)
                            .padding(start = 8.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.Start
                        ) {
                            Text(
                                text = "Detalhes:",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = tip.description,
                                color = Color.Gray
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Contato:",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.Black
                            )
                            Text(
                                text = "Telefone: (11) 98765-4321",
                                color = Color.Gray
                            )
                        }
                    }
                }
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun PreviewCombinedScreen() {
        appvetTheme { CombinedScreen() }
    }
}
