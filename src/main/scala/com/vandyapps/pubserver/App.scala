package com.vandyapps.pubserver

import com.twitter.finatra._

object App extends FinatraServer {

  println("================================")
  println("=          PUB SERVER          =")
  println("================================")

  class MainController extends Controller {
  
    get("/") { request =>
      render.plain("The pub is up").toFuture
    }
    
    get("/order") { request =>
      render.plain("return n latest orders").toFuture
    }
    
    post("/order") { request =>
      render.plain("register a new order").toFuture
    }
  
    notFound { request =>
      render.status(404).plain("404: nope, can't find it.").toFuture
    }

    error { request =>
      render.status(500).plain("500: something's wrong").toFuture
    }

  }
  
  register(new MainController)
}

