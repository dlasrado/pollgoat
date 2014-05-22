package models

case class User( age: Int,
                 firstName: String,
                 lastName: String,
                 email: String,
                 password: String,
                 active: Boolean)

object JsonFormats {
  import play.api.libs.json.Json

  // Generates Writes and Reads for Feed and User thanks to Json Macros
  implicit val userFormat = Json.format[User]
}