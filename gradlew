#!/usr/bin/env sh
set -e
HERE="$(cd "$(dirname "$0")" && pwd)"
CLASSPATH="$HERE/gradle/wrapper/gradle-wrapper.jar:$HERE/gradle/wrapper/gradle-wrapper-shared.jar"
if [ -z "$JAVA_HOME" ]; then
  JAVA_EXE=java
else
  JAVA_EXE="$JAVA_HOME/bin/java"
fi
exec "$JAVA_EXE" $GRADLE_OPTS $JAVA_OPTS -classpath "$CLASSPATH" org.gradle.wrapper.GradleWrapperMain "$@"
