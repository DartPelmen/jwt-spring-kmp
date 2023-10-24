package propses

import models.BookModel
import react.Props


external interface BookModelProp : Props {
    var entity: BookModel
}