name := "bbs"

version := "1.0"

lazy val `bbs` = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.10.4"

libraryDependencies ++= Seq( jdbc , cache , ws,
  "org.scalikejdbc" %% "scalikejdbc"                       % "2.2.1",
  "org.scalikejdbc" %% "scalikejdbc-play-plugin"           % "2.3.4",
  "org.scalikejdbc" %% "scalikejdbc-play-fixture-plugin"   % "2.3.4", // optional
  // substitute this for whatever DB driver you're using:
  "com.h2database"  %  "h2"                                % "1.4.184",
  "com.github.tototoshi" %% "play-flyway"  %  "1.1.3" )

unmanagedResourceDirectories in Test <+=  baseDirectory ( _ /"target/web/public/test" )  