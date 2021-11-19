#!/bin/bash
systemctl start docker.socket;
systemctl start docker.service;
docker run $1 -p 1935:1935 -p 3334:3334 -p 3478:3478 -p 8082:8082 -p 9000:9000 -p 9999:9999/udp -p 4000-4005:4000-4005/udp -p 10006-10010:10006-10010/udp io.rastered.ome;
