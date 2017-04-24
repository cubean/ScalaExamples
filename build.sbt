name := "ScalaExamples"

version := "1.0"

scalaVersion := "2.12.1"


scalaSource in Compile := baseDirectory.value / "src" / "main" / "scala"

scalaSource in Test := baseDirectory.value / "src" / "test" / "scala"

resourceDirectory in Compile := baseDirectory.value / "src" / "main" / "resources"

resourceDirectory in Test := baseDirectory.value / "src" / "test" / "resources"

enablePlugins(JavaAppPackaging)

libraryDependencies ++= Seq(
  "org.scalatest" % "scalatest_2.12" % "3.0.1",
  "org.scala-lang" % "scala-library" % "2.12.1",
  "org.apache.logging.log4j" % "log4j-core" % "2.8.1",
  "org.apache.logging.log4j" % "log4j-api" % "2.8.1",

  "org.aspectj" % "aspectjtools" % "1.8.10"
)

import com.typesafe.sbt.SbtAspectj._

aspectjSettings

/* project settings */

fork in run := true

javaOptions in run ++= (AspectjKeys.weaverOptions in Aspectj).value

AspectjKeys.inputs in Aspectj += compiledClasses.value

products in Compile := (products in Aspectj).value

products in Runtime := (products in Compile).value