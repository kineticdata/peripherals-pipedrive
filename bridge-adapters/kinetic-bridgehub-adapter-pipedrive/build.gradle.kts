plugins {
  java
  `maven-publish`
}

repositories {
  mavenLocal()
  maven {
    url = uri("https://s3.amazonaws.com/maven-repo-public-kineticdata.com/releases")
  }

  maven {
    url = uri("s3://maven-repo-private-kineticdata.com/releases")
    authentication {
      create<AwsImAuthentication>("awsIm")
    }

  }

  maven {
    url = uri("https://s3.amazonaws.com/maven-repo-public-kineticdata.com/snapshots")
  }

  maven {
    url = uri("s3://maven-repo-private-kineticdata.com/snapshots")
    authentication {
      create<AwsImAuthentication>("awsIm")
    }
  }

  maven {
    url = uri("https://repo.maven.apache.org/maven2/")
  }

  maven {
    url = uri("https://repo.springsource.org/release/")
  }

}

dependencies {
  implementation("org.apache.httpcomponents:httpclient:4.5.1")
  implementation("org.slf4j:slf4j-api:1.7.10")
  implementation("com.googlecode.json-simple:json-simple:1.1.1")
  implementation("com.kineticdata.agent:kinetic-agent-adapter:1.1.3-SNAPSHOT")
  implementation("javax.json:javax.json-api:1.1")
  implementation("com.jayway.jsonpath:json-path:2.8.0")
  implementation("org.glassfish:javax.json:1.1")
  implementation("org.apache.commons:commons-exec:1.1")
  implementation("commons-collections:commons-collections:3.2.2")
}

group = "com.kineticdata.bridges.adapter"
version = "1.0.1-SNAPSHOT"
description = "kinetic-bridgehub-adapter-pipedrive"
java.sourceCompatibility = JavaVersion.VERSION_1_8

publishing {
  publications.create<MavenPublication>("maven") {
    from(components["java"])
  }
  repositories {
    maven {
      val releasesUrl = uri("s3://maven-repo-private-kineticdata.com/releases")
      val snapshotsUrl = uri("s3://maven-repo-private-kineticdata.com/snapshots")
      url = if (version.toString().endsWith("SNAPSHOT")) snapshotsUrl else releasesUrl
      authentication {
        create<AwsImAuthentication>("awsIm")
      }
    }
  }
}

tasks.withType<JavaCompile>() {
  options.encoding = "UTF-8"
}
