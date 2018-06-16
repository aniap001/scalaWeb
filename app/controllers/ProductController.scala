package controllers

import akka.http.scaladsl.model.HttpHeader.ParsingResult.Ok
import javax.inject.Inject
import play.mvc.Controller
import play.api.mvc._
import play.libs.Json
import repositories.ProductRepository
import play.api.mvc._


/**
  * Created by mstobieniecka on 2018-06-16.
  */
@Singleton
class ProductController @Inject() (productRepository: ProductRepository) extends Controller {
}
