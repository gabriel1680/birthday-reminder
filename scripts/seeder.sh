#!/usr/bin/env bash

set -euo pipefail

CLI="./cli/build/libs/cli-standalone.jar"
BIRTHDATE="1999-08-18T00:00:00Z"

for i in $(seq 1 20); do
    echo "Creating contact $i..."
    java -jar "$CLI" create \
        -n="Cleber Wilson Santos $i" \
        -b="$BIRTHDATE"
done

echo
echo "✅ Successfully created 20 contacts."