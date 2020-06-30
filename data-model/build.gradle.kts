plugins {
  `java-library`
  jacoco
  id("org.hypertrace.avro-plugin") version "0.2.1"
  id("org.hypertrace.publish-plugin")
  id("org.hypertrace.jacoco-report-plugin")
}

tasks.test {
  useJUnitPlatform()
}

dependencies {
  api("org.apache.avro:avro:1.9.2")
  api("commons-codec:commons-codec:1.14")

  implementation("com.google.guava:guava:29.0-jre")
  implementation("org.apache.commons:commons-lang3:3.10")

  testImplementation("org.junit.jupiter:junit-jupiter:5.6.2")
  testImplementation("org.mockito:mockito-core:3.3.3")
}
