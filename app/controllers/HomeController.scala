package controllers

import java.sql._
import javax.inject._
import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import play.api.db._

@Singleton
class HomeController @Inject()(db: Database, cc: MessagesControllerComponents) extends MessagesAbstractController(cc) {

  // import forms.MyForm._

  def index() = Action { implicit request =>
    var msg = "database record:<br><ul>"
    try {
      db.withConnection { conn =>
        val stmt = conn.createStatement
        val rs = stmt.executeQuery("SELECT * FROM people")
        while(rs.next){
          msg += "<li>" + rs.getInt("id") + ":" + rs.getString("name") + "</li>"
        }
        msg += "</ul>"
      }
    } catch {
      case e: SQLException =>
        msg = "<li>no record...</li>"
    }
    Ok(views.html.index(
      msg
    ))
  }

/*  def form() = Action { implicit  request =>
    val form = myform.bindFromRequest
    val data = form.get
    Ok(views.html.index(
      "name: " + data.name + ", pass: " + data.pass  + ", radio:" + data.radio,
      form
    ))
  }*/

}
