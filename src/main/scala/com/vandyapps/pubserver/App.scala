package com.vandyapps.pubserver

import util.Try
import com.twitter.finatra._

object App extends FinatraServer {

  println("================================")
  println("=          PUB SERVER          =")
  println("================================")

  class MainController extends Controller with OrderRegister {
  
    get("/") { request =>
      render.plain(API_DOC).toFuture
    }
    
    get("/order") { request =>
      (for (_count <- request.params.get("count");
            apikey <- request.params.get("apikey");
            count  <- Try(_count.toInt).toOption;
            if isValid(apikey))
      yield PubReport(status = "Okay", 
                      orders = getOrders(count)))
          .getOrElse(PubReport(status = "Malformed request"))
          .>>>(render.json(_).toFuture)
    }
    
    post("/order") { request =>
      (for (_order <- request.params.get("orderNumber");
            apikey <- request.params.get("apikey");
            orderNum <- Try(_order.toInt).toOption;
            if isValid(apikey))
      yield orderNum)
          .map { num =>
              addOrder(num)
              PubReport(status = "Successfully added order #" + num) }
          .getOrElse(PubReport(status = "Malformed request"))
          .>>>(render.json(_).toFuture)
    }
  
    notFound { request =>
      render.json(PubReport(status = "Wrong API call")).toFuture
    }

    error { request =>
      render.json(PubReport(status = "Something's wrong")).toFuture
    }

  }
  
  register(new MainController)
  
  val API_DOC =
      """{
        |  "paths" : [
        |    { "path" : "/", "method" : "GET",
        |        "input" : [],
        |        "description" : "Return server API",
        |        "output" : "Object" },
        |    { "path" : "/order", "method" : "GET", 
        |        "input" : ["count", "apikey"],
        |        "description" : "",
        |        "output" : "PubReport" },
        |    { "path" : "/order", "method" : "POST", 
        |        "input" : ["orderNumber", "apikey"],
        |        "description" : "",
        |        "output" : "String" }
        |  ],
        |  "structs" : [
        |    { "type" : "PubOrder",
        |      "struct" : {
        |        "orderNumber" : "Number",
        |        "timeCreated" : "Number" }},
        |    { "type" : "PubReport",
        |      "struct" : {
        |        "orders" : "[PubOrder]",
        |        "status" : "String" }}
        |  ]
        |}
      """.stripMargin
      
  implicit class Piped[T](pipee: T) {
    def >>>[U](receiver: T=>U): U = receiver(pipee)
  }
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
  
  def getAllOrders: Seq[PubOrder] = orders
  
  def getOrders(count: Int): Seq[PubOrder] = orders.take(count)
  
  def orderCount = orders.length
  
  def isValid(key: String) = true
  
}
