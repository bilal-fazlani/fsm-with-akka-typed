# Finite State Machines with Akka Typed Actors

## Running Instructions

```sh
sbt run
```

This will start an Akka HTTP server on port 8080

## Endpoints

This project uses akka-http server to expose three endpoints. Checkout the [test.http](src/main/scala/com/example/test.http) file

|Endpoint|Akka Message| Description|
|-|-|-|
|`POST http://localhost:8080/?data=<STRING>`|`Save(replyTo, data)`|Accepts a query parameter named `data` and stores it in memory|
|`GET http://localhost:8080/`|`Read(replyTo)`|Returns previously stored data|
|`DELETE http://localhost:8080/`|`Clear(replyTo)`|Deletes saved data from memory|

Note: type of `data` is always `String`

## Introduction

The way it's designed, you can alternatively send requests. i.e.
Once you post some data to the server, the server goes into `readonly` mode. In this
mode, you can only make a get request. If you attempt to post again, it will
return BAD REQUEST. Once you read the data, it will then again go into `writeonly` mode. In addition to these two operations, you can clear the stored data using a DELETE request. The delete message/request is accepted in both - readonly & writeonly states and does not toggle the state after clearing the data.

To achieve this functionality, we have created a finite state machine using akka-typed.

## Pattern

The pattern requires us to come up with a list of states.
Each state will have a corresponding akka `behavior`.

![image](images/Behaviors.png)

In addition, the pattern requires that we create an ADT of messages for every
state of the state machine. Since we are using typed-actors,
it allows us to ensure that we handle all messages which are
applicable to state of machine. If any message is missed,
a compiler warning is generated. In order to prevent `ask pattern`
from timing out, when a message is not handled, we need to ensure
that every message has a `replyTo` address. That's why we have 
placed `replyTo` in the `Message` trait.

![image](images/Messages.png)

Finally, we need to create a similar set of ADTs for responses and
make sure that `Unhandled` response extends from all other response
ADTs because we need the ability to return `Unhandled` response
from all states.

![image](images/Responses.png)

## Motivation

This pattern allows us to create Finite State Machines with akka typed actors with following design benefits:

1. The ask pattern will not time out if a message is not not handled by current behavior
1. This pattern makes it easy to look at message & response hierarchy and grok full high-level working of the state machine
without looking at logic
1. This pattern enforces handling of all messages required for a state. If a message is not handled,
compiler will generate warnings/errors
