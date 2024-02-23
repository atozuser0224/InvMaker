import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.platform.Font
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun BoxScope.FuncView(
    num: MutableIntState,
    inv_item_name_List: SnapshotStateMap<Int, String>,
    inv_item_lore_List: SnapshotStateMap<Int, MutableList<String>>,
    x: MutableIntState,
    y: MutableIntState,
    inv_item_List: SnapshotStateMap<Int, String>,
    inv_item_selected_List: SnapshotStateMap<Int, Boolean>
) {
    Surface(modifier = Modifier.align(Alignment.TopCenter),color = MaterialTheme.colors.surface, elevation = 8.dp, shape = RoundedCornerShape(0.dp,0.dp,30.dp,30.dp)) {
        Box(Modifier.fillMaxHeight(1f)
            .fillMaxWidth(0.33f)){
            Column(horizontalAlignment = Alignment.Start, modifier = Modifier
                .fillMaxSize()) {
                Row {
                    Text("세로줄 조정 : ",Modifier.weight(4f).height(50.dp).offset(y=5.dp), style = MaterialTheme.typography.body1, fontWeight = FontWeight.Bold, fontFamily = fontFamily, fontSize = 36.sp)
                    numTextField(num,Modifier.weight(1f))
                }
            }
            ClickeItemTab(inv_item_name_List,inv_item_lore_List,x,y,Modifier.align(Alignment.BottomCenter), inv_item_List = inv_item_List,inv_item_selected_List)
        }

    }
}
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BoxScope.ClickeItemTab(
    inv_item_name_List: SnapshotStateMap<Int, String>,
    inv_item_lore_List: SnapshotStateMap<Int, MutableList<String>>,
    x: MutableIntState,
    y: MutableIntState,
    modifier: Modifier = Modifier,
    inv_item_List: SnapshotStateMap<Int, String>,
    inv_item_selected_List: SnapshotStateMap<Int, Boolean>
) {
    val index = x.value+(y.value*9)
    AnimatedVisibility(inv_item_List[index] != null,modifier = modifier){
        Surface(color = MaterialTheme.colors.surface, elevation = 12.dp, shape = RoundedCornerShape(30.dp,30.dp,0.dp,0.dp)) {
            Column(horizontalAlignment = Alignment.Start, modifier = Modifier
                .fillMaxHeight(.33f)
                .fillMaxWidth()) {
                OutlinedTextField(
                    value = inv_item_name_List[index]?:inv_item_List[index]?:"",
                    onValueChange = {
                        inv_item_name_List[index] = it
                    },
                    label = { Text("아이템 이름")},
                    shape = RoundedCornerShape(30.dp),
                    modifier = Modifier.fillMaxWidth(0.5f).align(Alignment.CenterHorizontally)
                )
                if (inv_item_lore_List[index] == null){
                    inv_item_lore_List[index] = mutableListOf("")
                }
                var rep by remember {
                    mutableStateOf<String>("")
                }
//                FilterChip(
//                    selected = inv_item_selected_List[index]?:false,
//                    onClick = {
//                        inv_item_selected_List[index] = !(inv_item_selected_List[index]?:false)
//                    }
//                ){
//                    Icon(Icons.Filled.)
//                }
                inv_item_lore_List[index]?.let {
                    OutlinedTextField(
                        value = rep,
                        onValueChange = {st->
                            rep = st
                            inv_item_lore_List[index] = st.split("\n").toMutableList()
                        },
                        singleLine = false,
                        label = { Text("lore")},
                        shape = RoundedCornerShape(30.dp),
                        modifier = Modifier.fillMaxWidth(1f).align(Alignment.CenterHorizontally)
                    )
                }
            }
        }
    }
}

val fontFamily = FontFamily(
    Font(
        resource = "font/minecraft.ttf",
    )
)
