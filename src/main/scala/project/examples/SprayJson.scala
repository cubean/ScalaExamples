package project.examples

/**
  * Created by Cubean Liu on 4/5/17.
  */

import spray.json.DefaultJsonProtocol._
import spray.json._

object SprayJson extends ExampleBase {

  private def jsonValue(): Map[String, String] = {
    val x1 = """ {"key1": "value1", "key2": 4} """

    val json = x1.parseJson
    println(json.prettyPrint)

    json.convertTo[Map[String, JsValue]].map(v =>
      (v._1, v._2 match {
        case s: JsString => s.value
        case o => o.toString()
      }))
  }

  override def runAll(): Unit = {
    println(jsonValue())
  }
}
