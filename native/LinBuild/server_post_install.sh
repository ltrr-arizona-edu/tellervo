#!/bin/bash
echo "***** Creating Corina database *****"
su - postgres -c "psql --file=/usr/share/corina-server/dummy.sql"

echo "***** Setting up Apache webserver for Corina *****"
ln /usr/share/corina-server/corina-apache /etc/apache2/sites-enabled/
/etc/init.d/apache2 restart