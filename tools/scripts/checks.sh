#!/usr/bin/env bash

set -e

./gradlew spotlessApply
./gradlew test