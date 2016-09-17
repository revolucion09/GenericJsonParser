package saif.libs

import spray.json._
import scala.reflect.runtime.universe._

/**
  * Created by saif on 9/16/16.
  */
trait GenericJsonProtocol extends DefaultJsonProtocol {

  implicit object GenericJsonProtocol extends RootJsonFormat[JsonSerializable] {

    @unchecked
    private def typeMatch(content: Any): JsValue = {
      content match {
        case int: Int => JsNumber(int)
        case double: Double => JsNumber(double)
        case long: Long => JsNumber(long)
        case string: String => JsString(string)
        case bool: Boolean => JsBoolean(bool)
        case array: Seq[Any] => JsArray(array.map(f => typeMatch(f)).toVector)
        case map: Map[Any, Any] => JsObject(map.map { case (key, value) => (key.toString, typeMatch(value)) })
        case somethingElse: JsonSerializable => somethingElse.toJson
        case _ => throw new IllegalArgumentException("Unexpected type could not be serialized into JSON")
      }
    }

    def write(something: JsonSerializable): JsValue = {
      val classMirror = runtimeMirror(something.getClass.getClassLoader)
      val instanceMirror = classMirror.reflect(something)
      val declaredFields = instanceMirror.symbol.toType.members.filter(!_.isMethod)
      val declaredSymbols = declaredFields.map(_.asTerm)
      val fieldMirrors = declaredSymbols.map(instanceMirror.reflectField)
      val jsContent = fieldMirrors.zip(declaredFields.map(_.name.toString.trim)).map {
        case (content, fieldName) => {
          fieldName -> typeMatch(content.get)
        }
      }.toMap
      JsObject(jsContent)
    }

    def read(value: JsValue) = {
      null
    }

  }
}
