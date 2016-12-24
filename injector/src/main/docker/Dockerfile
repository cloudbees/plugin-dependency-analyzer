FROM openjdk:8u102-jre

ENV HOME /home/plugininjector
RUN groupadd -g 1000 plugininjector && \
    useradd -c "Plugin Dependency Analyzer Injector" -d ${HOME} -u 1000 -g 1000 -m plugininjector

ADD src/main/docker/docker-entrypoint.sh ${HOME}
RUN chmod 755 ${HOME}/docker-entrypoint.sh

WORKDIR $HOME
USER plugininjector

# Application profile to use
ENV SPRING_PROFILES_ACTIVE prod

EXPOSE 8080
ENTRYPOINT ["/home/plugininjector/docker-entrypoint.sh"]

ADD target/injector.jar ${HOME}/injector.jar
