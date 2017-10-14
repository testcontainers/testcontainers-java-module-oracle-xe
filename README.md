# TestContainers Oracle XE Module

Testcontainers module for the Oracle XE database.

See [testcontainers.org](https://www.testcontainers.org) for more information about Testcontainers.

<!--[![Build Status](https://travis-ci.org/testcontainers/testcontainers-java-module-oracle-xe.svg?branch=master)](https://travis-ci.org/testcontainers/testcontainers-java-module-oracle-xe)-->

## Usage example

Running Oracle XE as a stand-in for in a test:

```java
public class SomeTest {

    @Rule
    public OracleContainer oracle = new Oracle XEContainer();
    
    @Test
    public void someTestMethod() {
        String url = oracle.getJdbcUrl();

        ... create a connection and run test as normal
```

## Dependency information

### Maven

```
<dependency>
    <groupId>org.testcontainers</groupId>
    <artifactId>oracle-xe</artifactId>
    <version>1.4.3</version>
</dependency>
```

### Gradle

```
compile group: 'org.testcontainers', name: 'oracle-xe', version: '1.4.3'
```

## License

See [LICENSE](LICENSE).

## Copyright

Copyright (c) 2015 - 2017 Richard North and other authors.

See [AUTHORS](AUTHORS) for contributors.
