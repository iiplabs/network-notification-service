# network-notification-service

Service to notify mobile network subscribers when called recipients could not be immediately reached and then reappear on the network after some time. The maximum number of such requests shall be 100-500 per second.

The system shall use 30 seconds interval between attempts to locate the subscriber, for a maximum period of 72 hours (three days).

Upon success, the system shall send an SMS message to the person who attempted to call the subscriber in the first place. The default window for such SMS messages shall be 09:00 - 22:00 each day. SMS service shall throttle the number of incoming HTTP requests to 300 per second, responding with error code 429 when the number of requests per second exceeds that parameter.

## Build Setup

1. Check out this repository.

2. Add ".env" file and set environmental variables in it. Check .env.example for the list of variables to be set and/or use the section below.

3. Install Docker.

## Environment variables

Below is the list of recommended content for your local .env file.

NNS_CORE_SQL_DATABASE=nns-core

NNS_CORE_SQL_USER=nns

NNS_CORE_SQL_PASSWORD=1234567890

NNS_CORE_SQL_ROOT_PASSWORD=root

NNS_CORE_MYSQL_PORT=3306

NNS_CORE_SERVER_PORT=9091

NNS_CORE_DB_URL=jdbc:mysql://nns-core-db:3306/nns-core?serverTimezone=Europe/Moscow&useSSL=false

NNS_CORE_MAX_REQUESTS_PER_SECOND=200

PING_SERVER_PORT=9092

SMS_SERVER_PORT=9093

NOTIFICATION_EXPIRY_WINDOW_HOURS=72

PING_ERROR_REPEAT_INTERVAL_SECONDS=30

PING_URL=http://ping-service:9092/api/v1/ping

SMS_SEND_WINDOW=09:00-22:00

SMS_TEXT=Абонент в сети с {$$time}. Вы можете перезвонить ему.

SMS_ERROR_REPEAT_INTERVAL_SECONDS=30

SMS_URL=http://sms-service:9093/api/v1/sendSms

SMS_MAX_REQUESTS_PER_SECOND=300

UI_API_URL=http://localhost:9091/api/v1

UI_SOCKET_URL=http://127.0.0.1:9091/ws

TZ=Europe/Moscow

## Docker

### Start the system

```bash
docker compose up -d
```

### Shutdown the system

```bash
docker compose down
```

### Rebuild an individual service

```bash
docker compose build ksms-service
```

### Check the latest build date of a service

```bash
docker inspect -f '{{.Created}}' ksms-service
```

### Redeploy an individual service

```bash
docker compose up --no-deps -d ksms-service
```

### Connect to logs of Spring Boot backend

```bash
docker logs --tail 50 --follow --timestamps nns-core
```

```bash
docker logs --tail 50 --follow --timestamps ping-service
```

```bash
docker logs --tail 50 --follow --timestamps sms-service
```

```bash
docker logs --tail 50 --follow --timestamps ksms-service
```

## Testing

Maven shall be used for unit testing.

```bash
mvn test
```

For E2E - end to end testing, please use NNS UI sub-project available at http://localhost:8080. Set the number of requests to be generated and watch them updated in real time, through the use of WebSockets implementations.
