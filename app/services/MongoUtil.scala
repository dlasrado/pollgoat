package com.equinix.common.scala.util

// Reactive Mongo imports
import reactivemongo.api._
import reactivemongo.api.collections.default._;
import reactivemongo.bson._


import scala.concurrent.ExecutionContext.Implicits.global
import scala.util._
import scala.concurrent._
import play.api.libs.iteratee.Iteratee
import play.api.libs.json._

// Reactive Mongo plugin, including the JSON-specialized collection
import play.modules.reactivemongo.MongoController
import play.modules.reactivemongo.json.collection.JSONCollection


object MongoUtil {

    def save(inObj : String) : Boolean = {

		val collection = connect();

		val document = Json.parse(inObj)


		val future = collection.insert(document).onComplete {
		  case Failure(e) => throw e
		  case Success(lastError) => {
		    println("successfully inserted document with lastError = " + lastError)
		  }
		}

		true
	}

	def connect() : JSONCollection = {

	  // gets an instance of the driver
	  // (creates an actor system)
	  val driver = new MongoDriver
	  val connection = driver.connection(List("localhost"))

	  println("scala connect (connection)- " + connection)
	  // Gets a reference to the database "plugin"
	  val db = connection("plugin")
	  println("scala connect (db) - " + db)

	  // Gets a reference to the collection "acoll"
	  // By default, you get a BSONCollection.
	  val collection = db.collection[JSONCollection]("acoll")
	  println("scala connect (collection) - " + collection)

	  collection
	}


	def query(queryJson : String) : Future[JsArray] = {

	  val collection = connect();

      // let's do our query
      val cursor: Cursor[JsObject] = collection.
      // find all people with name `name`
      find(Json.parse(queryJson)).
      // perform the query and get a cursor of JsObject
      cursor[JsObject]

      // gather all the JsObjects in a list
      val futurePersonsList: Future[List[JsObject]] = cursor.collect[List]()

      // transform the list into a JsArray
      val futurePersonsJsonArray: Future[JsArray] = futurePersonsList.map { products =>
      																			Json.arr(products)
      																	  }
      futurePersonsJsonArray      														
	}



}