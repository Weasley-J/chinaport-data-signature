#!/bin/bash
TAG="1.1.1"
docker stop chinaport-data-signature && docker rm -f chinaport-data-signature
docker rmi -f weasleyj/chinaport-data-signature:${TAG}-arm64
docker build -t weasleyj/chinaport-data-signature:${TAG}-arm64 . -f ./Dockerfile
docker login -u weasleyj
docker push weasleyj/chinaport-data-signature:${TAG}-arm64
exit 0
