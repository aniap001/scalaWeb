package models

import play.api.db.DBApi
import anorm._
import anorm.SqlParser._
import javax.inject.Inject
case class Product(id: Option[Long] = None, name: String, description: String, price: Double)

object Product {

  // -- Parsers

  /**
    * Parse a Product from a ResultSet
    */
  val simple = {
    get[Option[Long]]("product.id") ~
      get[String]("product.name") ~
      get[String]("product.description") ~
      get[Double]("product.price") map {
      case id ~ name ~ description ~ price => Product(id, name, description, price)
    }
  }
}
