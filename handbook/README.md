# Handbook

## Build and run the Docker image locally

From the project root:

```bash
# Build
docker build -t s-plan-api:local .

# Run (with your .env file)
docker run --rm -p 6033:6033 --env-file .env --name s-plan-api s-plan-api:local
```

Then hit `http://localhost:6033`.

### Notes

- **`.env` requirements** — `application.properties` expects `DB_URL`, `DB_USER`,
  `DB_PASS`, `AUTH0_ISSUER_URI`, `AUTH0_AUDIENCE`. If `DB_URL` points at
  `localhost`, the container can't reach your host DB — use
  `host.docker.internal` instead (Mac/Windows), e.g.
  `DB_URL=jdbc:postgresql://host.docker.internal:5432/stracker`.
- **Detached mode** — swap `--rm` for `-d` to run in the background.
  `docker logs -f s-plan-api` to tail, `docker stop s-plan-api` to kill.
- **Force a clean rebuild** — `docker build --no-cache -t s-plan-api:local .`
  The Gradle cache mount keeps normal rebuilds fast; only use `--no-cache` when
  you need to invalidate everything.
- **Mirror the deploy image** — to test what the server will run, build with
  the same tag shape: `docker build -t ghcr.io/sautergmbh/s-plan-api:dev .`
  and run with that tag.


### NEON DB Setup
- enable extension
```sql
CREATE EXTENSION IF NOT EXISTS timescaledb;
```

- create table
```sql
CREATE TABLE telemetry_locations (
    vehicle_id BIGINT NOT NULL,
    point_id TEXT NOT NULL,
    recorded_at TIMESTAMP NOT NULL,
    latitude DOUBLE PRECISION NOT NULL,
    longitude DOUBLE PRECISION NOT NULL,
    accuracy_m DOUBLE PRECISION,
    speed_mps DOUBLE PRECISION,
    bearing_deg DOUBLE PRECISION,
    is_moving BOOLEAN NOT NULL,
    received_at TIMESTAMP NOT NULL DEFAULT now(),
    PRIMARY KEY (vehicle_id, point_id, recorded_at)
);
```

- create hypertable
```sql
SELECT create_hypertable(
    'telemetry_locations',
    'recorded_at',
    if_not_exists => TRUE
);
```

- add index
```sql
CREATE INDEX telemetry_locations_vehicle_time_idx
ON telemetry_locations (vehicle_id, recorded_at DESC);
```

### Grafana OTLP
Password:

```bash
echo -n '1616346:glc_eyJvIjo' | base64
```

### Login on Server with Github Deploy User
```bash
echo "ghp_piWuvBIsV" | docker login ghcr.io \
  -u deploysauter \
  --password-stdin
```

```bash
echo "ghp_sampletoken" | docker login ghcr.io -u deploysauter --password-stdin
```

```bash
apt update
apt install -y docker.io

systemctl enable docker
systemctl start docker

docker --version
docker ps
```
