FROM ubuntu:16.04
MAINTAINER p.brewer@ltrr.arizona.edu
LABEL org.tridas.version="1.3.3"
LABEL org.tridas.release-date="2018-02-07"

ENV SERVER_NAME=tellervo

# Ensure terminal is set to non interactivei
RUN echo 'debconf debconf/frontend select Noninteractive' | debconf-set-selections

# Install dependencies
RUN set -e \
  && apt-get update \
  && apt-get install -y \
    apache2 \
    expect \
    iputils-ping \
    libapache2-mod-php \
    openjdk-8-jdk \
    php \
    php-curl \
    php-pgsql \
    php7.0-mbstring \
    php7.0-xml \
    php7.0-zip \
    postgis \
    postgresql-server-dev-9.5 \
    sudo \
    supervisor \
    wget \
  && rm -r /var/lib/apt/lists/* \
  && adduser --disabled-password --gecos '' tellervo \
  && adduser tellervo sudo \
  && echo '%sudo ALL=(ALL) NOPASSWD:ALL' >> /etc/sudoers \
  && echo "ServerName ${SERVER_NAME}" >> /etc/apache2/conf-enabled/servername.conf \
  && wget --directory-prefix=/tmp/ http://www.tellervo.org/tmp/tellervo-server-1.3.3.deb \
  && dpkg --install /tmp/tellervo-server-1.3.3.deb \
  && tellervo-server --auto-configure

# Setup Supervisord to run Apache and Postgres
COPY supervisord.conf /etc/supervisor/conf.d/supervisord.conf

# Ports to expose
EXPOSE 80

WORKDIR /var/www/tellervo
VOLUME /var/lib/postgresql/data

CMD ["/usr/bin/supervisord"]
