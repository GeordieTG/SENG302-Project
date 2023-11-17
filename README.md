# SENG302 Project Team 400
basic project using ```gradle```, ```Spring Boot```, ```Thymeleaf```, and ```GitLab CI```.

## Author
> - Phyu Wai Lwin
> - Geordie Gibson
> - Liam Cuthbert
> - Mustapha Conteh
> - Raith Fullam
> - Nathan Fronda
> - Daniel Neal
> - Natalie Kim


## Dependencies
>This project requires Java version >= 17, click here to get the latest stable OpenJDK release (as of time of writing)

## Technologies
> This project makes use of several technologies. Here are some helpful links to documentation/resources for them:
>
> - [Spring Boot](https://spring.io/projects/spring-boot) - Used to provide http server functionality
> - [Spring Data JPA](https://spring.io/projects/spring-data-jpa)- Used to implement JPA (Java Persistence API) repositories
> - [h2](https://www.h2database.com/html/main.html) - Used as an SQL JDBC and embedded database
> - [Thymeleaf](https://www.thymeleaf.org/) - A templating engine to render HTML on the server, as opposed to a separate client-side application (such as React)
> - [Gradle](https://gradle.org/) - A build tool that greatly simplifies getting application up and running, even managing our dependencies (for those who did SENG202, you can think of Gradle as a Maven replacement)
> - [Spring Boot Gradle Plugin ](https://docs.spring.io/spring-boot/docs/3.0.2/gradle-plugin/reference/htmlsingle/)- Allows us to more easily integrate our Spring Boot application with Gradle

## Importing project from VCS (Using Intellij)
>IntelliJ has built-in support for importing projects directly from Version Control Systems (VCS) like GitLab.
To download and import your project:
> - Launch IntelliJ and chose Get from VCS from the start-up window.
> - Input the url of the project https://eng-git.canterbury.ac.nz/seng302-2023/team-c.git


## How to run
### 1 - Running the project
From the root directory ...

On Linux:
```
./gradlew bootRun
```

On Windows:
```
gradlew bootRun
```

By default, the application will run on local port 8080 [http://localhost:8080](http://localhost:8080)

## Default Users

If you want to check functionality where email confirmation is required, you will need to make your own account with an email account that you are able to check.

username: test1@email.com
password: *****

username: test2@email.com
password: *****

username: test3@email.com
password: *****

and so on... We have test emails all the way up to test12@email.com all with the same password of five astrics

## How to run tests
> There are no special requirements to run the tests. Simply right-click the "test" folder (./src/test) and click "Run 'Tests in 'tab.test'"


## Contributors

- SENG302 teaching team
- Team-400 Authors (See Author)


## License

MIT License

Copyright (c) 2023 seng302-2023

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.



## References

- [Spring Boot Docs](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/)
- [Spring JPA docs](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/)
- [Thymeleaf Docs](https://www.thymeleaf.org/documentation.html)
- [Learn resources](https://learn.canterbury.ac.nz/course/view.php?id=17797&section=8)
