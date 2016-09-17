# reflect_json_processor

Very simple SCALA library based on *scala-reflect* and *spray-json* (works as well with akka-spray-json) to seamlessly get JSON out of your case classes

Extend any case class to be serialized into JSON with *JsonSerializable* and then you can already call into any instance spray json calls such as prettyPrint

Example:

```scala
case class Person(
                    name: String,
                    age: Int = 20,
                    weight: Double = 42.63,
                    male: Boolean = true,
                    inventory: Map[String,Int] = Map("Chairs" -> 5,"Tables" -> 2),
                    codes: List[Double] = List(0.4,2.5,3.1)
                   ) extends JsonSerializable

val person = Person("Peter")
person.prettyPrint
```
...
```
{
  "inventory": {
    "Chairs": 5,
    "Tables": 2
  },
  "weight": 42.63,
  "name": "Peter Parker",
  "age": 20,
  "codes": [0.4, 2.5, 3.1],
  "male": true
}
```
