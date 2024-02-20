import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun numTextField(num : MutableIntState,modifier: Modifier = Modifier){
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
        modifier = modifier,
        shape = RoundedCornerShape(60.dp),
        textStyle = TextStyle(
            fontWeight = FontWeight.Bold, fontSize = 20.sp,
            textAlign = TextAlign.Center
        )
    )
}
@Composable
fun showInv(
    num: Int,
    list: SnapshotStateMap<Int, String>,
    ax: MutableIntState,
    ay: MutableIntState,
    frameName: MutableState<String>
){
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
        Box {
            Image(
                painter = painterResource("topbar.png"),"topbar"
            )
            BasicTextField(value = frameName.value, onValueChange = {
                frameName.value = it
            }, modifier =  Modifier.fillMaxWidth(0.5f).offset(x =20.dp,y= -(10).dp).align(Alignment.BottomStart), textStyle = TextStyle(
                fontSize = 24.sp, fontFamily = fontFamily, textAlign = TextAlign.Start
            ), singleLine = true)
        }
        var n = 0
        repeat(num){y->
            Row(){
                Image(
                    painter = painterResource("part_start.png"),"img",
                    modifier = Modifier.scale(1.2f,1.0f)
                )
                repeat(9){x->
                    Box(){
                        if (ax.value == x && ay.value == y){
                            Image(
                                painter = painterResource("part_middle.png"),"img",
                                modifier = Modifier.clickable {
                                    ax.value = x
                                    ay.value = y
                                },
                                colorFilter = ColorFilter.tint(Color(0x66000000))
                            )
                        }else{
                            Image(
                                painter = painterResource("part_middle.png"),"img",
                                modifier = Modifier.clickable {
                                    ax.value = x
                                    ay.value = y
                                }
                            )
                        }
                        list[n++]?.let {
                            Image(
                                painter = painterResource("item/${it}.png"),"img",
                                modifier = Modifier.scale(2f).offset(x=10.dp,y=10.dp),
                                contentScale = ContentScale.Fit
                            )
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
@Composable
fun BoxScope.invView(num: MutableIntState,list: SnapshotStateMap<Int,String>,x:MutableIntState,y:MutableIntState,frameName : MutableState<String>) {
    Surface(modifier = Modifier.align(Alignment.TopStart),color = MaterialTheme.colors.surface, elevation = 5.dp, shape = RoundedCornerShape(0.dp,0.dp,30.dp,30.dp)) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
            .fillMaxHeight(0.44f)
            .fillMaxWidth(0.33f)
            .align(Alignment.TopStart)) {
            showInv(num.value, list,x,y,frameName)
        }
    }

}