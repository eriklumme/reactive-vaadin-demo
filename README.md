# reactive-vaadin-demo

A project that demonstrates some of the benefits of working with non-blocking reactive streams when I/O operations are slow.

The `vaadin-frontend` project hosts a simple Vaadin application, that can be run with `mvn spring-boot:run` in that module, or by running `FrontendApplication` in an IDE.

The `spring-backend` hosts the back end, and can be run with the same Maven command, or by running the `BackendApplication` in your IDE.

You can test the app at `localhost:8080`.

# City view

In the city view, there is a grid that lists names of cities. There are three buttons for fetching the cities in different ways (synchronously, asynchronously, reactive/non-blocking).

The backend call to fetch the cities takes around 3 seconds.

# Change password view

In the change password view, a user can change their password, as long as the new password is `Vaadin123`.

It uses a `Context` to make the name of the current user available to the client that initiates the backend call.
