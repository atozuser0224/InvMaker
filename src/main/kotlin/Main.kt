import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.*
import androidx.compose.ui.window.WindowPosition.PlatformDefault.x
import java.awt.SystemColor.text
import java.io.File

@Composable
@Preview
fun App() {
    var num = remember { mutableIntStateOf(1) }
    val itemList = listImageFilesInItemDirectory()
    val inv_itemList = listOf<String>()
    var x = remember {
        mutableIntStateOf(20)
    }
    var y = remember {
        mutableIntStateOf(20)
    }
    var slot = slot(x,y)
    MaterialTheme {
        Box(Modifier.fillMaxSize()){
            invView(num,inv_itemList,slot)
            itemView(itemList)
        }
    }
}
@Composable
fun BoxScope.invView(num: MutableIntState,list: List<String>,slot: slot) {
    Surface(modifier = Modifier.align(Alignment.TopStart),color = MaterialTheme.colors.surface, elevation = 5.dp, shape = RoundedCornerShape(0.dp,0.dp,30.dp,30.dp)) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
            .fillMaxHeight(0.44f)
            .fillMaxWidth(0.33f)
            .align(Alignment.TopStart)) {
            showInv(num.value, list,slot)
            numTextField(num)
        }
    }

}
@Composable
fun BoxScope.itemView(list: List<String>) {
    Surface(modifier = Modifier.align(Alignment.BottomStart),color = MaterialTheme.colors.surface, elevation = 8.dp, shape = RoundedCornerShape(30.dp,30.dp,0.dp,0.dp)) {
        LazyColumn(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
            .fillMaxHeight(0.5f)
            .fillMaxWidth(0.33f)
            .padding(10.dp)
            ) {
            items(list.chunked(5)){
                Row(Modifier.fillParentMaxWidth().height(100.dp)) {
                    repeat(it.size){n->
                        Column(Modifier.offset(x=10.dp,y=10.dp).weight(1f)) {
                            Image(
                                painter = painterResource("item/${it[n]}.png"),"img",
                                modifier = Modifier.scale(3f).offset(x=10.dp),
                                contentScale = ContentScale.Fit
                            )
                            Text(it[n],Modifier.offset(x=-10.dp,y=20.dp))
                        }

                    }
                }
            }
        }
    }
}
fun main() = application {
    Window(onCloseRequest = ::exitApplication, state = WindowState(width = 1920.dp, height = 1080.dp, position = WindowPosition(0.dp,0.dp), placement = WindowPlacement.Maximized), title = "InvManager") {
        App()
    }
}
@Composable
fun numTextField(num : MutableIntState){
    OutlinedTextField(
        value = if (num.value == 0) "" else num.value.toString(),
        onValueChange = {it->
            if (it.matches(Regex("-?\\d+"))) {
                if (it.toInt() > 6){
                    num.value = 6
                }else if (it.toInt() < 0){
                    num.value = 0
                }else{
                    num.value = it.toInt()
                }
            }else if (it == ""){
                num.value = 0
            }
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done
        ),
        modifier = Modifier.fillMaxWidth(0.85f),
        shape = RoundedCornerShape(60.dp),
        textStyle = TextStyle(
            textAlign = TextAlign.Center
        )
    )
}
@Composable
fun showInv(num : Int , list: List<String>,slot: slot){
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
        Image(
            painter = painterResource("topbar.png"),"topbar"
        )
        var n = 0
        repeat(num){y->
            Row(){
                Image(
                    painter = painterResource("part_start.png"),"img",
                    modifier = Modifier.scale(1.2f,1.0f)
                )
                repeat(9){x->
                    Box(){
                        if (slot.x.value == x && slot.y.value == y){
                            Image(
                                painter = painterResource("part_middle.png"),"img",
                                modifier = Modifier.clickable {
                                    slot.x.value = x
                                    slot.y.value = y
                                },
                                colorFilter = ColorFilter.tint(Color(0x66000000))
                            )
                        }else{
                            Image(
                                painter = painterResource("part_middle.png"),"img",
                                modifier = Modifier.clickable {
                                    slot.x.value = x
                                    slot.y.value = y
                                }
                            )
                        }

                        if (n < list.size){
                            list[n++]?.let {
                                Image(
                                    painter = painterResource("item/${it}.png"),"img",
                                    modifier = Modifier.scale(2f).offset(x=10.dp,y=10.dp),
                                    contentScale = ContentScale.Fit
                                )
                            }
                        }
                    }

                }
                Image(
                    painter = painterResource("part_end.png"),"img",
                    modifier = Modifier.scale(1.15f,1.0f)
                )
            }
        }
        Image(
            painter = painterResource("end.png"),"topbar"
        )
    }
}

fun listImageFilesInItemDirectory(): List<String> {
    val itemDir = File("C:\\Users\\wagwa\\Downloads\\InventoryManager\\InvMaker\\src\\main\\resources\\item")
    val imageFileList = mutableListOf<String>()

    if (itemDir.exists() && itemDir.isDirectory) {
        val imageFiles = itemDir.listFiles { file ->
            file.isFile && file.extension in listOf("jpg", "jpeg", "png", "gif") // 이미지 파일 필터링
        }
        imageFiles?.forEach { imageFile ->
            imageFileList.add(imageFile.nameWithoutExtension)
        }
    } else {
        println("Item directory does not exist or is not a directory.")
    }

    return imageFileList
}

data class slot(var x : MutableIntState,var y:MutableIntState)