#!/usr/bin/env bash
set -euo pipefail

VERSION="${1:?Usage: deploy-s-plan-api.sh <version>}"

# Config
CLIENT="sauter-dev"
OWNER="sautergmbh"
REPO="s-plan-api"

IMAGE="ghcr.io/${OWNER}/${REPO}:${VERSION}"
CONTAINER="s-plan-api-${CLIENT}"
ENV_FILE="/opt/s-plan/${CLIENT}-api.env"
LOG_VOLUME="s-plan-api-${CLIENT}-logs"

# Steps
echo "Deploying $IMAGE → $CONTAINER"

if [ ! -f "$ENV_FILE" ]; then
  echo "❌ Missing env file: $ENV_FILE"
  exit 1
fi

echo "📥 Pulling image..."
docker pull "$IMAGE"

echo "🧹 Removing old container (if exists)..."
docker rm -f "$CONTAINER" 2>/dev/null || true

echo "🚀 Starting new container..."
docker run -d \
  --name "$CONTAINER" \
  --restart unless-stopped \
  --env-file "$ENV_FILE" \
  -v "${LOG_VOLUME}:/app/logs" \
  -p 6033:6033 \
  "$IMAGE"

echo "🧼 Cleaning unused images..."
docker image prune -f

echo "✅ Done"
docker ps --filter "name=$CONTAINER"
