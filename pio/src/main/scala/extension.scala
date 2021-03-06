package psp
package std
package pio

import api._

/** Purely extension methods.
 */

final class JioSeqStringOps(val xs: scSeq[String]) extends AnyVal {
  def nonEmpties: scSeq[String] = xs filterNot (s => (s eq null) || (s == ""))
}

final class JioJarEntryOps(val entry: JarEntry) extends AnyVal {
  def isClassFile: Boolean = entry.getName endsWith ".class"
  def precise: Precise     = entry.getSize.size
}

final class JioUriOps(val uri: jUri) extends AnyVal {
  def slurp(): Array[Byte] = uri.toURL.openStream().slurp()
  def fs: Path             = java.nio.file.Paths get uri
}
