import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.awt.SystemColor.text

@Composable
fun BoxScope.itemView(list: List<String>, x: MutableIntState, y: MutableIntState, map: SnapshotStateMap<Int, String>,num: MutableIntState) {
    var value by remember {
        mutableStateOf("")
    }
    Surface(modifier = Modifier.align(Alignment.BottomStart),color = MaterialTheme.colors.surface, elevation = 8.dp, shape = RoundedCornerShape(30.dp,30.dp,0.dp,0.dp)) {
        Column(
            Modifier.fillMaxHeight(0.5f)
            .fillMaxWidth(0.33f)
            .padding(10.dp),horizontalAlignment = Alignment.CenterHorizontally) {
            val focusManager = LocalFocusManager
            OutlinedTextField(
                value = value,
                onValueChange = {it->
                    value= it
                },
                modifier = Modifier.fillMaxWidth(0.85f),
                shape = RoundedCornerShape(60.dp),
                textStyle = TextStyle(
                    textAlign = TextAlign.Center,
                    fontSize = 28.sp,
                    fontFamily = fontFamily
                ),
                keyboardActions = KeyboardActions(onDone = {
                })
            )
            LazyColumn(modifier = Modifier
            ) {
                items(list
                    .filter {
                        if (it.all { !it.isDigit() }){
                            true
                        }else{
                            it.filter { it.isDigit()} == "00" || it.filter { it.isDigit()} == "0"
                        }

                    }
                    .filter {
                        if (it != ""){
                            it.contains(value)
                        }else{
                            true
                        }
                    }
                    .chunked(5)){
                    Row(Modifier.fillParentMaxWidth().height(130.dp).padding(top = 20.dp)) {
                        repeat(it.size){n->
                            Column(Modifier.weight(1f).fillMaxHeight().clickable {
                                if (x.value+(y.value*9)<num.value * 9){
                                    map[x.value+(y.value*9)] = it[n].lowercase()
                                }
                            }) {
                                Image(
                                    painter = painterResource("item/${it[n]}.png"),"img",
                                    modifier = Modifier.scale(3f).weight(3f).align(Alignment.CenterHorizontally),
                                    contentScale = ContentScale.Fit
                                )
                                Text(it[n].uppercase(),
                                    Modifier.fillMaxWidth().weight(1.3f), textAlign = TextAlign.Center, fontFamily = fontFamily, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }

    }
}