### How to use this spring-boot project

- Install packages with `mvn package`
- Run `mvn spring-boot:run` for starting the application (or use your IDE)

Application (with the embedded H2 database) is ready to be used ! You can access the url below for testing it :

- Swagger UI : http://localhost:8080/swagger-ui.html
- H2 UI : http://localhost:8080/h2-console

> Don't forget to set the `JDBC URL` value as `jdbc:h2:mem:testdb` for H2 UI.


## Contributions
- Add tests
  - Added tests for AuthControllers and Employee Controllers
  - used mockmvc and mockito libraries for mocking http requests and objects respectively
  - The code coverage for AuthControllers and Employee Controllers is 100%

- Change syntax
  - updated syntax and resolved bugs
  - added proper response entities as responses in all teh controllers since it was missing in most of them

- Protect controller end points
  - Added JWT auth and Spring Security based authentication to protect the controller endpoints
  - configured endpoints to be able to only accessible when a bearer token is sent with the request
  - created 2 new endpoints and opened them without authorization to signup and authenticate
  - the /signup endpoint registers a new user with give username and password
  - the /authenticate endpoint takes username and password and generated a JWT and sends it back as a response
  - For authentication purposes I also used a passwordEncoder which is standard practice
  - this token now can be used to send requests to our employee endpoints
  - for authentication I created several classes
    - configured websecurity and denied requests without authorization
    - created user repository and user model which will be used for authentication
    - implemented Spring security User details and User Details Service which helps in Authenticating incoming requests

- Exception Handling
  - Added Exception Handling separately in a Controller Advice
  - Added lots of cases to handle bad requests
  - We can easily add more exceptions if we want to

- Bug fixing and errors
  - Initally the code contained a lot of bugs like missing @RequestBody params, incorrectly saving employee, creating duplicate employees while saving with same name. For these issues I updated the code and handled all the errors.
  - I tried to add as much Standard praactices as I can like keeping Controller-service-repo format, creating service and serviceImpl classes, not creating unnecessary temporary objects which would take up memory.

- Caching for Database
  - I also added Caching which will decrease the load on Database.
  - Read about caching in SpringBoot and how it works and used @Cacheable @CachePut @CacheEvict properly to handle deletion and updation in smoothly without any errors.
  - we can also add caching for UserRepository but since it was not being used for this example so I left it.

- What would I have done if I had more time.
  - I am currently working on documentation since it takes time so I left this part for the end.
  - I was thinking of shifting to Postgres or MySQL database, So maybe I can change the database url to a self hosted MySQL server. Within code there will be no change since SpringDataJpa handles everything and I just need to provide the Mysql dialect in properties file.
  - I would have added role based auth as well if I would have got more time.
  - Finally I am working on writing a Dockerfile so that an instance of it can be easily run by just running "Docker run myapp -p 8080:8080" my-app-image"
  - I need to remove the silly "System.out.println()" sattements which I wrote for some debugging purpose.

### Instructions

- download the zip file of this project
- create a repository in your own github named 'java-challenge'
- clone your repository in a folder on your machine
- extract the zip file in this folder
- commit and push

- Enhance the code in any ways you can see, you are free! Some possibilities:
  - Add tests
  - Change syntax
  - Protect controller end points
  - Add caching logic for database calls
  - Improve doc and comments
  - Fix any bug you might find
- Edit readme.md and add any comments. It can be about what you did, what you would have done if you had more time, etc.
- Send us the link of your repository.

#### Restrictions
- use java 8


#### What we will look for
- Readability of your code
- Documentation
- Comments in your code 
- Appropriate usage of spring boot
- Appropriate usage of packages
- Is the application running as expected
- No performance issues

#### Your experience in Java

Please let us know more about your Java experience in a few sentences. For example:

- I have 3 years experience in Java and I started to use Spring Boot from last year
- I'm a beginner and just recently learned Spring Boot
- I know Spring Boot very well and have been using it for many years
