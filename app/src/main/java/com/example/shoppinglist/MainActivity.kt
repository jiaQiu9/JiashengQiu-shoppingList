package com.example.shoppinglist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.shoppinglist.ui.theme.ShoppingListTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ShoppingListTheme {
                Scaffold(modifier = Modifier.fillMaxSize(),
                    topBar = {
                        TopAppBar(
                            colors = topAppBarColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer,
                                titleContentColor = MaterialTheme.colorScheme.primary,
                            ),
                            title = {
                                Text("Top app bar")
                            }
                        )
                    }
                ) { innerPadding ->
                    Column(
                        modifier= Modifier
                            .padding(innerPadding),

                        ){
                        //Spacer(modifier=Modifier.height(16.dp))
                        shoppingList()
                    }

                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Composable
fun shoppingList(){
    var shoppingListV = remember{ mutableStateListOf<Triple<String,Int,Boolean>>() }
    var first by remember{ mutableStateOf("") }
    var second by remember{ mutableStateOf("") }
    var (snackbarVisibleState, setSnackBarState) = remember { mutableStateOf(false) }



    fun isNumeric(toCheck: String): Boolean{
        return toCheck.toIntOrNull() != null
    }

    Column(modifier=Modifier.fillMaxSize()){

        //Spacer(modifier=Modifier.height(16.dp))
        Row(){
            OutlinedTextField(value=first, onValueChange = {first=it}, label={Text("Item Name")})
        }
        Row(){
            OutlinedTextField(value=second, onValueChange={second=it}, label={Text("Item Quantity/Size")})
        }

        Row(){
            Button(onClick={
                if (first.isNotEmpty() && second.isNotEmpty() && isNumeric(second)){
                    shoppingListV.add(Triple(first, second.toInt(), false))
                    first=""
                    second=""
                } else if (!isNumeric(second)){
                    first=""
                    second=""

                    setSnackBarState(!snackbarVisibleState)
                }
            }){
                Text("Add Item")
            }
        }
        if (snackbarVisibleState) {
            Snackbar(
                action = {
                    Button(onClick = {
                        setSnackBarState(!snackbarVisibleState)
                    }) {
                        Text("Close")
                    }
                },
                modifier = Modifier.padding(8.dp)
            ) { Text(text = "Please enter a numeric value in the second field!!!") }
        }

        Spacer(modifier=Modifier.height(16.dp))
        Row(modifier=Modifier.padding(16.dp)){
            LazyColumn(
                modifier=Modifier.fillMaxSize()

            ){
                items(shoppingListV){
                        pair ->
                    Row(){
                        Text(
                            text= "Name: ${pair.first}, Quantity/Size: ${pair.second}  "
                        )
                        Checkbox(
                            checked= pair.third,
                            onCheckedChange = { checked ->
                                // Update the specific pair's checked state
                                val index = shoppingListV.indexOf(pair)
                                if (index>=0){
                                    shoppingListV[index]= Triple(pair.first, pair.second, checked)

                                }
                            }
                        )
                    }

                }

            }

        }
    }
}


