package services

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.Failure
import scala.util.Success

import play.api.libs.json.JsArray
import play.api.libs.json.JsObject
import play.api.libs.json.Json
import play.api.libs.json.Json.toJsFieldJsValueWrapper
import play.modules.reactivemongo.json.collection.JSONCollection
import reactivemongo.api.Cursor
import reactivemongo.api.MongoDriver


object MongoUtil {

  // gets an instance of the driver
	// (creates an actor system)
	val driver = new MongoDriver
	def save(collection : JSONCollection,inObj : String) : scala.concurrent.Future[reactivemongo.core.commands.LastError] = {

		val document = Json.parse(inObj)

		collection.insert(document)
	}

	def connect(host: String) : reactivemongo.api.MongoConnection = {
	  driver.connection(List(host))
	}

	def connect(connection: reactivemongo.api.MongoConnection,db : String, collectionName: String) : JSONCollection = {

	  println("scala connect (connection)- " + connection)
	  // Gets a reference to the database "plugin"
	  val database = connection(db)
	  println("scala connect (db) - " + database)

	  // Gets a reference to the collection "acoll"
	  // By default, you get a BSONCollection.
	  val collection = database.collection[JSONCollection](collectionName)
	  println("scala connect (collection) - " + collection)


	  collection
	}


	def query(collection : JSONCollection,queryJson : String) : Future[JsArray] = {

      // let's do our query
      val cursor: Cursor[JsObject] = collection.
      // find all documents with a criteria
      find(Json.parse(queryJson)).
      // perform the query and get a cursor of JsObject
      cursor[JsObject]

      // gather all the JsObjects in a list
      val futureList: Future[List[JsObject]] = cursor.collect[List]()

      // transform the list into a JsArray
      val futureJsonArray: Future[JsArray] = futureList.map { resultList =>
      																			JsArray(resultList)
      																	  }
      futureJsonArray      														
	}

	def delete(collection : JSONCollection, queryJson : String) :  scala.concurrent.Future[reactivemongo.core.commands.LastError] = {

	  val criteriaDocument = Json.parse(queryJson)

	  collection.remove(criteriaDocument)

	}

	def update(collection : JSONCollection, queryJson : String, newDocStr : String) : scala.concurrent.Future[reactivemongo.core.commands.LastError]  = {

	  val criteriaDocument = Json.parse(queryJson)
	  val newDocument = Json.parse(newDocStr)
	  val retLastError = reactivemongo.core.commands.GetLastError()

	  collection.update(criteriaDocument,newDocument,retLastError,false,true)


	}

}