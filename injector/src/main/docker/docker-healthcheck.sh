#!/bin/sh

port=${1:-8080}
STATUS=$(curl -s -o /dev/null -w '%{http_code}' "http://localhost:${port}/api/ping" || echo 1)
test $STATUS  -eq 200 && exit 0 || exit 1
