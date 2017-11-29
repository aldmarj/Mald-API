#!/usr/bin/env bash

render_template() {
  eval "echo \"$(cat $1)\""
}

render_template k8s-deployment-template.yml > k8s-deployment.yml

kubectl apply -f k8s-deployment.yml

# Blocking command so job exits when deployment rollout finishes
kubectl rollout status deployment/mald-api-deployment