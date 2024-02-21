import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key.Companion.R
import androidx.compose.ui.res.useResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.platform.Font
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import java.nio.file.Paths

@Composable
fun BoxScope.FuncView(num : MutableIntState) {
    Surface(modifier = Modifier.align(Alignment.TopCenter),color = MaterialTheme.colors.surface, elevation = 8.dp, shape = RoundedCornerShape(0.dp,0.dp,30.dp,30.dp)) {
        Column(horizontalAlignment = Alignment.Start, modifier = Modifier
            .fillMaxHeight(1f)
            .fillMaxWidth(0.33f)) {
            Row {
                Text("세로줄 조정 : ",Modifier.weight(4f).height(50.dp).offset(y=5.dp), style = MaterialTheme.typography.body1, fontWeight = FontWeight.Bold, fontFamily = fontFamily, fontSize = 36.sp)
                numTextField(num,Modifier.weight(1f))
            }
        }
    }
}
val fontFamily = FontFamily(
    Font(
        resource = "font/minecraft.ttf",
    )
)
