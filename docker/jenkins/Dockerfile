FROM jenkins:latest

# Don't run in interactive mode 
ENV DEBIAN_FRONTEND noninteractive
 
USER root
 
RUN curl -sSL https://get.docker.com/ | sh \
    && rm -rf /var/lib/apt/lists/*

# Get plugins for jenkins (git, ...)
COPY plugins.txt /usr/share/jenkins/plugins.txt

# Get the original jenkins config
COPY config.xml /var/jenkins_home/jobs/computer-database/config.xml

RUN /usr/local/bin/plugins.sh /usr/share/jenkins/plugins.txt

# DinD environment
ENV DOCKER_HOST tcp://run-dind:2375