package controllers

import java.sql._
import javax.inject._
import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import play.api.db._
import anorm._

@Singleton
class HomeController @Inject()(db: Database, cc: MessagesControllerComponents) extends MessagesAbstractController(cc) {

  import forms.PersonForm._

  def index() = Action { implicit request =>
    db.withConnection { implicit conn =>
      val result: List[PersonData] =
        SQL("Select * from people").as(personparser.*)
      Ok(views.html.index(
        "People Data.", result
      ))
    }
  }

  // ユーザ登録画面の表示
  def add() = Action { implicit request =>
    Ok(views.html.add(
      "フォームを入力してください。",
      form
    ))
  }

  def create() = Action { implicit request =>
    val formdata = form.bindFromRequest
    val data = formdata.get
    db.withConnection { implicit conn =>
      SQL("insert into people values (default, {name}, {mail}, {tel})")
        .on(
          "name" -> data.name,
          "mail" -> data.mail,
          "tel" -> data.tel
        ).executeInsert()
      Redirect(routes.HomeController.index)
    }
  }

  def edit(id: Int) = Action { implicit request =>
    var formdata = personform.bindFromRequest
    db.withConnection { implicit conn =>
      val pdata: PersonData =
      SQL("select * from people where id = {id}")
        .on("id" -> id)
        .as(personparser.single)
      formdata = personform.fill(pdata)
      Ok(views.html.edit(
        "フォームを編集してください。",
        formdata, id
      ))
    }
  }

  def update(id: Int) = Action { implicit request =>
    val formdata = personform.bindFromRequest
    val data = formdata.get
    db.withConnection { implicit conn =>
      val result =
        SQL("update people set name = {name}, mail = {mail}, tel = {tel} where id = {id}")
        .on(
          "name" -> data.name,
          "mail" -> data.mail,
          "tel" -> data.tel,
          "id" -> data.id
        ).executeUpdate
      Redirect(routes.HomeController.index)
    }
  }

  def delete(id: Int) = Action { implicit request =>
    db.withConnection { implicit conn =>
      val data: PersonData =
        SQL("select * from people where id = {id}")
          .on("id" -> id)
          .as(personparser.single)
      Ok(views.html.delete(
        "このレコードを削除します。",
        data, id
      ))
    }
  }

  def remove(id: Int) = Action { implicit request =>
    db.withConnection { implicit conn =>
      val result =
        SQL("delete from people where id = {id}")
          .on("id" -> id)
          .executeUpdate
      Redirect(routes.HomeController.index)
    }
  }

  def show(id: Int) = Action { implicit request =>
    db.withConnection { implicit conn =>
      val result: PersonData =
        SQL("Select * from people where id = {id}").on("id" -> id).as(personparser.single)
      Ok(views.html.show(
        "People Data.", result
      ))
    }
  }

}
