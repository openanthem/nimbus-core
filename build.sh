#!/bin/bash

wget http://clojars.org/repo/redis/embedded/embedded-redis/0.4/embedded-redis-0.4.jar
mvn -D maven.repo.local=/home/loudbinary/.m2/repository install:install-file -Dfile=embedded-redis-0.4.jar
rm embedded-redis-0.4.jar


function buildWithOutTests {
  mvn -D maven.repo.local=/home/loudbinary/.m2/repository clean install -D skipDockerBuild -f platform.dependencies/pom.xml -D maven.test.skip=true
  mvn -D maven.repo.local=/home/loudbinary/.m2/repository clean install -f platform.parent/pom.xml -D skipDockerBuild -D maven.test.skip=true
  mvn -D maven.repo.local=/home/loudbinary/.m2/repository clean install -f platform.core.processor/pom.xml -D skipDockerBuild -D maven.test.skip=true
  mvn -D maven.repo.local=/home/loudbinary/.m2/repository clean install -f platform.spec.model/pom.xml -D skipDockerBuild -D maven.test.skip=true
  mvn -D maven.repo.local=/home/loudbinary/.m2/repository clean install -f platform.spec.model.test/pom.xml -D skipDockerBuild -D maven.test.skip=true
  mvn -D maven.repo.local=/home/loudbinary/.m2/repository clean install  -f platform.core.process/pom.xml -D skipDockerBuild -D maven.test.skip=true
  mvn -D maven.repo.local=/home/loudbinary/.m2/repository clean install  -f platform.web/pom.xml -D skipDockerBuild -D maven.test.skip=true
}

function buildWithTests {
  mvn -D maven.repo.local=/home/loudbinary/.m2/repository clean install -D skipDockerBuild -f platform.dependencies/pom.xml
  mvn -D maven.repo.local=/home/loudbinary/.m2/repository clean install -f platform.parent/pom.xml -D skipDockerBuild
  mvn -D maven.repo.local=/home/loudbinary/.m2/repository clean install -f platform.core.processor/pom.xml -D skipDockerBuild
  mvn -D maven.repo.local=/home/loudbinary/.m2/repository clean install -f platform.spec.model/pom.xml -D skipDockerBuild
  mvn -D maven.repo.local=/home/loudbinary/.m2/repository clean install -f platform.spec.model.test/pom.xml -D skipDockerBuild
  mvn -D maven.repo.local=/home/loudbinary/.m2/repository clean install  -f platform.core.process/pom.xml -D skipDockerBuild
  mvn -D maven.repo.local=/home/loudbinary/.m2/repository clean install  -f platform.web/pom.xml -D skipDockerBuild
}

buildWithOutTests
