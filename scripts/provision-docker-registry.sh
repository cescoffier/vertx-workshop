#!/bin/bash

export host=localhost
mkdir ../../docker-registry-data
docker run -d -p 5000:5000 --restart=always --name registry \
  -v `pwd`../../docker-registry-data:/var/lib/registry \
  registry:2
docker pull mongo && docker tag mongo ${host}:5000/mongo
docker pull redis && docker tag redis ${host}:5000/redis
docker pull hawkular/hawkular && docker tag hawkular/hawkular ${host}:5000/hawkular
docker pull grafana/grafana && docker tag grafana/grafana ${host}:5000/grafana
docker pull jboss/hawkular-aio && docker tag jboss/hawkular-aio ${host}:5000/hawkular-aio
docker pull graylog2/allinone && docker tag graylog2/allinone ${host}:5000/allinone
docker pull java:openjdk-8u66-jre && docker tag  java:openjdk-8u66-jre ${host}:5000/java
