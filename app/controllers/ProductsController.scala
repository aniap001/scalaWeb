package controllers

import models.{Product, ProductForm, ProductFormData, ProductUpdateForm}
import play.api.data.Form
import play.api.mvc.{Action, Controller}
import service.ProductService

import scala.concurrent.{Await, Future}

import scala.concurrent.ExecutionContext.Implicits.global
import play.api.libs.json.{Json, Writes}


class ProductsController extends Controller {


  def index = Action.async { implicit request =>

    ProductService.listAllProducts map { productsDB =>
      Ok(views.html.products(productsDB))
    }
    }


}

