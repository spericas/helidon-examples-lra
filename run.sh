#!/bin/bash

echo "running coordinator service..."
java -jar lra-coordinator-helidon/target/lra-coordinator-helidon-0.0.1-SNAPSHOT.jar &
echo "running order service..."
java -jar order/target/order-0.0.1-SNAPSHOT.jar &
echo "running inventory service..."
java -jar inventory/target/inventory-0.0.1-SNAPSHOT.jar &


echo "running success/complete case ..."
echo "adding inventory ..."
curl http://localhost:8091/inventory/addInventory
echo "placing order ..."
curl http://localhost:8090/placeOrder

echo "running failure/compensate case ..."
echo "removing inventory ..."
curl http://localhost:8091/inventory/removeInventory
echo "placing order ..."
curl http://localhost:8090/placeOrder

echo "finished"


