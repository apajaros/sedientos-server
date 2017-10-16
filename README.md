# sedientos-server
Server for sedientos app. This project has three modules:
- Data: the model used in the other modules.
- Rest-api: a REST API built with Spring Mongo Data REST and Spring Security.
- Osm2mongo: a Spring Batch that exports data in Open Street Maps format to a mongoDB using our data model.

The REST API is hosted on Heroku at https://sedientos-server.herokuapp.com/api/ (please note it may take about a minute to answer if the dyno is sleeping). The documentation generated with Spring RESTdocs is available at https://sedientos-server.herokuapp.com/docs/index.html.
