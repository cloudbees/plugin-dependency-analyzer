.PHONY: all clean docker

TAG=latest
IMAGE=$(USER)/plugin-dependency-analyzer:$(TAG)
PORT=8080

all: target/plugin-dependency-analyzer.jar

clean:
	@mvn clean
	@docker image rm -f $(IMAGE)
	@docker-compose rm -fvs

target/plugin-dependency-analyzer.jar:
	@mvn clean package -Dmaven.test.skip=true

docker: target/plugin-dependency-analyzer.jar
	@docker build -t $(IMAGE) --build-arg PORT=$(PORT) -f src/main/docker/Dockerfile $(CURDIR)

run: target/plugin-dependency-analyzer.jar
	@#docker-compose up --quiet-pull --abort-on-container-exit --remove-orphans
	@docker run --rm -d --name plugins-database -e NEO4J_AUTH=neo4j/foobar -p 7474:7474 -p 7687:7687 neo4j:3.3.4
	@NEO4J_PASSWORD=foobar mvn spring-boot:run
