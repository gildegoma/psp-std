package psp
package build

import sbt._, Keys._
import bintray.Plugin.bintrayPublishSettings

object Build extends sbt.Build {
  def imports = """
    import java.{ lang => jl, util => ju }
    import java.nio.{ file => jnf }
    import psp.std._
  """.trim

  def common = bintrayPublishSettings ++ Seq[Setting[_]](
               organization :=  "org.improving",
                    version :=  "0.1.0-M1",
               scalaVersion :=  "2.11.2",
                logBuffered :=  false,
              scalacOptions ++= Seq("-language:_"),
               javacOptions ++= Seq("-nowarn", "-XDignore.symbol.file"),
                   licenses :=  Seq("Apache-2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0")),
    publishArtifact in Test :=  false
  )

  /** What is accomplished by this structure?
   *
   *   - std does not depend on scala-reflect
   *   - the macro project can make use of psp.std
   *   - when testing psp.std, we have access to psp.macros
   *   - after touching a testfile, the macro runs and the tests run, but the entire project isn't rebuilt
   *
   *  It's harder to get all those at once than you may think.
   */
  lazy val root = project in file(".") dependsOn (macros, std) aggregate (macros, std) settings (common: _*) settings (
                          name :=  "psp-std-root",
                   description :=  "psp's project which exists to please sbt",
                   shellPrompt :=  (s => "%s#%s> ".format(name.value, (Project extract s).currentRef.project)),
    initialCommands in console :=  imports,
                       publish :=  (),
                  publishLocal :=  (),
               publishArtifact :=  false,
                          test <<= run in Test toTask "" dependsOn (clean in Test)
  )

  lazy val std = project settings (common: _*) settings (
           name := "psp-std",
    description := "psp's non-standard standard library"
  )

  lazy val macros = project dependsOn std settings (common: _*) settings (
                   name := "psp-std-macros",
            description := "macros for psp's non-standard standard library",
    libraryDependencies += "org.scala-lang" % "scala-reflect" % scalaVersion.value
  )
}
