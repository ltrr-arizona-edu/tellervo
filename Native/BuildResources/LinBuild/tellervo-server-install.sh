#!/bin/bash

# Set up the PostgreSQL apt repo, which provides PL/Java for Trixie.
sudo apt install -y postgresql-common ca-certificates
sudo /usr/share/postgresql-common/pgdg/apt.postgresql.org.sh

# Install dependencies
sudo apt update
sudo apt install -y apache2 postgresql-17 postgresql-contrib-17 postgresql-server-dev-17 postgresql-17-postgis-3 postgresql-17-postgis-3-scripts postgresql-17-pljava default-jdk php libapache2-mod-php php-pgsql php-mbstring php-xml php-zip php-curl php-common expect dialog

# Install Tellervo server
sudo apt install -y ./tellervo-server-2.0.deb
