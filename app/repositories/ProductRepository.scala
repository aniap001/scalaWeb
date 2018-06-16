package repositories

import javax.inject.Inject
import models.Product
import play.db.Database

/**
  * Created by mstobieniecka on 2018-06-16.
  */
class ProductRepository @Inject() (db: Database) {
  def getAll(limit: Int, offset: Int): List[Product] = db.withConnection { implicit conn =>
    SQL"""SELECT * FROM "PRODUCT" LIMIT $limit OFFSET $offset""".as(Product.simple.*)
  }

  def getById(id: Long): Option[Product] = db.withConnection { implicit conn =>
    SQL"""SELECT * FROM "PRODUCT" WHERE id=$id""".as(Product.simple.singleOpt)
  }

  def save(name: String, description: String, price: Double): Option[Long] = db.withConnection { implicit conn =>
    SQL"""INSERT INTO "PRODUCT" ("name", "description", "price") VALUES ($name, $description, $price)""".executeInsert()
  }

  def updateById(id: Long, name: String, description: String, price: Double): Int = db.withConnection { implicit conn =>
    SQL"""UPDATE "PRODUCT" SET "name"=$name, "description"=$description, "price"=$price WHERE "id"=$id""".executeUpdate
  }

  def deleteById(id: Long): Int = db.withConnection { implicit conn =>
    SQL"""DELETE "PRODUCT" WHERE "id"=$id""".executeUpdate()
  }
}
