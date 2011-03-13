import sbt._

class BotballSimulatorProject(info: ProjectInfo) extends DefaultWebProject(info) with IdeaProject {
	val akka_repo = "Akka Maven Repository" at "http://scalablesolutions.se/akka/repository"
	val akka = "se.scalablesolutions.akka" % "akka-actor_2.8.0" % "1.0-M1"

	val junit = "junit" % "junit" % "4.5" % "test->default"
  val specs = "org.scala-tools.testing" %% "specs" % "1.6.6" % "test->default"
}
