Wanderer
This project aims to be a travel companion where all enthusiastic travellers can find attractive and well-known places of attraction in their nearby locality with real-time map location. The core idea of this application is to multiply the traveller's community where new travellers can get a fresh perspective of places through the memories shared by the other zealous travellers.

Our main site is at
https://wanderer-live.herokuapp.com/login

Project Version
Java 11 or greater and Angular 13 or greater are recommended

Building the project
Maven is used as a project building tool and if you want to view the project in an IDE, it is best to first generate some of the source files; otherwise, you will get many compilation complaints in the IDE.
mvnw clean install

If you want to generate the packaged jar files from the source files, run the following maven command
mvnw package

Note: For IntelliJ IDE follow the steps described in the official document https://www.jetbrains.com/idea/guide/tutorials/creating-a-project-from-github/clone-from-github/

The project has a Test Coverage of more than 80%, if you don’t wish to run test cases while building, the following link will provide the dependency to be used for skipping the tests
https://www.baeldung.com/maven-skipping-tests

Run Angular
Run ng serve for a dev server. Navigate to http://localhost:4200/. The app will automatically reload if you change any of the source files.

Run ng generate component component-name to generate a new component. You can also use ng generate directive|pipe|service|class|guard|interface|enum|module

License
Project Wanderer is a part of the Final Project Submission under the course ‘Advanced Software Development Concepts’ at Dalhousie University for the batch January-2022. Using this project without any reference and citations will be considered as an Academic Integrity Offence at the University.

The following link should be used for references and citations https://git.cs.dal.ca/courses/2022-winter/csci-5308/group21
