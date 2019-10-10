echo "-----------------------------------------------Stop the containers----------------------------------------------------------------------"
docker-compose -f docker-compose.yml stop

echo "-----------------------------------------------Build the application ----------------------------------------------"
docker-compose -f docker-compose.yml build

echo "-----------------------------------------------Starting db1 -----------------------------------"
docker-compose -f docker-compose.yml  up -d db_account
sleep 15s 

echo "-----------------------------------------------Starting db2 -----------------------------------"
docker-compose -f docker-compose.yml  up -d db_stock
sleep 15s 

echo "-----------------------------------------------Starting db3 -----------------------------------"
docker-compose -f docker-compose.yml  up -d db_order
sleep 15s 

echo "-----------------------------------------------Starting the config microservice -----------------------------------"
docker-compose -f docker-compose.yml  up -d p12-config
sleep 15s 

echo "-----------------------------------------------Starting the registration microservice -----------------------------"
docker-compose -f docker-compose.yml  up -d p12-register
sleep 15s

echo "-----------------------------------------------Starting the api-gateway --------------------------------"
docker-compose -f docker-compose.yml up -d p12-api-gateway
sleep 15s

echo "-----------------------------------------------Starting the account service ----------------------------------"
docker-compose -f docker-compose.yml up -d p12-account
sleep 15s

echo "-----------------------------------------------Starting the stock service-----------------------------------"
docker-compose -f docker-compose.yml  up -d p12-stock
sleep 15s  

echo "-----------------------------------------------Starting the order service ----------------------------"
docker-compose -f docker-compose.yml up -d p12-order
sleep 60s

echo "-----------------------------------------------Starting the UI client -------------------------"
docker-compose -f docker-compose.yml up -d p12-ui
sleep 60s

echo "Done"
