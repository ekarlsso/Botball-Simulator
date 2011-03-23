import sbt._

class BotballSimulatorProject(info: ProjectInfo) extends DefaultWebProject(info) with IdeaProject with AkkaProject {

  //REST part dependencies
  val scalatraVersion = "2.0.0.M3"
  val scalatra = "org.scalatra" %% "scalatra" % scalatraVersion
  val scalate = "org.scalatra" %% "scalatra-scalate" % scalatraVersion
  val servletApi = "org.mortbay.jetty" % "servlet-api" % "2.5-20081211" % "provided"

  val akkaTypedActor = akkaModule("typed-actor")

  val slf4jBinding = "ch.qos.logback" % "logback-classic" % "0.9.25" % "runtime"

  override def testClasspath = super.testClasspath +++ buildCompilerJar

  val sonatypeNexusSnapshots = "Sonatype Nexus Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"

  //own framework dependencies
  val ondexRepo = "ondex" at "http://ondex.rothamsted.bbsrc.ac.uk/nexus/content/groups/public"
  val Jcommon = "jfree" % "jcommon" % "1.0.16"

  val scalala_repo = "Scalala Maven Repo" at "http://repo.scalanlp.org/repo"
  val scalala = "org.scalanlp" % "scalala_2.8.1.RC3" % "1.0.0.RC1-SNAPSHOT"

  //Testing
  val junit = "junit" % "junit" % "4.5" % "test->default"
  val specs = "org.scala-tools.testing" %% "specs" % "1.6.6" % "test->default"
  val mockito = "org.mockito" % "mockito-all" % "1.8.5" % "test->default"
}
