if [ $# -ne 1 ]
  then
    echo "Specify exactly one argument: the relative path of the database file to initialize"
    exit 1
fi
if [ -f $1 ]
  then
    echo "Database file already exists, manually remove it if you want to re-initialize"
    exit 1
fi

parent_path=$( cd "$(dirname "$0")" ; pwd -P )

sqlite3 $1 < "$parent_path"/db/init_queries.sql
