#!/bin/bash

echo "build inventory service..."
cd inventory
mvn install
echo "build order service..."
cd ../order
mvn install
echo "build LRA coordinator service..."
cd ../lra-coordinator-helidon
mvn install
./build-lra-coordinator-helidon-jar.sh
