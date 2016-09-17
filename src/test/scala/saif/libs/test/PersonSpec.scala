package saif.libs.test

import saif.libs.JsonSerializable

/**
  * Created by saif on 9/16/16.
  */
class PersonSpec extends UnitSpec {
  case class Person(
                    name: String,
                    age: Int = 20,
                    weight: Double = 42.63,
                    male: Boolean = true,
                    inventory: Map[String,Int] = Map("Chairs" -> 5,"Tables" -> 2),
                    codes: List[Double] = List(0.4,2.5,3.1)
                   ) extends JsonSerializable

  "A Person" should "be printed in JSON format" in {
    val p = Person("Peter Parker")
    println(p.prettyPrint)
    succeed
  }
}
