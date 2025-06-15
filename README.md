# SubstAgent
A Java agent that listens for strings containing environment variables and substitutes them accordingly. It is intended for use with Spigot plugins to enable environment variable support in their configuration files.

## How to use
`java -javaagent:<path to SubstAgent jar> -Xbootclasspath/a:<path to SubstAgent jar> ...`