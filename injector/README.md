# Plugin Dependency Analyzer Injector

## Build
```
mvn clean verify
```

## Run
### Neo4J Database

```
docker run --name neo4j-pda \
 -p 7474:7474 -p 7687:7687 \
 -e NEO4J_AUTH=neo4j/neo4j123 \
 -d \
  neo4j:3.1.0
```
