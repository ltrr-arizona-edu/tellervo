#!/bin/bash

# Install postgres server dev 
sudo apt install postgresql-server-dev-14

# Set up postgres apt repo
sudo /usr/share/postgresql-common/pgdg/apt.postgresql.org.sh

# Install remaining dependencies
sudo apt update
sudo apt install apache2 postgresql-14 postgresql-contrib-14 postgresql-server-dev-14 openjdk-11-jdk postgresql-14-pljava postgresql-14-postgis-3 php libapache2-mod-php php-pgsql php-mbstring php-xml php-zip php-curl php-common expect

# Install Tellervo server
sudo dpkg --install tellervo-server-2.0.deb

