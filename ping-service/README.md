# ping-service

Mock service to check if subscriber has reconnected to the mobile network.

End-points:

1. GET "api/v1/ping?msisdn=\<msisndB\>"

"msisndB" is the 11 digits phone number

The service responds randomly on whether subscriber is connected to mobile network.

The service's response when subscriber is in network.

{
  "status": "inNetwork"
}

The service's response when subscriber is not in network.

{
  "status": "unavailableSubscriber"
}

The body of the incoming request shall be validated for the following rules.

1. Parameter "msisdn" (phone number) shall be mandatory, with 11 digits.

If the incoming request is found as not valid, the service responds with HTTP status 400 and the following example body.

{
  "errors": {
    "msisdn": "Incorrect msisdn"
  }
}

## Connect to the service logs

```bash
docker logs --tail 50 --follow --timestamps ping-service
```

## Testing

```bash
mvn test
```
