package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._

@Singleton
class HomeController @Inject()(cc: MessagesControllerComponents) extends MessagesAbstractController(cc) {

  import forms.MyForm._

  def index() = Action { implicit request =>
    Ok(views.html.index(
      "これはコントローラーで用意したメッセージです。",
      myform
    ))
  }

  def form() = Action { implicit  request =>
    val form = myForm.bindFromRequest
    val data = form.get
    Ok(views.html.index(
      "name: " + data.name + ", pass: " + data.pass,
      form
    ))
  }

}
