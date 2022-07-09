# network-notification-service

Service to notify mobile network subscribers when called recipients could not be immediately reached and then reappear on the network after some time.

## Build Setup

1. Check out this repository.

2. Add ".env" file and set environmental variables in it. Check .env.example for the list of variables to be set.

3. Install Docker.

## Docker

### Start the system

```bash
docker compose up -d
```

### Shutdown the system

```bash
docker compose down
```

### Connect to logs of Spring Boot backend

```bash
docker logs --tail 50 --follow --timestamps nns-core
```

## Testing
