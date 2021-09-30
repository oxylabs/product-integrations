# Requirements

- Java 1.8+
- javax.json-1.1.4.jar

# Usage

It is required to include JSON library in the classpath while compiling:
```bash
$ javac -classpath javax.json-1.1.4.jar:. Listener.java
```

It is required to include JSON library in the classpath while rinning as well:
```bash
$ java -classpath javax.json-1.1.4.jar:. Listener
```