package models

import play.api.Play
import play.api.data.Form
import play.api.data.Forms._
import play.api.db.slick.DatabaseConfigProvider
import play.api.libs.json.Json

import scala.concurrent.Future
import slick.driver.JdbcProfile
import slick.driver.MySQLDriver.api._

import scala.collection.mutable.ListBuffer
import scala.concurrent.ExecutionContext.Implicits.global

case class Product(id: Long, name: String, description: String, price: Int)

case class ProductFormData(name: String, description: String, price: Int)

case class ProductUpdateFormData(id: Long, name: String, description: String, price: Int)

case class ProductJson(id: String, quantity: String)

object ProductJson {
  implicit val productJsonFormat = Json.format[ProductJson]
}

case class ProductsJson(products: List[ProductJson])

object ProductsJson {
  implicit val productsJsonFormat = Json.format[ProductsJson]
}

object ProductForm {

  val form = Form(
    mapping(
      "name" -> nonEmptyText,
      "description" -> nonEmptyText,
      "price" -> number
    )(ProductFormData.apply)(ProductFormData.unapply)
  )
}

object ProductUpdateForm {

  val form = Form(
    mapping(
      "id" -> longNumber,
      "name" -> nonEmptyText,
      "description" -> nonEmptyText,
      "price" -> number
    )(ProductUpdateFormData.apply)(ProductUpdateFormData.unapply)
  )
}

class ProductTableDef(tag: Tag) extends Table[Product](tag, "products") {

  def id = column[Long]("id", O.PrimaryKey,O.AutoInc)
  def name = column[String]("name")
  def description = column[String]("description")
  def price = column[Int]("price")

  override def * =
    (id, name, description, price) <>(Product.tupled, Product.unapply)
}


object Products {

  val dbConfig = DatabaseConfigProvider.get[JdbcProfile](Play.current)

  val products = TableQuery[ProductTableDef]

  def add(product: Product): Future[String] = {
    dbConfig.db.run(products += product).map(res => "Product successfully added").recover {
      case ex: Exception => ex.getCause.getMessage
    }
  }

  def edit(product: Product): Future[String] = {
    dbConfig.db.run(products.withFilter(_.id === product.id).update(product)).map(res => "Product successfully updated").recover {
      case ex: Exception => ex.getCause.getMessage
    }
  }

  def get(id: Long): Future[Option[Product]] = {
    dbConfig.db.run(products.filter(_.id === id).result.headOption)
  }

  def listAll: Future[Seq[Product]] = {
    dbConfig.db.run(products.result)
  }


}
