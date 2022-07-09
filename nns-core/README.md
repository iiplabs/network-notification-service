# nns-core

REST service to check if the mobile subscriber has reconnected to the cell network and notify the other subscriber when he does so.

End-points:

1. POST "api/v1/unavailableSubscriber"

Example of the request body:

{
  "msisdnB": "79031112233", 
  "msisdnA": "79063332211"
}

The service accepts the incoming request for checking and immediately responds with HTTP status 200 and the following body when the request is valid.

{
  "status": "OK"
}

The service throttles the number of incoming HTTP requests to max 500 per second. When the number of HTTP requests exceed the maximum, the service shall respond with HTTP status 429 and the following body.

{ 
  "status": 429, 
  "error": "Too Many Requests", 
  "message": "Too Many Requests"
}

The body of the incoming request shall be validated using the following rules.

1. Fields "msisdnA" and "msisdnB" (phone numbers) shall be present, with 11 digits.

If the incoming request is found as not valid, the service responds with HTTP status 400 and the following example body.

{
  "status": "FAIL",
  "errors": {
    "destinationPhone": "Incorrect msisdnA"
  }
}

### Connect to the service logs

```bash
docker logs --tail 50 --follow --timestamps nns-core
```

### Testing

```bash
mvn test
```
