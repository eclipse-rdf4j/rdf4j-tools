# Eclipse RDF4J server and workbench

Dockerfile for RDF4J server and workbench, based on the Tomcat 8.5 Alpine image.

A slightly modified web.mxl is used to fix a known UTF-8 issue (see also http://docs.rdf4j.org/server-workbench-console)

## Port

By default port 8080 is exposed.

## Volume

The Dockerfile provides two volumes:
 
  * RDF4J data will be stored `/var/rdf4j`
  * Tomcat server logs in `/usr/local/tomcat/logs`

## Building a docker images

The RDF4J version can be selected by setting the VERSION build argument
(default version is 2.2.4).

Example:
```
docker build --build-arg VERSION=2.2.4 -t rdf4j .
```

