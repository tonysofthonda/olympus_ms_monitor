
# Monitor Microservice  

This module is a simple **FTP Folder Check**. Its purpose is to review via a scheduller job if **files** are available in a folder in **MFTP Server** and notify to **ms.trasnferfile** of the availability of such file.  

This application exposes 1 single endpoints (as a RESTFul service) that check the health of srevice, once the application has started a shceduller starts running with a customizable parameter of timelapse.  


## How it works

1. The project includes a properties file  (**application.properties**), with the entries:  
   `monitor.timelapse: To indicate the host to use to send emails notifications`
   `mftp.credentials.host, mftp.credentials.port, mftp.credentials.user, mftp.credentials.pass: To indicate user credentials for MFTP server`
   `monitor.workdir.inbound: To indicate the source folder in witch scheduller will be reading the files`

2. On a daily basis, the module runs a scheduller customizable job that perform the next:  
     
3. Perform a conecction to a MFTP server with the provided host & credentials  
   
4. If the shceduller finds one or more files, this will select the **First File**, the one with the newest date in the customatizable folder.

5. At the end, **ms.transferfile** will be called sending the next information: 

{
    "status": 1,
    "msg":"SUCCESS",
    "file": "{fileName}"
}


## Tools  

+ Java v1.8.0_202
+ Maven v3.8.6
+ Spring Boot v2.6.14
+ JUnit v5.8.2 with AssertJ v3.21.0
+ Lombok v1.18.24
+ Logback v1.2.11


## Run the app

Obtaining the application's Jar file  
`$ mvn clean install`  
  
Running the project as an executable JAR  
`$ mvn spring-boot:run`  

Running the tests  
`$ mvn test`  


## Usage

### 1. Service Health check endpoint
#### Request
`GET /olympus/monitor/v1/health`

    curl -i -X GET -H http://{server-domain|ip}/olympus/monitor/health

#### Response
    HTTP/1.1 200 OK
    Content-Type: application/json
    Transfer-Encoding: chunked
    Date: Mon, 22 May 2023 05:00:55 GMT
    
   Honda Olympus [name: ms.monitor] [version: 1.0.2] [profile: dev] 2023-05-22T05:00:55 America/Mexico_City

### 2. Manually run service once
#### Request
`POST /olympus/logevent/v1/event`

    curl -i -X POST -H 'Content-Type: application/json' -d '{"source": , "status": "X", "msg": "Succes process", "file": "fexampleFile.txt"}' http://{server-domain|ip}/olympus/monitor/health

#### Response
    HTTP/1.1 200 OK
    Content-Type: application/json
    Transfer-Encoding: chunked
    Date: Mon, 15 May 2023 05:00:55 GMT
    
    {
        "timestamp": "2023-05-17T12:25:28.062-05:00",
        "status": 400,
        "errors": [
            "source is mandatory"
        ]
    }
