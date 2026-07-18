#!/usr/bin/env bash

set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PYTHON_BIN="${PYTHON_BIN:-python3}"
API_BASE_URL="${API_BASE_URL:-http://localhost:8080}"
NOTIFICATION_COUNT="${1:-20}"
NOTIFICATIONS_URL="${API_BASE_URL%/}/notifications"

if [[ ! "$NOTIFICATION_COUNT" =~ ^[1-9][0-9]*$ ]]; then
    echo "Notification count must be a positive integer." >&2
    exit 1
fi

if (( NOTIFICATION_COUNT > 10000 )); then
    echo "Notification count must not exceed 10000." >&2
    exit 1
fi

if ! command -v curl >/dev/null 2>&1; then
    echo "curl is required to seed notifications through the API." >&2
    exit 1
fi

if ! command -v "$PYTHON_BIN" >/dev/null 2>&1; then
    echo "Python was not found. Set PYTHON_BIN to a valid Python executable." >&2
    exit 1
fi

if ! "$PYTHON_BIN" -c "import faker" >/dev/null 2>&1; then
    echo "Python Faker is required." >&2
    echo "Install it with: $PYTHON_BIN -m pip install -r $SCRIPT_DIR/requirements.txt" >&2
    exit 1
fi

if ! curl --fail --silent --show-error "$NOTIFICATIONS_URL" >/dev/null; then
    echo "The notifications API is not available at $NOTIFICATIONS_URL" >&2
    echo "Start the API or set API_BASE_URL to the correct address." >&2
    exit 1
fi

created=0

while IFS= read -r payload; do
    created=$((created + 1))
    echo "Creating email notification $created/$NOTIFICATION_COUNT..."

    curl --fail --silent --show-error \
        --request POST \
        --header "Content-Type: application/json" \
        --data "$payload" \
        "$NOTIFICATIONS_URL" \
        >/dev/null
done < <("$PYTHON_BIN" - "$NOTIFICATION_COUNT" <<'PYTHON'
import json
import sys

from faker import Faker


count = int(sys.argv[1])
fake = Faker("pt_BR")

for _ in range(count):
    payload = {
        "type": "email",
        "value": fake.unique.email(),
    }
    print(json.dumps(payload, ensure_ascii=True))
PYTHON
)

echo
echo "✅ Successfully created $created email notifications through the API."
