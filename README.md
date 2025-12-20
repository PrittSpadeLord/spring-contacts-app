Currently work in progress. Future updates to this readme will mention the project's scope and purpose.

Docker compose has been set up!

To run tests, do:

```
docker compose run -e BASE_URL='https://yourbaseurl.com/' -e PORT='8080' -e ...(all other env vars)... tester
```

Consult the `docker-compose.yml` file to see the list of env vars necessary for tests to pass.

To run the application, do:

```
BASE_URL='https://yourbaseurl.com/' PORT='8080' ...(all other env vars)... docker compose up app
```

Pro tip: if you are running your database outside the container, replace `localhost` with `host.docker.internal` as described in the `docker-compose.yml`.