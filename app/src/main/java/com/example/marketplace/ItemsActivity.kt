package com.example.marketplace

import android.os.Bundle
import android.os.PersistableBundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

class ItemsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Load items with applied filter

        setContent {
            // A surface container using the 'background' color from the theme
            Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                LazyColumn {
                    items(50) {
                        displayItem(it)
                    }
                }
            }
        }
    }

    @Composable
    fun displayItem(itemId: Int) {
        Row (
            Modifier
                .padding(3.dp)
                .border(BorderStroke(1.dp, Color.Black), shape = RoundedCornerShape(10.dp))
                .height(150.dp)
                .fillMaxWidth()
        ) {
            Image(painter = painterResource(id = R.drawable.sljivik1), contentDescription = "Item thumbnail", modifier = Modifier
                .width(160.dp)
                .height(120.dp))
            Column (
                modifier = Modifier.padding(10.dp)
            ){
                Text(text = "[Item $itemId] name")
                Text(text = "[Item $itemId] description")
                Text(text = "[Item $itemId] price: $499")
            }
        }
    }
}