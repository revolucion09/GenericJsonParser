package saif.libs

import spray.json._

/**
  * Created by saif on 9/16/16.
  */
object JsonSerializable extends GenericJsonProtocol {
  implicit def something2Json(j: JsonSerializable): JsValue = j.toJson(GenericJsonProtocol)
}

trait JsonSerializable