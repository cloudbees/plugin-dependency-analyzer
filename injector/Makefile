.PHONY: all clean docker

TAG=latest
IMAGE=$(USER)/injector:$(TAG)
PORT=8080

all: target/injector.jar

clean:
	@mvn clean
	@docker image rm -f $(IMAGE)
	@docker-compose rm -fvs

target/injector.jar:
	@mvn clean package -Dmaven.test.skip=true

docker: target/injector.jar
	@docker build -t $(IMAGE) --build-arg PORT=$(PORT) -f src/main/docker/Dockerfile $(CURDIR)
