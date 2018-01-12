#!/usr/bin/env bash

aws s3 cp s3://dev-com-hedvig-cluster-ett-data/kube ~/.kube --recursive

curl -LO https://storage.googleapis.com/kubernetes-release/release/$(curl -s https://storage.googleapis.com/kubernetes-release/release/stable.txt)/bin/linux/amd64/kubectl
chmod +x ./kubectl

./kubectl set image deployment/back-office-service back-office-service=$REMOTE_IMAGE_URL:$TRAVIS_BUILD_NUMBER
