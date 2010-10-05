#!/bin/bash
echo "***** Creating Corina database *****"

echo "Would you like to create a blank Corina database?"
echo "Note that if an existing database exists it will be deleted!"
read -p "   (y/n)  "
if [ "$REPLY" == "y" ]; then
	su - postgres -c "psql --file=/usr/share/corina-server/set_database_permission.sql"
	su - postgres -c "psql --file=/usr/share/corina-server/corina_template.sql"
fi



echo "***** Setting up Apache webserver for Corina *****"

echo "Would you like the Apache webservice to be configured automatically?"
read -p "   (y/n)  "
if [ "$REPLY" == "y" ]; then
	ln /usr/share/corina-server/corina-apache /etc/apache2/sites-enabled/
fi
/etc/init.d/apache2 restart