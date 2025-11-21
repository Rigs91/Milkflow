#!/bin/sh
DIR="$(cd "$(dirname "$0")" && pwd)"
JAVA_CMD="${JAVA_HOME:+$JAVA_HOME/bin/}java"
if [ -z "$JAVA_HOME" ]; then
  JAVA_CMD="java"
fi
"$JAVA_CMD" -Dorg.gradle.appname=gradlew -classpath "$DIR/gradle/wrapper/gradle-wrapper.jar" org.gradle.wrapper.GradleWrapperMain "$@"
