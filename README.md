# Employee Management System API

The Employee Managment System (EMS) is a system for tracking which employees have worked against which clients for a business.

This project consistutes the API for the EMS allowing users to.

  - Create business and retrieve businesses.
  - Create and retrieve user accounts in the form of employees.
  - Login to user accounts, generating a session key.
  - Create and retrieve clients with location boundaries.
  - Create and retrieve worklogs with location and time data.

### Apache Tomcat 8
This API was designed to run on an apache tomcat 8 instance. To run it you will need to start up an apache tomcat 8 instance and deploy the project onto it. Most IDEs will support this, see the specific IDE documentation. This can also be done manually guidance can be found at [codejava](http://www.codejava.net/servers/tomcat/how-to-deploy-a-java-web-application-on-tomcat) on how to do this.

### JAX-RS and Jersey
The EMS API is powered by JAX-RS and Jersey implementatons allowing for effortless sending and retrieval of our beans.

URLs are built like follows:
  - /buisness
  - /business/<your business here>
  - /business/<your business here>/employee/<username>
  - /business/cibusinesstag/employee/mostWorked/top/1/10/between/10/20

### MariaDB Connector/J and MySQL
The API consists of a custom query running implementation to handle trival SQL queries and transactions to a configurable endpoint. This makes use of the inbuilt apache tomcat database pooling system to keep connections efficent.

MariaDB Connector/J fully support MySQL and enables us to create secure SSL connections with ease.

# Tech

The EMS API uses a number of third party dependencies to work properly:

* [MariaDB Connector/J](https://mariadb.com/kb/en/library/about-mariadb-connector-j/) - fully supported MySQL driver.
* [JUnit4](http://junit.org/junit4/) - simple framework for unit testing frameworks.
* [Log4J](https://logging.apache.org/log4j/2.x/) - loggin made easy.
* [JAX-RS](https://github.com/jax-rs) - java EE restful web services.
* [Glassfish Jersey](https://jersey.github.io/) - restful web services for java.
* [Apache Tomcat 8+](https://tomcat.apache.org/) -  java servlet, javaServer pages, java expression language and java webSocket technologies.
* [Apache Maven](https://maven.apache.org/) - software project management and comprehension tool.
* [Docker](https://www.docker.com/) - container management system.
* [Yaml](http://yaml.org/) - human-readable data serialization language.

# Kubernetes + Google Cloud Compute

EMS API is designed to run on Kubernetes on the Google's Cloud Compute Engine. This allows us all the benefits of the cloud, such as; scalability, availablity, and fault tolerance.

See [KUBERNETES.md](https://github.com/joemccann/dillinger/blob/master/KUBERNETES.md)