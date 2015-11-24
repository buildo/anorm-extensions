organization  := "io.buildo"

name          := "anorm-extensions"

version       := "0.1.0"

scalaVersion  := "2.11.7"

libraryDependencies ++= Seq(
  "com.typesafe.play"  %%  "anorm"      % "2.4.0",
  "com.chuusai"        %%  "shapeless"  % "2.1.0"
)

bintrayOrganization := Some("buildo")

licenses += ("MIT", url("http://opensource.org/licenses/MIT"))
