FROM openjdk:8-jre-alpine

RUN apk add --update --no-cache curl tini
RUN adduser -h /var/lib/plugin-dependency-analyzer -D -u 1000 plugin-dependency-analyzer plugin-dependency-analyzer

USER plugin-dependency-analyzer

ENV SPRING_PROFILES_ACTIVE production
ARG PORT=8080
EXPOSE ${PORT}
ENV PORT ${PORT}

ENTRYPOINT ["/sbin/tini", "-g", "--"]
CMD ["/var/lib/plugin-dependency-analyzer/docker-entrypoint.sh"]
HEALTHCHECK --interval=15s --timeout=2s --retries=3 \
  CMD /var/lib/plugin-dependency-analyzer/docker-healthcheck.sh ${PORT}

COPY --chown=plugin-dependency-analyzer:plugin-dependency-analyzer src/main/docker/*.sh target/plugin-dependency-analyzer.jar /var/lib/plugin-dependency-analyzer/
