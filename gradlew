#!/usr/bin/env sh

set -e

GRADLE_WRAPPER_JAR="$PROJECT_DIR/gradle/wrapper/gradle-wrapper.jar"

if [ ! -f "$GRADLE_WRAPPER_JAR" ]; then
    echo "Error: No se encontró gradle-wrapper.jar"
    exit 1
fi

exec java -jar "$GRADLE_WRAPPER_JAR" "$@"
