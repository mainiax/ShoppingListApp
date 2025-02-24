

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column


import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SmallFloatingActionButton

import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.shoppinglistapp.R
import com.example.shoppinglistapp.ShoppingListItem


@Composable
fun AlertDialogComposable(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    dialogTitle: String,
    dialogText: String,
    icon: ImageVector,
    itemNames:String,
    onItemChange:(String)->Unit,
    ItemQuantity:String,
    onQuantityChange:(String)->Unit,
    shoppingItems:MutableState<List<ShoppingListItem>>
) {
    val localContext = LocalContext.current
    val toast = Toast.makeText(localContext, "input value must be greater than 0 and a number", Toast.LENGTH_SHORT)
    val focusManager = LocalFocusManager.current
    AlertDialog(

        icon = {
            Box(

                modifier = Modifier
                    .padding(10.dp)
                    .size(40.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        colorResource(id = R.color.purple_200)
                            .copy(alpha = 0.4f)
                    ),
                contentAlignment = Alignment.Center

            ){
                Icon(icon, contentDescription = "Example Icon", tint = colorResource(id = R.color.purple_200),modifier = Modifier
                    .padding(5.dp)
                    .size(35.dp)
                    .clip(
                        RoundedCornerShape(20.dp)
                    )

                )
            }

        },
        title = {
            Text(
            text = "Add Shopping Item",
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                fontSize = MaterialTheme.typography.titleLarge.fontSize,
                 color = colorResource(id = R.color.purple_200)
            )
        }
,
        text = {

            Column {
                OutlinedTextField( value = itemNames,
                    onValueChange = onItemChange,
                    label = { Text("Item Name") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),

                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,  // Restrict input to numbers
                        imeAction = ImeAction.Done          // Set action to "Done"
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            focusManager.clearFocus()  // Dismiss keyboard when "Done" is pressed
                        }
                    )

                )

                OutlinedTextField( value = ItemQuantity,
                    onValueChange = onQuantityChange,
                    label = { Text("Item Quantity") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                  //  KeyboardActions = KeyboardActions( imeAction = ImeAction.Done)

                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,  // Restrict input to numbers
                        imeAction = ImeAction.Done          // Set action to "Done"
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            focusManager.clearFocus()  // Dismiss keyboard when "Done" is pressed
                        }
                    )
                )

            }

        },


        onDismissRequest = {
            onDismissRequest()
        },


        confirmButton = {
//            TextButton(
//                onClick = {
//                    onConfirmation()
//                }
//            ) {
//                Text("Confirm")
//            }

            Row(modifier = Modifier.fillMaxWidth().padding(8.dp), horizontalArrangement = Arrangement.SpaceBetween) {
              dialogButton (onClick = {
                  if(itemNames.isNotEmpty() && ItemQuantity.isNotEmpty() && isNumeric(ItemQuantity)){
                      val newShoppingItem = ShoppingListItem(
                          shoppingItems.value.size+1,
                          itemNames,  ItemQuantity.toInt()
                      )
                      shoppingItems.value +=newShoppingItem
                      onConfirmation()
                      onItemChange("")
                      onQuantityChange("")
                      Log.d("shoppingItems", shoppingItems.value.toString())
                  } else{
                    toast.show()
                  }
               },Icons.Filled.Add)
                dialogButton (onClick = {onDismissRequest()},  Icons.Filled.Clear)
            }
        },

    )
}


fun isNumeric(str: String): Boolean = str.toDoubleOrNull() != null;

@Composable
fun dialogButton(onClick: () -> Unit,icon: ImageVector) {
    Column  {

        SmallFloatingActionButton(
            onClick = { onClick() },
            modifier = Modifier.align(Alignment.CenterHorizontally).clip(RoundedCornerShape(10.dp)),
            containerColor = colorResource(id = R.color.purple_200),
            contentColor = colorResource(id = R.color.white)
        ) {
            Icon(icon, "Small floating action button.")
        }
    }
}