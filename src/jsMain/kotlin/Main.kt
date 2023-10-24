import kotlinx.coroutines.MainScope
import react.FC
import react.Props
import react.create
import react.dom.client.createRoot
import web.dom.document

val scope = MainScope()

fun main() {
    val container = document
        .getElementById("root") ?: error("Couldn't find container!")
    val root = createRoot(container)
    root.render(FC<Props>{
        Card{}
    }.create())
}
