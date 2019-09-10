#!/bin/bash

echo "adding lra-coordinator-5.9.8.Final.war classes to lra-coordinator-helidon-0.0.1-SNAPSHOT.jar ... "
mkdir lra-coordinator-helidon-jarbuild
#cp target/libs/lra-coordinator-5.9.8.Final.war lra-coordinator-helidon-jarbuild/
cp /Users/pparkins/go/src/github.com/jbosstm/narayana/rts/lra/lra-coordinator/target/lra-coordinator.war lra-coordinator-helidon-jarbuild/
cd lra-coordinator-helidon-jarbuild/
#jar xvf lra-coordinator-5.9.8.Final.war
jar xvf lra-coordinator.war
cd WEB-INF/classes/
jar uvf ../../../target/lra-coordinator-helidon-0.0.1-SNAPSHOT.jar *
cd ../../../
rm -rf lra-coordinator-helidon-jarbuild
