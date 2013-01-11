resolvers ++= Seq(
  Resolver.file("Local Play Repository", file(Path.userHome.absolutePath + "/Lib/play2/repository/local"))(Resolver.ivyStylePatterns),
  "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/",
  Resolver.sonatypeRepo("releases")
)

addSbtPlugin("play" % "sbt-plugin" % "2.1-RC2")

addSbtPlugin("com.typesafe.sbt" % "sbt-pgp" % "0.7")

addSbtPlugin("me.lessis" % "ls-sbt" % "0.1.2")
