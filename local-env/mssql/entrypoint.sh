
# create and populate database
function create_and_populate()
{
    envsubst < create_db.sql.template > create_db.sql

    # Create
    for i in {1..50};
    do
        echo "database: start setup database"
        /opt/mssql-tools/bin/sqlcmd -S localhost -U "${SA_USER}" -P "${SA_PASSWORD}" -d master -i create_db.sql
        if [ $? -eq 0 ]
        then
            echo "database: setup.sql completed"
            break
        else
            echo "not ready yet..."
            sleep 1
        fi
    done

}

create_and_populate &

# start sql server
/opt/mssql/bin/sqlservr 
