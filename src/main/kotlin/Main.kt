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
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.SwingPanel
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
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.*
import androidx.compose.ui.window.WindowPosition.PlatformDefault.x
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea
import org.fife.ui.rsyntaxtextarea.SyntaxConstants
import org.fife.ui.rtextarea.RTextScrollPane
import java.awt.SystemColor.text
import java.io.File
import javax.swing.BoxLayout
import javax.swing.JPanel

@Composable
@Preview
fun App() {
    var num = remember { mutableIntStateOf(1) }
    val itemList = listImageFilesInItemDirectory()
    val frame_name = remember { mutableStateOf("이곳을 눌러 수정") }
    val inv_itemList = remember {
        mutableStateMapOf<Int,String>()
    }
    val x = remember {
        mutableIntStateOf(20)
    }
    val y = remember {
        mutableIntStateOf(20)
    }
    var code = remember {
        mutableStateOf("")
    }
    val codeMaker = CodeMaker(inv_itemList,num,frame_name)
    LaunchedEffect(inv_itemList.toList(),num.value,frame_name.value){
        code.value = codeMaker.make()
    }
    MaterialTheme {
        Box(Modifier.fillMaxSize()) {
            invView(num, inv_itemList, x, y,frame_name)
            itemView(itemList, x, y, inv_itemList,num)
            FuncView(num)
            Box(Modifier.align(Alignment.TopEnd)){
                CodeEditor(code,Modifier.fillMaxWidth(0.33f).fillMaxHeight())
            }
        }
    }
}

fun main() = application {
    Window(onCloseRequest = ::exitApplication, icon = painterResource("item/chest.png"), state = WindowState(width = 1920.dp, height = 1080.dp, position = WindowPosition(0.dp,0.dp), placement = WindowPlacement.Maximized), title = "InvManager") {
        App()
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

    return imageFileList.filter {
        !it.contains("top")&&!it.contains("bottom")&&!it.contains("side")
    }
}


var rtCodeEditor: RTextScrollPane? = null

@Composable
fun CodeEditor(code: MutableState<String>, modifier: Modifier = Modifier) {
    var textArea by remember { mutableStateOf<RSyntaxTextArea?>(null) }
    var sp by remember { mutableStateOf<RTextScrollPane?>(null) }

    DisposableEffect(Unit) {
        textArea = RSyntaxTextArea(20, 60)
        textArea?.syntaxEditingStyle = SyntaxConstants.SYNTAX_STYLE_KOTLIN
        textArea?.isCodeFoldingEnabled = true
        textArea?.antiAliasingEnabled = true

        sp = RTextScrollPane(textArea)
        onDispose {
            textArea = null
            sp = null
        }
    }

    LaunchedEffect(code.value) {
        textArea?.text = code.value
    }

    Box(modifier = modifier) {
        SwingPanel(
            background = Color.White,
            factory = {
                JPanel().apply {
                    layout = BoxLayout(this, BoxLayout.Y_AXIS)
                    add(sp)
                }
            }
        )
    }
}
