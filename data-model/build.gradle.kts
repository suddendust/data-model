plugins {
  `java-library`
  jacoco
  id("org.hypertrace.publish-plugin")
  id("org.hypertrace.jacoco-report-plugin")
  id("com.github.davidmc24.gradle.plugin.avro") version "1.3.0"
}

tasks.test {
  useJUnitPlatform()
}

dependencies {
  api("org.apache.avro:avro:1.10.2")
  constraints {
    api("org.apache.commons:commons-compress:1.21") {
      because("Multiple vulnerabilities in avro-declared version")
    }
  }
  api("commons-codec:commons-codec:1.14")
  api("io.micrometer:micrometer-core:1.5.3")

  implementation("com.google.guava:guava:30.0-jre")
  implementation("org.apache.commons:commons-lang3:3.10")

  testImplementation("org.junit.jupiter:junit-jupiter:5.6.2")
  testImplementation("org.mockito:mockito-core:3.3.3")
}
