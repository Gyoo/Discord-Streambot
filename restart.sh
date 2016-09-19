#!/bin/bash
PID=$(ps -ef | awk '$NF~"StreamBot" {print $2}') 
kill $PID
java -jar -Xmx1024m StreamBot.jar > log.txt 2> /dev/null &
