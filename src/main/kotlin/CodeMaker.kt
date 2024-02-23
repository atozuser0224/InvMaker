import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.snapshots.SnapshotStateMap

class CodeMaker(
    val itemList: SnapshotStateMap<Int, String>,
    val num: MutableIntState,
    val frameName: MutableState<String>,
    val inv_item_name_List: SnapshotStateMap<Int, String>,
    val inv_item_lore_List: SnapshotStateMap<Int, MutableList<String>>,
    val inv_item_selected_List: SnapshotStateMap<Int, Boolean>
) {
    fun make(): String {
        val sb = StringBuilder("frame(${num.value}, Component.text(\"${frameName.value}\")){\n")
        itemList.forEach { t, u ->
            sb.append("    item(${t%9},${t/9},ItemStack(Material.${u.uppercase()}))")
            if (inv_item_name_List[t] != null){
                sb.append(".apply{\n")
                sb.append("    editMeta { meta->\n")
                sb.append("        meta.setDisplayName(\"${inv_item_name_List[t]}\")\n")
                if (inv_item_lore_List[t] != null){
                    sb.append("        meta.lore = listOf(")
                    inv_item_lore_List[t]!!.forEach{ loreList ->
                        sb.append("\"${loreList}\",")
                    }
                    sb.append(")\n        }\n" +
                            "    }\n")
                }
            }else{
                sb.append("\n")
            }
        }
        sb.append("}")
        return sb.toString()
    }
}