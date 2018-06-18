package controllers

import com.ning.http.client.Request
import models._
import play.api.data.Form
import play.api.mvc.{Action, AnyContent, Controller}
import service.ProductService
import javax.inject._

import play.api.i18n.MessagesApi

import scala.concurrent.{Await, Future}
import scala.concurrent.ExecutionContext.Implicits.global
import play.api.libs.json.{Json, Writes}


class ProductsController @Inject() (val messagesApi: MessagesApi) extends Controller with play.api.i18n.I18nSupport {

  def welcome = Action.async {

    Future {
      Ok(views.html.welcome())
    }
  }

  def addProduct() = Action.async {
    Future {
      Ok(views.html.newProduct(ProductForm.productForm))
    }
  }

  def listAll() = Action.async { implicit request =>

    ProductService.listAllProducts map { productsDB =>
      Ok(views.html.products(productsDB))
    }
  }

  def saveProduct() = Action { implicit request =>
    val formData: ProductForm = ProductForm.productForm.bindFromRequest.get
    Ok(formData.toString)
  }

}

