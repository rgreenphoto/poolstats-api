#!/bin/sh

rm *.jar
cd ..
mvn -Dmaven.test.skip=true clean package
cd docker
cp ../target/poolstats-api-*.jar .
docker-compose build --force-rm poolstats-api
docker-compose up --force-recreate
