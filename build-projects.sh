#!/bin/sh

# The build order: (The command line arguments should be in this order as well)
    # nimbus-parent - it builds:
    	# nimbus-core 
    	# nimbus-entity-dsl
    # nimbus-test
    # nimbus-extn-activiti
	# nimbus-ui-app (it is an npm build that created maven artifact and deploys to artifactory)
   
PROJECTS="nimbus-parent,nimbus-test,nimbus-extn-activiti,nimbus-ui-app";

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
  
  
  if [[ "$i" == "nimbus-ui-app" ]]; then
	  cd $i 
	  npm install
	  npm run prodbuild
	  cd ..
   else
	  mvn clean install -f $str/pom.xml $MVN_ARGS
  fi 

  echo "####################################################################"
  echo "### Finished building: $str "
  echo "####################################################################"
done
