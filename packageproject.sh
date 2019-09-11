#!/bin/bash

echo "clean inventory service..."
cd inventory
mvn clean
echo "clean order service..."
cd ../order
mvn clean
echo "clean LRA coordinator service..."
cd ../lra-coordinator-helidon
mvn clean
cd ../
jar cf helidon-example-lra.jar *
