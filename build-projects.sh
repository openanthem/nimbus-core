#!/bin/sh

# The build order: (The command line arguments should be in this order as well)
    # nimbus-parent - it builds:
        # nimbus-core 
        # nimbus-entity-dsl
        # nimbus-test
    # nimbus-ui
    # nimbus-extn-activiti
    # nimbus-starter
   
PROJECTS=".,nimbus-ui,nimbus-extn-activiti,nimbus-starter";

while getopts p:m: option
do
  case "${option}"
  in
  p) PROJECTS=${OPTARG};;
  m) MVN_ARGS=${OPTARG};;
  esac
done

IFS=',' read -ra NAMES <<< "$PROJECTS"

echo "Running build-projects.sh with the following parameters..."
echo "NAMES = $NAMES";
echo "MVN_ARGS = $MVN_ARGS";

for i in "${NAMES[@]}"; do
  str=$i;
  echo "####################################################################"
  echo "### Starting to build: $str "
  echo "####################################################################"
  
  BUILD_PROFILES="";
  if [[ "$i" == "nimbus-ui" ]]; then
    BUILD_PROFILES="devbuild"
  fi
  
  if [[ -z "$BUILD_PROFILES" ]]; then
    mvn clean install -f $str/pom.xml $MVN_ARGS
  else
    mvn clean install -f $str/pom.xml $MVN_ARGS -P $BUILD_PROFILES
  fi 

  echo "####################################################################"
  echo "### Finished building: $str "
  echo "####################################################################"
done
