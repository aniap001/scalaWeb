package controllers

import models._
import play.api.data.Form
import play.api.mvc._
import service.ProductService
import javax.inject.Inject
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.i18n.Messages.Implicits._

import scala.concurrent.{Await, Future}
import scala.concurrent.ExecutionContext.Implicits.global
import play.api.libs.json.{Json, Writes}


class ProductsController @Inject() (val messagesApi: MessagesApi) extends Controller with I18nSupport {

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

  def saveProduct() = Action.async { implicit request =>
    val formData: ProductForm = ProductForm.productForm.bindFromRequest.get
    ProductService.addProduct(new Product(-1, formData.name, formData.description, formData.price)).map( message =>
      Redirect(routes.ProductsController.listAll())
    )
  }
}

