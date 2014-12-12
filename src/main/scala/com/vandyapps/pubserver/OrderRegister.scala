package com.vandyapps.pubserver

trait OrderRegister {
  
  private var orders = Nuclear(List.empty[PubOrder])

  def addOrder(num: Int): Unit = {
    val newOrder = PubOrder(
      orderNumber = num,
      timeCreated = System.currentTimeMillis)
    orders.update( newOrder :: _ )
  }

  def getAllOrders: Seq[PubOrder] = {
    removeStaleOrder()
    orders.get
  }

  def getOrders(count: Int): Seq[PubOrder] = {
    removeStaleOrder()
    orders.get.take(count)
  }

  def orderCount = orders.get.length

  def removeStaleOrder() {
    orders.update ( _.filter { o =>
      (System.currentTimeMillis - o.timeCreated) < FIVE_MINUTES
    } )
  }

  private val FIVE_MINUTES = 5 * 60 * 1000

}

case class PubOrder(
    orderNumber: Int,
    timeCreated: Long)

case class PubReport(
    orders: Seq[PubOrder] = List.empty,
    status: String        = "")