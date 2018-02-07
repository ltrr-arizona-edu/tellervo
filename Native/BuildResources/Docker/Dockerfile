#FROM ubuntu:16.04
FROM FROM mmunro/bloatstack:0.1.2-xenial 
MAINTAINER p.brewer@ltrr.arizona.edu
LABEL org.tridas.version="1.3.3"
LABEL org.tridas.release-date="2017-02-01"

# Ensure terminal is set to non interactive
RUN echo 'debconf debconf/frontend select Noninteractive' | debconf-set-selections

# Download and install dependencies

#RUN apt-get update && apt-get install -y --no-install-recommends \
#apt-utils \
#expect \
#supervisor \ 
wget \
#iputils-ping \
#iproute2 \
#sudo \
#&& rm -r /var/lib/apt/lists/*


RUN adduser --disabled-password --gecos '' tellervo
RUN adduser tellervo sudo
RUN echo '%sudo ALL=(ALL) NOPASSWD:ALL' >> /etc/sudoers
USER tellervo


RUN sudo apt-get update && sudo apt-get install -y --no-install-recommends \
openjdk-8-jdk \ 
apache2 \ 
openssh-client \
postgresql-9.5 \ 
postgresql-common \
postgresql-contrib-9.5 \
postgresql-server-dev-9.5 \
postgresql-9.5-postgis-scripts \
postgis \
php \
php-pgsql \
php7.0-xml \
php7.0-zip \
php-curl \
php7.0-mbstring \
php-common \
libapache2-mod-php \
&& sudo rm -r /var/lib/apt/lists/*


# Download and install Tellervo Server
RUN wget   --directory-prefix=/tmp/ http://www.tellervo.org/tmp/tellervo-server-1.3.3.deb \
    && sudo dpkg --install /tmp/tellervo-server-1.3.3.deb \
    && sudo tellervo-server --auto-configure

USER root

ENV SERVER_NAME=tellervo 
RUN echo 'ServerName ${SERVER_NAME}' >> /etc/apache2/conf-enabled/servername.conf



# Setup Supervisord to run Apache and Postgres

COPY supervisord.conf /etc/supervisor/conf.d/supervisord.conf
COPY reinstall-tellervo /usr/bin/
RUN chmod 0755 /usr/bin/reinstall-tellervo


# Ports to expose
EXPOSE 80 

WORKDIR /var/www/tellervo
VOLUME /var/lib/postgresql/data

CMD ["/usr/bin/supervisord"]



