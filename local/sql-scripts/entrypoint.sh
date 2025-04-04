#!/bin/sh

echo "Starting SQL Server..."
/opt/mssql/bin/sqlservr &

# Wait for SQL Server to start
sleep 20

echo "Running database initialization script..."
/opt/mssql-tools18/bin/sqlcmd -C -S localhost -U sa -P "$SA_PASSWORD" -d master -i /var/opt/mssql/scripts/init-db.sql

# Keep SQL Server running
wait $!
