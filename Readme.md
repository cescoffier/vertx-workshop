# vert.x micro-service workshop

In this workshop, you are going to develop a micro-service application called _vert2go_. This workshop covers:

* how to build micro-services with vert.x
* how you can use the event bus to bind your micro-services
* how to consume a REST API
* how to build an application using several persistence technologies
* how to provide a REST API, and build proxies
* how to deploy some micro-service to Openshift
* how to centralize your logs
* how to deploy a vert.x micro-service in a docker container
* how to monitor your application

The _vert2go_ application is a recommendation application where users can rates the place they like.

The slides are available on: http://devoxx0workshop0slides-vertxdemos.rhcloud.com/#/

**Prerequisites:**

* Java 8 (JDK)
* Git
* Apache Maven
* Docker (or docker-machine)
* An IDE
* RoboMongo (optional)


## Using the provided virtual box image

We provide a virtual box image with everything you need on it.
 
1. Install Oracle virtual box from https://www.virtualbox.org/wiki/Downloads. Installation details are on https://www.virtualbox.org/manual/ch02.html
2. Retrieve the virtual box image (`vertx-workshop.ova`).
3. Open virtual box and import the _appliance_ (File -> Import Appliance)
4. Select the `vertx-workshop.ova` file and click on import on the next screen
5. Wait....
6. Start the virtual machine by selecting it and then clic on the _Start_ button (green arrow)
  
## Using docker machine

Docker runs natively on Linux. Because the Docker daemon uses Linux-specific kernel features, you can’t run Docker 
natively in OS X or Windows. Instead, you must use docker-machine to create and attach to a virtual machine (VM). This
 machine is a Linux VM that hosts Docker for you on your Mac or Windows. If you are on Mac OS X or Windows, you can use 
 docker via docker machine (https://docs.docker.com/machine/).

1. Install docker and docker-machine from https://www.docker.com/docker-toolbox. Installation instructions are there: http://docs.docker.com/mac/step_one/
2. Once done run `docker run hello-world` to verify your installation 
  
The installation process will create a VM that has a minimal linux to run docker.
  
  




