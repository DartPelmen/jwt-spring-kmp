import kotlinx.coroutines.launch
import models.BookModel
import network.getBooks
import propses.BookModelProp
import react.FC
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.h2
import react.dom.html.ReactHTML.h3
import react.dom.html.ReactHTML.p
import react.useEffect
import react.useState

val Card = FC<BookModelProp>{

    var bookList by useState<List<BookModel>>(listOf())

    useEffect(null) {
        scope.launch {
            val books = getBooks()
            bookList = books
        }
    }

    return@FC div{
        for(b in bookList){
            h2{
                +b.title

            }
            h3{
                +b.annotation
            }
            p{
                +"----------"
            }
        }
    }

}