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

run: docker
	@docker-compose up --quiet-pull --abort-on-container-exit --remove-orphans
