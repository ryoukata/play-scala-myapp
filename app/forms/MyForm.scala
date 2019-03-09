package forms

object MyForm {
  import play.api.data._
  import play.api.data.Forms._

  case class Data(name: String, pass: String)

  val myform = Form(
    mapping(
      "name" -> text,
      "pass" -> text
    )(Data.apply)(Data.unapply)
  )
}