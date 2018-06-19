#!/bin/bash

# Run script for client

# Number of parameters: 4
# Parameter:
#    $1: server_host
#    $2: negotiation_port
#    $3: auth_code
#    $4: msg

java Client $1 $2 $3 "$4"
