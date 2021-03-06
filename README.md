# Mitchell International Software Development Engineering Internship Coding Challenge

[![Build Status](https://travis-ci.com/paulpan05/Mitchell-vehicle-challenge.svg?branch=master)](https://travis-ci.com/paulpan05/Mitchell-vehicle-challenge)

Developed by: Junhong Pan (Paul)

Vehicle back-end for Mitchell coding challenge, implemented with Java Spring MVC and hosted on Heroku.

## Project Structure
 - src
    - main
        - java/com.mitchell.challenge.vehicle
            - Vehicle.java: the vehicle model class file which database table is build from.
            - VehicleApplication.java: The file which the web service is run off of.
            - VehicleController.java: The controller class defining REST endpoints.
            - VehicleRepository.java: The data access class defining calls to H2 database.
            - VehicleService.java: The service class which handles errors for requests and calls repository for data.
        - resources/application.properties: Datasource configuration for H2 runtime in-memory database.
    - test
        - java/com.mitchell.challenge.vehicle
            - VehicleApplicationTests.java: The file for all unit tests of the application.
        - resources/application.properties: Datasource configuration for H2 runtime in-memory database in test
        environment.


## Requested Features
These required features are implemented:
 - [x] Usage of either C# or Java.
 - [x] Some form of automated testing.
 - [x] Some form of in-memory persistence of created vehicle objects.

Additionally, there are optional features which are implemented:
 - [x] Add validation to your service.
    - [x] Vehicles must have a non-null / non-empty make and model specified, and the year must be between
      1950 and 2050.
 - [x] Add filtering to your service.
    - [x] The GET vehicles route should support filtering vehicles based on one or more vehicle properties. (EX:
          retrieving all vehicles where the ‘Make’ is ‘Toyota’)
 - [ ] Write an example client for your service. (Did not have enough time for this one and decided to improve upon
 backend implementation)

## Implementation

The application is implemented with Spring MVC Java Framework. A driver is embedded in the program which summons the H2
in-memory database when a call to an endpoint results in update of data.

The list of valid routes are:
 - https://mitchell-vehicle-challenge.herokuapp.com/vehicles
    - GET: Gets list of vehicles in the database. Optional filtering request parameters can be supplied.
        - **year:** filter list of vehicles by year
        - **make:** filter list of vehicles by make
        - **model:** filter list of vehicles my model
        - If multiple filters are provided, all vehicles matching any one of the filters will be returned.
    - POST: Creates a vehicle in the database, request body must be in the form {id, year, make, model}
    - PUT: Updates the vehicle with the specific id in the database - Note: the request body must have id as one of the
    keys, along with optional year, make, and model keys if update to those are to be performed.
 - https://mitchell-vehicle-challenge.herokuapp.com/vehicles/{id}
    - GET: Gets the vehicle with the specific id in the database
    - DELETE: Deletes the specific vehicle with a certain id in the database
    
## Deployment

Deployment is automated with TravisCI to Heroku. For TravisCI deployment status, check the badge on the top of README

## Testing

Testing cases can be found in src/test/java/com/mitchell/challenge/vehicle/VehicleApplicationTests.java, testing is also
automated via TravisCI during deployment

## License

Licence for this project can be found at [LICENSE](LICENSE)

