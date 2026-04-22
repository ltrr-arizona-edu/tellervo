#!/bin/bash

# Install postgres server dev 
sudo apt install postgresql-server-dev-all

# Set up postgres apt repo
sudo /usr/share/postgresql-common/pgdg/apt.postgresql.org.sh

# Install remaining dependencies
sudo apt update
sudo apt install apache2 postgresql postgresql-contrib postgresql-server-dev-all default-jdk postgresql-postgis php libapache2-mod-php php-pgsql php-mbstring php-xml php-zip php-curl php-common expect dialog

# Install Tellervo server
sudo dpkg --install tellervo-server-2.0.deb
