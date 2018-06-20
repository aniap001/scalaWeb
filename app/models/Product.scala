package models

import play.api.Play
import play.api.data.Form
import play.api.db.slick.DatabaseConfigProvider
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import slick.driver.JdbcProfile
import slick.driver.MySQLDriver.api._
import play.api.data.Forms._

case class Product(id: Long, name: String, description: String, price: Int)

case class ProductForm(name: String, description: String, price: Int)

case class ProductUpdateForm(id: Long, name: String, description: String, price: Int)

case class ProductSearchForm(name: String, description: String)

class ProductTableDef(tag: Tag) extends Table[Product](tag, "products") {

  def id = column[Long]("id", O.PrimaryKey,O.AutoInc)
  def name = column[String]("name")
  def description = column[String]("description")
  def price = column[Int]("price")

  override def * =
    (id, name, description, price) <>(Product.tupled, Product.unapply)
}

object ProductForm {
  val productForm = Form[ProductForm](
    mapping(
      "name" -> nonEmptyText,
      "description" -> nonEmptyText,
      "price" -> number
    )(ProductForm.apply)(ProductForm.unapply)
  )
}

object ProductUpdateForm {
  val productUpdateForm = Form[ProductUpdateForm](
    mapping(
      "id" -> longNumber,
      "name" -> nonEmptyText,
      "description" -> nonEmptyText,
      "price" -> number
    )(ProductUpdateForm.apply)(ProductUpdateForm.unapply)
  )
}

object ProductSearchForm {
  val productSearchForm = Form[ProductSearchForm](
    mapping(
      "name" -> text,
      "description" -> text
    )(ProductSearchForm.apply)(ProductSearchForm.unapply)
  )
}

object Products {
  val dbConfig = DatabaseConfigProvider.get[JdbcProfile](Play.current)
  val products = TableQuery[ProductTableDef]

  def listAll: Future[Seq[Product]] = {
    dbConfig.db.run(products.result)
  }

  def search(name: String, description: String): Future[Seq[Product]] = {
    dbConfig.db.run(products.filter(product => (product.name like '%' + name + '%') && (product.description like '%' + description + '%')).result)
  }

  def add(product: Product): Future[String] = {
    dbConfig.db.run(products += product).map(res => "Product successfully added").recover {
      case ex: Exception => ex.getCause.getMessage
    }
  }

  def get(id: Long): Future[Option[Product]] = {
    dbConfig.db.run(products.filter(_.id === id).result.headOption)
  }

  def update(product: Product): Future[String] = {
    dbConfig.db.run(products.withFilter(_.id === product.id).update(product)).map(res => "Product successfully updated").recover {
      case ex: Exception => ex.getCause.getMessage
    }
  }

  def delete(id: Long): Future[String] = {
    dbConfig.db.run(products.withFilter(_.id === id).delete.map(res => "Product successfully deleted"));
  }
}