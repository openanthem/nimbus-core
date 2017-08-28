#!/bin/sh

# The build order: (The command line arguments should be in this order as well)
    # platform.dependencies
    # platform.parent
    # platform.core.processor
    # platform.spec.model
    # platform.spec.model.test
    # platform.core.process
   
if [ $# -gt 0 ]; then
    PROJECTS=$1;
else
    PROJECTS=dependencies,parent,core.processor,spec.model,spec.model.test,core.process;
fi
IFS=',' read -ra NAMES <<< "$PROJECTS"

for i in "${NAMES[@]}"; do
  str="platform."$i;
  echo "####################################################################"
  echo "### Starting to build: $str "
  echo "####################################################################"
  
  BUILD_PROFILES="";
  if [[ "$i" == "web" ]]; then
    BUILD_PROFILES="local"
  fi
  
  if [[ -z "$BUILD_PROFILES" ]]; then
    mvn clean install -f $str/pom.xml -D skipTests=true
  else
    mvn clean install -f $str/pom.xml -D skipTests=true -P $BUILD_PROFILES
  fi 

  echo "####################################################################"
  echo "### Finished building: $str "
  echo "####################################################################"
done
