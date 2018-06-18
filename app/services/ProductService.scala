package service

import models.{Product, Products}

import scala.concurrent.Future

object ProductService {

  def listAllProducts: Future[Seq[Product]] = {
    Products.listAll
  }
}
