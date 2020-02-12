# Mitchell International Software Development Engineering Internship Coding Challenge

Vehicle back-end for Mitchell coding challenge, implemented with Java Spring MVC and hosted on Heroku.

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

Deployment is automated with TravisCI to Heroku. To reference the build status, click the build icon above.

## Testing

Testing cases can be found in src/test/java/com/mitchell/challenge/vehicle/VehicleApplicationTests.java, testing is also
automated via TravisCI during deployment

## License

Licence for this project can be found at [LICENSE](LICENSE)

