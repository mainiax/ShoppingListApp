package com.example.shoppinglistapp

import AlertDialogComposable
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shoppinglistapp.ui.theme.ShoppingListAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ShoppingListAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    val openAlertDialog = remember { mutableStateOf(false) }
    val itemName = remember { mutableStateOf("") }
    val itemQuantity = remember { mutableStateOf("") }
    var shoppingitems = remember {  mutableStateOf(listOf<ShoppingListItem>(  ShoppingListItem(1,"item1",1),)) };

    Column (modifier = modifier.fillMaxSize(), verticalArrangement = Arrangement.Center){
        Row(modifier = Modifier
            .weight(0.5f)
            .fillMaxWidth(), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.Bottom){
            inputItemUi(Modifier,openAlertDialog,  itemName=itemName.value,
                onItemChange={it->itemName.value=it} ,
                itemQuantity = itemQuantity.value,
                onQuantityChange = {it->itemQuantity.value=it},
                shoppingItems = shoppingitems
            )
        }

        Row(modifier = Modifier
            .weight(6f)
            .fillMaxWidth(), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.Bottom){
           itemsListUi(Modifier,shoppingitems)
        }
        Row(modifier = Modifier
            .weight(0.9f)
            .fillMaxWidth(), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.Bottom){
            addItemsUi(modifier = Modifier, openAlertDialog = openAlertDialog,
                onDismissRequest = {it-> openAlertDialog.value = it},
                onConfirmation = {it-> openAlertDialog.value = it},
                itemName=itemName.value,
                onItemChange={it->itemName.value=it}  ,
                itemQuantity=itemQuantity.value,
               onQuantityChange = {it->itemQuantity.value=it}
            )
        }

    }
}
@Composable
fun itemsListUi(modifier: Modifier,shoppingItems:MutableState<List<ShoppingListItem>>){
    LazyColumn(modifier = Modifier
        .padding(bottom = 16.dp)
        .fillMaxSize()){

        items(shoppingItems.value) { i ->
            when(i.isChecked){
                true -> ShoppingListItemEditor(item = i, onItemChange = {})
                false -> ShoppingListItem(item = i, onEditClick = {
                    shoppingItems.value = shoppingItems.value.map {
                        if (it == i) it.copy(isChecked = true) else it
                    }
                                                                  },
                    onDeleteClick = {}, modifier = Modifier)
            }

        }
    }
}
@Composable
fun ShoppingListItem(item: ShoppingListItem,
                     onEditClick: () -> Unit,
                     onDeleteClick: () -> Unit, modifier: Modifier)
{

    Box(
        modifier = Modifier
            .padding(10.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(
                colorResource(id = R.color.purple_200)
                    .copy(alpha = 0.2f)
            )
            .fillMaxWidth()
            .border(
                2.dp,
                colorResource(id = R.color.purple_200),
                RoundedCornerShape(20.dp)
            ),
        contentAlignment = Alignment.Center,
    ){
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically) {
            Column (modifier = Modifier.padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)){
                var icon = Icons.Default.ShoppingCart;
                Icon(icon, contentDescription = "Example Icon",
                    tint = colorResource(id = R.color.purple_200),modifier = Modifier
                        .padding(5.dp)
                        .size(35.dp)
                        .clip(
                            RoundedCornerShape(20.dp)
                        )
                )
            }

            ShoppingListItemInfo(
                itemName = item.name,
                itemQuantity = item.quantity,
                modifier = Modifier
                    .weight(1f) // Take up remaining space
            )

            ActionButtonUI(
                onDeleteClick = {},
                onEditClick = onEditClick
            )
        }
    }
}

@Composable
fun ShoppingListItemEditor(item: ShoppingListItem,
                           onItemChange: (ShoppingListItem) -> Unit) {

    var editedItemName = remember { mutableStateOf(item.name) }
    var editedItemQuantity = remember { mutableStateOf(item.quantity.toString()) }
    var isEditing = remember { mutableStateOf(item.isChecked) }

    Row ( modifier = Modifier.fillMaxWidth().background(Color.Transparent)
        .padding(8.dp), horizontalArrangement = Arrangement.SpaceEvenly){
        TextField(
            value = editedItemName.value,
            onValueChange = { editedItemName.value = it },
            singleLine = true,
           modifier =  Modifier.wrapContentSize().padding(8.dp)
        )

        TextField(
            value = editedItemQuantity.value,
            onValueChange = { editedItemQuantity.value = it },
            singleLine = true,
            modifier =  Modifier.wrapContentSize().padding(8.dp)
        )
        Button(onClick = {
            isEditing.value = false

            val updatedItem = item.copy(name = editedItemName.value,
                quantity = editedItemQuantity.value.toIntOrNull()?:1)
            onItemChange(updatedItem)
        }) {
            Text("Save")}

    }
}

@Composable
fun ActionButtonUI(
    onDeleteClick: () -> Unit,
    onEditClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .padding(8.dp, 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        IconButton(onClick = onDeleteClick) {
            Icon(
                imageVector = Icons.Filled.Delete,
                contentDescription = "Extended floating action button.",
                modifier = Modifier.size(35.dp),
                tint = colorResource(id = R.color.purple_200)
            )
        }
        IconButton(onClick = onEditClick) {
            Icon(
                imageVector = Icons.Filled.Edit,
                contentDescription = "Extended floating action button.",
                modifier = Modifier.size(35.dp),
                tint = colorResource(id = R.color.purple_200)
            )
        }
    }
}

@Composable
fun ShoppingListItemInfo(
    itemName: String,
    itemQuantity: Int,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row {
            Text(
                text = itemName.substring(0,1).uppercase() + itemName.substring(1),
                color = colorResource(id = R.color.purple_200),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "Qty: $itemQuantity",
                color = colorResource(id = R.color.purple_200),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
@Composable
fun inputItemUi(modifier: Modifier,
                openAlertDialog: MutableState<Boolean>,
                itemName:String,onItemChange:(String)->Unit,
                itemQuantity: String,
                onQuantityChange:(String)->Unit,shoppingItems:MutableState<List<ShoppingListItem>>
){

    Column (horizontalAlignment = Alignment.CenterHorizontally,verticalArrangement = Arrangement.Center, modifier = Modifier.padding(bottom = 16.dp)){
        when{
            openAlertDialog.value -> {
                AlertDialogComposable(
                    onDismissRequest = {
                        openAlertDialog.value = false
                    },
                    onConfirmation = {
                        openAlertDialog.value = false
                    },
                    dialogText = "This is some text", dialogTitle = "this is a title", icon =Icons.Default.ShoppingCart,
                    itemNames = itemName,
                    onItemChange = {
                        onItemChange(it)
                    },
                    ItemQuantity = itemQuantity,
                    onQuantityChange = {
                        onQuantityChange(it)
                    },
                    shoppingItems = shoppingItems,

                )
            }

        }
    }
}
@Composable
fun addItemsUi(modifier: Modifier, openAlertDialog: MutableState<Boolean>,
               onDismissRequest: (Boolean) -> Unit,
               onConfirmation: (Boolean) -> Unit,
               itemName:String,onItemChange:(String)->Unit,
               itemQuantity:String,onQuantityChange:(String)->Unit){

    Column (horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.padding(bottom = 16.dp)) {
        ExtendedFloatingActionButton(
            onClick = { openAlertDialog.value = true},
            icon = { Icon(Icons.Filled.Edit, "Extended floating action button.") },
            text = { Text(text = "Add Item") },
            modifier = modifier
                .align(Alignment.CenterHorizontally)
                .clip(RoundedCornerShape(10.dp)),
            containerColor = colorResource(id = R.color.purple_200),
            contentColor = colorResource(id = R.color.white)
        )
    }

}






@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GreetingPreview() {
    ShoppingListAppTheme {
        Greeting("Android")
    }
}