#!/bin/bash
# Add check database is up and running
if [ ! "$(docker ps -q -f name=db)" ]; then

    docker run --name db -p 3306:3306 -e MYSQL_USER=admin \
    -e MYSQL_PASSWORD=admin -e MYSQL_ROOT_PASSWORD=password \
    -e MYSQL_DATABASE=xmlparser -d mysql:8.1
echo "Wait for DB startup"
sleep 30
fi

export DATABASE_URL_CUSTOMIZE="jdbc:mysql://localhost:3306/xmlparser"
export USER_NAME=admin
export USER_PASSWORD=admin
./gradlew bootRun


