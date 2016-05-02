# Image source
FROM java:openjdk-8-jdk

# Maven version
ENV MAVEN_VERSION 3.3.9

# Maven installation
RUN mkdir -p /usr/share/maven \
  && curl -fsSL http://apache.osuosl.org/maven/maven-3/$MAVEN_VERSION/binaries/apache-maven-$MAVEN_VERSION-bin.tar.gz \
    | tar -xzC /usr/share/maven --strip-components=1 \
  && ln -s /usr/share/maven/bin/mvn /usr/bin/mvn

# Maven home definition
ENV MAVEN_HOME /usr/share/maven

# Copy project in the workdir
COPY . /usr/app/

# Database properties for docker
RUN cp /usr/app/docker/mysql/dbconf.properties /usr/app/src/test/resources/
RUN cp /usr/app/docker/mysql/dbconf.properties /usr/app/src/main/resources/

# Workdir definition
WORKDIR /usr/app/