// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.1.0" apply false
    id("com.google.gms.google-services") version "4.4.1" apply false
}
buildscript {
    repositories {
        google()
        mavenCentral()  // Убедитесь, что Maven Central доступен, так как Realm распространяется через него
    }
    dependencies {
        classpath("com.android.tools.build:gradle:8.1.0")  // Замените на актуальную версию вашего Android Gradle Plugin
        classpath("io.realm:realm-gradle-plugin:10.18.0")  // Замените на последнюю версию Realm
    }
}