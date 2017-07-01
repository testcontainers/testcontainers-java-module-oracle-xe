#!/usr/bin/env bash

openssl aes-256-cbc -K $encrypted_4ed9d1624ee2_key -iv $encrypted_4ed9d1624ee2_iv -in release/codesigning.asc.enc -out release/codesigning.asc -d
gpg --fast-import release/codesigning.asc

mvn versions:set -DnewVersion=$TRAVIS_TAG
mvn deploy -P sign,build-extras -DskipTests --settings release/settings.xml
