# Plugin Dependency Analyzer Injector

## Build
### Jar
```
mvn clean verify
```

### Docker image
The docker image requires to have the `jar`, so build the JAR with Maven first.
```
docker build \
  -t ${USER}/plugin-dependency-analyzer-inject \
  -f src/main/docker/Dockerfile \
  .
```

## Run
The application requires a Neo4J database.
### Neo4J Database
```
docker run --name plugin-dependency-analyzer \
 -p 7474:7474 -p 7687:7687 \
 -e NEO4J_AUTH=neo4j/neo4j123 \
 -d \
  neo4j:3.1.0
```

### Docker image
```
docker run \
  -p 8080:8080 \
  -ti \
  -e NEO4J_URI=foobar \
  -e NEO4J_USERNAME=foobar \
  -e NEO4J_PASSWORD=foobar \
  ${USER}/plugin-dependency-analyzer-inject
```
