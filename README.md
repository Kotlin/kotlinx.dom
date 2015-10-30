# kotlinx.dom [ ![Download](https://api.bintray.com/packages/kotlin/kotlinx.dom/kotlinx.dom/images/download.svg) ](https://bintray.com/kotlin/kotlinx.dom/kotlinx.dom/_latestVersion) [![TeamCity (simple build status)](https://img.shields.io/teamcity/http/teamcity.jetbrains.com/s/KotlinTools_KotlinxDom_Build.svg)](https://teamcity.jetbrains.com/viewType.html?buildTypeId=KotlinTools_KotlinxDom_Build&branch_Kotlin_KotlinX=%3Cdefault%3E&tab=buildTypeStatusDiv&guest=1)

# Maven 

```xml
<dependency>
    <groupId>org.jetbrains.kotlinx</groupId>
    <artifactId>kotlinx.dom</artifactId>
    <version>${kotlinx.dom.version}</version>
</dependency>

<repositories>
  <repository>
    <snapshots>
      <enabled>false</enabled>
    </snapshots>
    <id>bintray-kotlin-kotlinx.html</id>
    <name>bintray</name>
    <url>http://dl.bintray.com/kotlin/kotlinx.dom</url>
  </repository>
</repositories>
```

# Gradle

```groovy
repositories {
    maven {
        url "http://dl.bintray.com/kotlin/kotlinx.dom" 
    }
}

dependencies {
    compile "org.jetbrains.kotlinx:kotlinx.dom:${kotlinx.dom.version}"
}
```
