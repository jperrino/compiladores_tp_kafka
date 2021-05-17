# Docker Kafka Server with Java Consumer and Producer

## Pre-requisites:
- Docker version 19.03.13 installed.
- Docker-compose version 1.27.4 installed.
- Java 8+ installed.
- IntelliJ Idea IDE installed.

## Setup:
- Clone the repo.
- Cd to the root folder
- Run command:
`docker-compose -f docker-compose.yml up -d`
- Run command: `docker exec -it kafka /bin/sh` to access bash console
- Change directory to `opt/kafka`
- Run command to create **test** topic: 
`./bin/kafka-topics.sh --create --zookeeper zookeeper:2181 --replication-factor 1 --partitions 1 --topic test`
- Open project in IntelliJ
- Build and run **SampleProducer**
- Build and run **SampleConsumer**
