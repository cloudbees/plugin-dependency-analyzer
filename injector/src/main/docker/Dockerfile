FROM openjdk:8-jre-alpine

RUN apk add --update --no-cache curl tini
RUN adduser -h /var/lib/injector -D -u 1000 injector injector

USER injector

ENV SPRING_PROFILES_ACTIVE production
ARG PORT=8080
EXPOSE ${PORT}
ENV PORT ${PORT}

ENTRYPOINT ["/sbin/tini", "-g", "--"]
CMD ["/var/lib/injector/docker-entrypoint.sh"]
HEALTHCHECK --interval=15s --timeout=2s --retries=3 \
  CMD /var/lib/injector/docker-healthcheck.sh ${PORT}

COPY --chown=injector:injector src/main/docker/*.sh target/injector.jar /var/lib/injector/