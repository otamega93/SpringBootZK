# SpringBootZK
POC based on a forked repo which tweaked ZK into Spring Boot. It consumes the /gems endpoint from my gem-backend project (POST, PUT, DELETE, GET).

It runs on localhost:8080, uses Spring Boot and consumes the url especified in the GemService.

It has a beans tweak so it can actually read and manipulate resources from an HATEOAS rich response (it's used in the findAll method). The rest use a regular restTemplate without any tweaks.

It has some experiments with the ZK framework as for to be become a kinda nice POC.

The language of the entire framework can be change in the file zk.xml (changing en for es, for example in the case you need to use spanish).

The window to use can be accessed by going to localhost:8080/gems. This will trigger the Spring controller and will lead to the gem.zul view.
