package com.vandyapps.pubserver

import util.Try
import com.twitter.finatra._

object App extends FinatraServer with RequestValidation {

  println("================================")
  println("=          PUB SERVER          =")
  println("================================")
  println(s"With password: ${apiKey}")

  class MainController extends Controller
      with OrderRegister {

    get("/") { request =>
      render.static("index.txt").toFuture
    }

    get("/order") { request =>
      (for {
        countStr <- request.params.get("count");
        count    <- Try(countStr.toInt).toOption
      } yield PubReport(status = "Okay",
                        orders = getOrders(count)))
          .getOrElse(PubReport(status = "Malformed request"))
          .>>>(render.json(_).toFuture)
    }

    post("/order") { request =>
      (for {
        orderStr <- request.params.get("orderNumber");
        apikey   <- request.params.get("apikey");
        orderNum <- Try(orderStr.toInt).toOption;
        if isValid(apikey, orderNum)
      } yield orderNum)
          .map { num =>
              addOrder(num)
              PubReport(status = "Successfully added order #" + num) }
          .getOrElse(PubReport(status = "Malformed request"))
          .>>>(render.json(_).toFuture)
    }

    get("/console") { request =>
      render.static("main.html").toFuture
    }

    notFound { request =>
      render.json(PubReport(status = "Wrong API call")).toFuture
    }

    error { request =>
      render.json(PubReport(status = s"Something's wrong: ${request.error}")).toFuture
    }

  }

  register(new MainController)

  implicit class Piped[T](pipee: T) {
    def >>>[U](receiver: T=>U): U = receiver(pipee)
  }

  lazy val apiKey = ""

}

case class PubOrder(
    orderNumber: Int,
    timeCreated: Long)

case class PubReport(
    orders: Seq[PubOrder] = List.empty,
    status: String        = "")

trait OrderRegister {
  private var orders = List.empty[PubOrder]

  def addOrder(num: Int): Unit =
      orders ::= PubOrder(
          orderNumber = num,
          timeCreated = System.currentTimeMillis)

  def getAllOrders: Seq[PubOrder] = {
    removeStaleOrder()
    orders
  }

  def getOrders(count: Int): Seq[PubOrder] = {
    removeStaleOrder()
    orders.take(count)
  }

  def orderCount = orders.length

  def removeStaleOrder() {
    orders = orders.filter { o =>
      (System.currentTimeMillis - o.timeCreated) < FIVE_MINUTES
    }
  }

  private val FIVE_MINUTES = 5 * 60 * 1000

}

trait RequestValidation {
  private val hasher =
    java.security.MessageDigest.getInstance("SHA-1")

  def apiKey: String

  def isValid(key: String, data: Int) = {
    val inputString: String = apiKey + data
    hasher.update(inputString.getBytes("UTF-8"))
    val hash = hasher.digest()
    key.equalsIgnoreCase(toHex(hash))
  }

  private def toHex(bytes: Array[Byte]) =
    javax.xml.bind.DatatypeConverter.printHexBinary(bytes)

}
