# Job Management System

## High-Level System Design

The Job Management System is designed to handle the execution of various types of jobs. Each job can be in one of four states: `QUEUED`, `RUNNING`, `SUCCESS`, or `FAILED`. The system is flexible enough to allow new types of jobs to be added without the need for extensive redevelopment. It supports functionality such as creating, deleting, querying, retrying jobs, and scheduling them based on priorities and predefined schedules.

### Key Components:

- **Job**: Represents the core entity with details such as type, state, priority, and scheduled time.
- **JobState Enum**: The state of a job, which can be `QUEUED`, `RUNNING`, `SUCCESS`, or `FAILED`.
- **JobService**: A service layer to manage the creation, deletion, state updates, and querying of jobs.
- **JobController**: An API controller to handle HTTP requests for creating, updating, deleting, and querying jobs.
- **Database**: An in-memory H2 database is used to store jobs and their states.

---

## How to Build and Run Locally

### Prerequisites:

- Java 17
- Maven 3.9.x

### Steps:

1. **Clone the Repository:**

   ```bash
   git clone https://github.com/markyasser/job-management.git
   cd job-management
   ```

2. **Build the Project:**

   ```bash
    mvn clean install
   ```

3. **Run the Application:**

   ```bash
   mvn spring-boot:run
   ```

4. **Create configuration file:**

   Create a file named `application.properties` in the `src/main/resources` directory and add the following content:

   ```properties
   spring.application.name=
   spring.datasource.url=
   spring.datasource.driver-class-name=
   spring.datasource.username=
   spring.datasource.password=
   spring.jpa.hibernate.ddl-auto=
   ```

5. **Access the Application:**
   Access the API throw `http://localhost:8080/{END_POINT}`

## How to Run the Test Suite

To run the test suite and generate coverage report for the Job Management System, you can use the following command:

```bash
mvn clean test jacoco:report
```

This will run all the tests and generate a coverage report in the `target/site/jacoco` directory.
Access the `index.html` file to view the coverage report.

## Swagger API documentation

API documentation is available at `http://localhost:8080/swagger-ui.html`

## Assumptions and Shortcuts

1. **Assumption about Job Types**: The system doesn't validate the type of the job. It's assumed that the job types are handled externally, and that each job type will be supported by adding the necessary logic without changing the core system.

2. **In-Memory Database**: The system uses an in-memory H2 database for simplicity. In a production environment, a more robust database like PostgreSQL or MySQL should be used.

## Technical Debt

1. **Job Execution Logic**: The job execution logic is not fully implemented. The system only tracks state changes but does not actually perform any actions for jobs. This will require an additional execution engine.

2. **In-Memory Database**: The in-memory H2 database is not suitable for production use. A persistent database like PostgreSQL or MySQL should be used.

## Deployment Options

1. **Cloud Deployment**: The application can be deployed on cloud platforms like AWS, Azure, or Google Cloud Platform for scalability, reliability, and ease of management.

2. **Docker**: The application can be containerized using Docker to simplify deployment, scaling and to ensure consistency across different environments. with the following command:

```bash
sudo docker-compose up -d --build
```

3. **Kubernetes**: The application can be deployed on Kubernetes for container orchestration, scaling, and management.

4. **CI/CD Pipeline**: Implementing a CI/CD pipeline using tools like Jenkins or GitHub Actions can automate the build, test, and deployment process.

An already deployed version of the application can be accessed at Azure [http://devtest.switzerlandnorth.cloudapp.azure.com:8080/swagger-ui.html](http://devtest.switzerlandnorth.cloudapp.azure.com:8080/swagger-ui.html)
