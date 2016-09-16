package saif.libs

import spray.json._

import scala.reflect._
import scala.reflect.runtime.universe._
import scala.reflect.runtime.{universe => ru}
/**
  * Created by Saif Addin Ellafi on 9/16/16.
  */
class GenericJsonContext[A:WeakTypeTag:ClassTag] extends DefaultJsonProtocol {

  implicit object GenericJsonFormat extends RootJsonFormat[A] {

    def write(something: A): JsValue = {
      val declaredFields = something.getClass.getDeclaredFields
      val declaredFieldNames = declaredFields.map(_.getName)
      val classMirror = ru.runtimeMirror(something.getClass.getClassLoader)
      val declaredSymbols = declaredFields.map(field => ru.weakTypeOf[A].decl(ru.TermName(field.getName)).asTerm)
      val instanceMirror = classMirror.reflect(something)
      val fieldMirrors = declaredSymbols.map(instanceMirror.reflectField)
      val jsContent: Map[String, JsValue] = fieldMirrors.zip(declaredFieldNames).map { case (content, name) => {
        content.get match {
          case text: String => name -> JsString(text)
          case number: Int => name -> JsNumber(number)
          case decimal: Double => name -> JsNumber(decimal)
          case long: Long => name -> JsNumber(long)
          case bool: Boolean => name -> JsBoolean(bool)
          case _ => name -> JsNull
        }
      }
      }.toMap
      JsObject(jsContent)
    }

    def read(jsContent: JsValue): A = {
      throw new Exception("Not yet")
    }
  }
}


case class Person(name: String, age: Int = 20, weight: Double = 42.63, male: Boolean = true)
case class Car(color: String)

object Execution {
  def generate {
    val c = Car("Red")
    val p = Person("Peter Parker")
    new GenericJsonContext[Person] {
      println(p.toJson.prettyPrint)
    }
    new GenericJsonContext[Car] {
      println(c.toJson.prettyPrint)
    }
  }
}