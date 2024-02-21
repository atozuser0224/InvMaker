import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.snapshots.SnapshotStateMap

class CodeMaker(val itemList : SnapshotStateMap<Int,String>,val num : MutableIntState,val frameName : MutableState<String>) {
    fun make(): String {
        val sb = StringBuilder("frame(${num.value}, Component.text(\"${frameName.value}\")){\n")
        itemList.forEach { t, u ->
            sb.append("    item(${t%9},${t/9},ItemStack(Material.${u.uppercase()}))\n")
        }
        sb.append("}")
        return sb.toString()
    }
}