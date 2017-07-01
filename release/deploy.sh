#!/usr/bin/env bash

openssl aes-256-cbc -K $encrypted_350aba14307b_key -iv $encrypted_350aba14307b_iv -in release/codesigning.asc.enc -out release/codesigning.asc -d
gpg --fast-import release/codesigning.asc

mvn versions:set -DnewVersion=$TRAVIS_TAG
mvn deploy -P sign,build-extras -DskipTests --settings release/settings.xml
