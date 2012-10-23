resolvers ++= Seq(
  "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/",
  "less is" at "http://repo.lessis.me"
)

addSbtPlugin("play" % "sbt-plugin" % "2.1-SNAPSHOT")

addSbtPlugin("com.typesafe.sbt" % "sbt-pgp" % "0.7")

addSbtPlugin("me.lessis" % "ls-sbt" % "0.1.2")
