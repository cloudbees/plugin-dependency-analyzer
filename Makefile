.PHONY: all clean docker

TAG=latest
IMAGE=$(USER)/plugin-dependency-analyzer:$(TAG)
PORT=8080

all: target/plugin-dependency-analyzer.jar

clean:
	@mvn clean

target/plugin-dependency-analyzer.jar:
	@mvn clean package -Dmaven.test.skip=true

docker: target/plugin-dependency-analyzer.jar
	@docker build -t $(IMAGE) --build-arg PORT=$(PORT) -f src/main/docker/Dockerfile $(CURDIR)

run: docker
	@docker run --rm -d \
	  --name plugins-database \
		--env-file .docker-env \
		-p 7474:7474 -p 7687:7687 neo4j:3.4.1
	@docker run --rm  -ti \
	  --env-file .docker-env \
	  -v ${HOME}/.cache/cloudbees-support/plugins:/var/lib/plugin-dependency-analyzer/.cache/cloudbees-support/plugins:ro \
	  -p $(PORT):$(PORT) $(IMAGE)
