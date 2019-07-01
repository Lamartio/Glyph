#!/usr/bin/env bash

./gradlew clean :library-android:build :library-android:bintrayUpload $@
