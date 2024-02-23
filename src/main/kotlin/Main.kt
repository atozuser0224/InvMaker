import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.SwingPanel
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.*
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea
import org.fife.ui.rsyntaxtextarea.SyntaxConstants
import org.fife.ui.rtextarea.RTextScrollPane
import org.kohsuke.github.GHRelease
import java.awt.Desktop
import java.net.URI
import java.nio.file.Paths
import javax.swing.BoxLayout
import javax.swing.JPanel


@Composable
@Preview
fun App() {
    val version = "1.1.1"
    val accessToken = "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"
    val ownerName = "atozuser0224"
    val repoName = "InvMaker"
    var num = remember { mutableIntStateOf(1) }
    val itemList = list
    val frame_name = remember { mutableStateOf("이곳을 눌러 수정") }
    val inv_itemList = remember {
        mutableStateMapOf<Int,String>()
    }
    val inv_item_name_List = remember {
        mutableStateMapOf<Int,String>()
    }
    val inv_item_lore_List = remember {
        mutableStateMapOf<Int,MutableList<String>>()
    }
    val inv_item_selected_List = remember {
        mutableStateMapOf<Int,Boolean>()
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
    // GitHub 인증
    var releasename = remember {
        mutableStateOf("")
    }
    val dialogState = remember { mutableStateOf(false) }
    LaunchedEffect(true) {
        val github = org.kohsuke.github.GitHubBuilder().withOAuthToken(accessToken).build()

        // 리포지토리 정보 가져오기
        val repo: org.kohsuke.github.GHRepository = github.getRepository("$ownerName/$repoName")
        println("sadfasdfasdfasdf")

        // 최신 릴리즈 가져오기
        val release: GHRelease? = repo.listReleases().toList()[0]
        releasename.value = release?.name?:""
        if (release?.name != version){
            dialogState.value = true
        }
    }
    val desktop = if (Desktop.isDesktopSupported()) Desktop.getDesktop() else null
    if (dialogState.value) {
        AlertDialog(onDismissRequest = { dialogState.value = false},
            title = {
                Text(text = "새로운 업데이트!")
            },
            text = {Text("업데이트가 등장했습니다. 현재 버전 ${version} -> 신규 버전 ${releasename.value}")},
            confirmButton = {
                Button(
                    onClick = { desktop?.browse(URI("https://github.com/atozuser0224/InvMaker/releases/tag/${releasename.value}")) },
                ) {
                    Text("확인", color = Color.White)
                }
            },
            dismissButton = {
                Button(
                    onClick = { dialogState.value = false },
                ) {
                    Text("다음에 하기", color = Color.Red)
                }
            })
    }
    val codeMaker = CodeMaker(inv_itemList,num,frame_name,inv_item_name_List,inv_item_lore_List,inv_item_selected_List)
    LaunchedEffect(inv_itemList.toList(),num.value,frame_name.value,inv_item_lore_List.toList(),inv_item_name_List.toList(),inv_item_selected_List.toList()){
        code.value = codeMaker.make()
    }
    MaterialTheme {
        Box(Modifier.fillMaxSize()) {
            invView(num, inv_itemList, x, y,frame_name)
            itemView(itemList, x, y, inv_itemList,num)
            FuncView(num , inv_item_name_List, inv_item_lore_List,x,y,inv_itemList,inv_item_selected_List)
            Box(Modifier.align(Alignment.TopEnd)){
                CodeEditor(code,Modifier.fillMaxWidth(0.33f).fillMaxHeight())
            }
        }
    }
}

fun main() = application {
    Window(onCloseRequest = ::exitApplication, icon = painterResource("item/chest.png") ,state = WindowState(width = 1920.dp, height = 1080.dp, position = WindowPosition(0.dp,0.dp), placement = WindowPlacement.Maximized), title = "InvManager") {
        App()
    }
}
fun listImageFilesInItemDirectory(): List<String> {
    val projectDirAbsolutePath = Paths.get("").toAbsolutePath().toString()
    val itemDir = Paths.get(projectDirAbsolutePath, "/src/main/resources/item").toFile()
    println(itemDir)
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
    val sb = StringBuilder("{")
    imageFileList.filter {
        !it.contains("top")&&!it.contains("bottom")&&!it.contains("side")
    }.forEach {
        sb.append("\"$it\",")
    }
    sb.append("}")
    println(sb.toString())
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
