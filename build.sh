#!/bin/bash

function deploySnapshots {
 ./mvnw -s $HOME/.m2/settings.xml -D maven.repo.local=$HOME/.m2/repository deploy -f platform.dependencies/pom.xml
 ./mvnw -s $HOME/.m2/settings.xml -D maven.repo.local=$HOME/.m2/repository deploy -f platform.parent/pom.xml
 ./mvnw -s $HOME/.m2/settings.xml -D maven.repo.local=$HOME/.m2/repository deploy -f platform.core.processor/pom.xml
 ./mvnw -s $HOME/.m2/settings.xml -D maven.repo.local=$HOME/.m2/repository deploy -f platform.core.process/pom.xml
 #./mvnw -s $HOME/.m2/settings.xml -D maven.repo.local=$HOME/.m2/repository deploy -f platform.spec.model/pom.xml
 #./mvnw -s $HOME/.m2/settings.xml -D maven.repo.local=$HOME/.m2/repository deploy -f platform.spec.model.test/pom.xml
 ./mvnw -s $HOME/.m2/settings.xml -D maven.repo.local=$HOME/.m2/repository deploy -f platform.web/pom.xml
}

function buildWithOutTests {
 ./mvnw clean install -D skipDockerBuild -D maven.test.skip=true
}

function buildWithTests {
 ./mvnw clean install -D skipDockerBuild
}

function releasePrepare {
  #./mvnw --batch-mode -Dtag=nimbus-core-api-1.0 -Dproject.rel.com.antheminc.oss:nimbus.core.dependencies.api=1.0 -Dproject.dev.com.antheminc.oss:nimbus.core.dependencies.api=1.1-SNAPSHOT release:prepare -f platform.dependencies/pom.xml
  #./mvnw --batch-mode -Dtag=nimbus-core-api-1.0 -Dproject.rel.com.antheminc.oss:nimbus.core.api=1.0 -Dproject.dev.com.antheminc.oss:nimbus.core.api=1.1-SNAPSHOT release:prepare -f platform.parent/pom.xml
  :
}
#releasePrepare
buildWithOutTests
