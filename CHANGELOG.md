# Changelog

## 17.0.0

- Java 17 release line (`maven.compiler.release=17`)
- Jakarta XML Bind API 4.0.5
- SLF4J 2.0.18
- Same utility surface as 11.0.0 on Java 17 bytecode

## 11.0.0

- Java 11 release line (`maven.compiler.release=11`)
- Jakarta XML Bind API 4.0.5
- SLF4J 2.0.18
- Java 11 idioms: NIO.2 UTF-8 file I/O, `List.of`, `Objects.equals`, `DateTimeFormatter`, `ConcurrentHashMap`, try-with-resources, `Class<?>`

## 8.0.0

- Version line aligned to Java 8 (`maven.compiler.release=8`)
- Distribution migrated to Sonatype Central Portal (`central-publishing-maven-plugin`)
- Updated `slf4j` to 1.7.36 (last 1.7.x on Maven Central)
- Plugin versions updated: compiler 3.15.0, surefire 3.5.5, javadoc 3.12.0, source 3.4.0, gpg 3.2.8
- GPG signing skipped by default; enabled via `-Prelease`

## 1.2.1

- Converted to Maven
- Replaced external dependencies with Maven ones

## 1.2.0

- Added support for JVM 1.7_21+

## 1.0

- Initial release

## Copyright

Copyright 2009 NEHTA. Copyright 2021-2026 ADHA. Apache License 2.0 - see **LICENSE.txt**.
