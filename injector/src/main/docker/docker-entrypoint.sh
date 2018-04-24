#!/bin/sh

HERE="$(dirname $(readlink -f "$0"))"
exec java \
  -Djava.awt.headless=true \
  -jar "${HERE}/plugins-tiers.jar"
