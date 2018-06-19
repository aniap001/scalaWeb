package service

import models.{Product, Products}

import scala.concurrent.Future

object ProductService {

  def listAllProducts: Future[Seq[Product]] = {
    Products.listAll
  }

  def addProduct(product: Product): Future[String] = {
    Products.add(product)
  }

  def getProduct(id: Long): Future[Option[Product]] = {
    Products.get(id)
  }

  def updateProduct(product: Product): Future[String] = {
    Products.update(product)
  }
}
