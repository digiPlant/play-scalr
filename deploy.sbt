import xerial.sbt.Sonatype.SonatypeKeys._

sonatypeSettings

// Workaround for bouncycastle not working with play 2.3

useGpg := true

//credentials := Seq(Credentials(Path.userHome / ".ivy2" / ".digiplantcredentials"))

licenses := Seq("Apache 2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0"))

homepage := Some(url("https://github.com/digiplant/play-scalr"))

pomExtra :=
    <scm>
      <url>git://github.com/digiPlant/play-scalr.git</url>
      <connection>scm:git://github.com/digiPlant/play-scalr.git</connection>
    </scm>
    <developers>
      <developer>
        <id>leon</id>
        <name>Leon Radley</name>
        <url>http://github.com/leon</url>
      </developer>
    </developers>