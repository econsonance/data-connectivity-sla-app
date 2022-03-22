# data-connectivity-sla (Console App)

## Description
This App is used to test a number of different storage providers using a target file or a specific connection string and driver option.

## Arguments
This App accepts three arguments:
1. `--file`: The path of a JSON file that contains the **Targets** to verify connectivity to (e.g. ~/target-1.json)
2. `--conn`: The connection string of the target (required if runconfig was not provided)
3. `--driver`: The driver identifier to use with the connection string, e.g. azblob (required if runconfig was not provided)

## Drivers
Represent the storage provider supported by this App (using JDBC).
The following providers are supported:
1. Postgres
2. SQL Server
3. MySQL
4. Azure Storage Account Blob
5. AWS S3
6. Google Storage
7. SAP
8. MongoDB
9. Snowflake
10. HTTP Endpoint



## Condition
The App can trigger an output based on one of the following status:
1. on-success: When the connectivity verification succeeds (default)
2. on-failure: When the connectivity verification fails 

## Output Type
This App currently produces one of two types of outputs:
1. stdout: An stdout/stderr for command line (default)
2. webhook: It triggers an HTTP request (more details below)

## Webhook Output
Webhook outputs need two arguments:
1. Method: The HTTP method used which can be any of the following values:
    a. get-querystring: It will create an **HTTP GET** request passing a querystring parameter *status* with value *SUCCESS* or *FAILED*
2. URL: The URL used to create the request for the output webhook.

# FINAL NOTE

The following drivers:
1. Azure Storage Account Blob
2. Google Storage

**do not support JDBC or S3 API** but rather offer a hadoop-like fashion to connect.

For those drivers we use code similar to: https://stackoverflow.com/questions/38624298/to-connect-to-hadoop-using-java to test the connectivity 