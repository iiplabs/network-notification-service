# sms-service

Dummy service to send SMS notifications when user reconnects to the cell network.

End-points:

1. POST "api/v1/sendSms"

Example of the request body:

{
  "text": "Этот абонент снова в сети",
  "msisdnB": "79031112233", "msisdnA": "79063332211"
}

The service waits for any time up to 5 seconds to simulate a slow service and responds with HTTP status 200 and the following body when there are no issues.

{
  "status": "OK"
}

The service throttles the number of incoming HTTP requests to 300 per second. When the number of HTTP requests exceed the maximum, the service shall respond with HTTP status 429 and the following body.

{ 
  "status": 429, 
  "error": "Too Many Requests", 
  "message": "Too Many Requests"
}

### Connect to logs of Spring Boot backend

```bash
docker logs --tail 50 --follow --timestamps sms-service
```

## Testing

```bash
mvn test
```
