# Ratpack Todo example

Simple Ratpack todo REST API inspired form [Daniel Hyun
](https://danhyun.github.io/2016-gr8confeu-rapid-ratpack-groovy/) example.

## Run

1- Run the `jooqCodegen` task:
```
gradle jooqCodegen
```

2- Run the app:
```
gradle run
```

3- CRUD Operations:

```
# Create a new task
curl -X POST -H 'Content-type: application/json' --data '{"title":"New Task"}' http://localhost:5050/

# Get all tasks
curl -X GET http://localhost:5050/

# Delete all tasks
curl -X DELETE http://localhost:5050
```
