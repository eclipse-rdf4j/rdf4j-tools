FROM tomcat:8.5-jre8-alpine
MAINTAINER Bart Hanssens (bart.hanssens@bosa.fgov.be)

ENV VERSION="2.2.4"
ENV JAVA_OPTS="-Dorg.eclipse.rdf4j.appdata.basedir=/var/rdf4j"

RUN cd /tmp && \
	wget -q "http://www.eclipse.org/downloads/download.php?file=/rdf4j/eclipse-rdf4j-${VERSION}-sdk.zip&r=1" -O /tmp/rdf4j.zip && \
	unzip -q /tmp/rdf4j.zip && \
	cp /tmp/eclipse-rdf4j-${VERSION}/war/*.war /usr/local/tomcat/webapps && \
	rm -rf /tmp/eclipse && \
	rm /tmp/rdf4j.zip && \
	mkdir -p /var/rdf4j

COPY web.xml /usr/local/tomcat/conf/web.xml

VOLUME /var/rdf4j

EXPOSE 8080

