organization  := "io.buildo"

name          := "anorm-extensions"

version       := "0.1.1"

scalaVersion  := "2.11.7"

libraryDependencies ++= Seq(
  "com.typesafe.play"   %%  "anorm"         % "2.4.0",
  "com.chuusai"         %%  "shapeless"     % "2.1.0",
  "org.eu.acolyte"      %%  "jdbc-scala"    % "1.0.33-j7p" % Test,
  "org.specs2"          %%  "specs2-core"   % "2.4.9" % Test
)

bintrayOrganization := Some("buildo")

licenses += ("MIT", url("http://opensource.org/licenses/MIT"))
