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
  ./mvnw -D maven.repo.local=$HOME/.m2/repository clean install -D skipDockerBuild -f platform.dependencies/pom.xml -D maven.test.skip=true
  ./mvnw -D maven.repo.local=$HOME/.m2/repository clean install -f platform.parent/pom.xml -D skipDockerBuild -D maven.test.skip=true
  ./mvnw -D maven.repo.local=$HOME/.m2/repository clean install -f platform.core.processor/pom.xml -D skipDockerBuild -D maven.test.skip=true
  ./mvnw -D maven.repo.local=$HOME/.m2/repository clean install -f platform.spec.model/pom.xml -D skipDockerBuild -D maven.test.skip=true
  ./mvnw -D maven.repo.local=$HOME/.m2/repository clean install -f platform.spec.model.test/pom.xml -D skipDockerBuild -D maven.test.skip=true
  ./mvnw -D maven.repo.local=$HOME/.m2/repository clean install  -f platform.core.process/pom.xml -D skipDockerBuild -D maven.test.skip=true
  ./mvnw -D maven.repo.local=$HOME/.m2/repository clean install  -f platform.web/pom.xml -P devbuild -D skipDockerBuild -D maven.test.skip=true
}

function buildWithTests {
  ./mvnw -D maven.repo.local=$HOME/.m2/repository clean install -D skipDockerBuild -f platform.dependencies/pom.xml
  ./mvnw -D maven.repo.local=$HOME/.m2/repository clean install -f platform.parent/pom.xml -D skipDockerBuild
  ./mvnw -D maven.repo.local=$HOME/.m2/repository clean install -f platform.core.processor/pom.xml -D skipDockerBuild
  ./mvnw -D maven.repo.local=$HOME/.m2/repository clean install -f platform.spec.model/pom.xml -D skipDockerBuild
  ./mvnw -D maven.repo.local=$HOME/.m2/repository clean install -f platform.spec.model.test/pom.xml -D skipDockerBuild
  ./mvnw -D maven.repo.local=$HOME/.m2/repository clean install  -f platform.core.process/pom.xml -D skipDockerBuild
  ./mvnw -D maven.repo.local=$HOME/.m2/repository clean install  -f platform.web/pom.xml -P devbuild -D skipDockerBuild
}

function releasePrepare {
  #./mvnw --batch-mode -Dtag=nimbus-core-api-1.0 -Dproject.rel.com.antheminc.oss:nimbus.core.dependencies.api=1.0 -Dproject.dev.com.antheminc.oss:nimbus.core.dependencies.api=1.1-SNAPSHOT release:prepare -f platform.dependencies/pom.xml
  #./mvnw --batch-mode -Dtag=nimbus-core-api-1.0 -Dproject.rel.com.antheminc.oss:nimbus.core.api=1.0 -Dproject.dev.com.antheminc.oss:nimbus.core.api=1.1-SNAPSHOT release:prepare -f platform.parent/pom.xml
  :
}
#releasePrepare
buildWithOutTests
