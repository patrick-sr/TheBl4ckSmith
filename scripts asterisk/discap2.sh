#!/bin/bash

echo "Channel: SIP/OPERADORA/$1" > "/data/disca$2.call"
echo "MaxRetries: 1" >> "/data/disca$2.call"
echo "MaxRetries: 60" >> "/data/disca$2.call"
echo "WaitTime: 30" >> "/data/disca$2.call"
echo "Context: saidap2" >> "/data/disca$2.call"
echo "Extension: $1" >> "/data/disca$2.call"
echo "Callerid: $2" >> "/data/disca$2.call"
echo "Priority: 1" >> "/data/disca$2.call"

touch /data/$2
mv /data/disca$2.call /var/spool/asterisk/outgoing/