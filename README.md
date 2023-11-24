# students-database

## A4Q1 Students Database Interaction

* Youtube video: https://youtu.be/PzaqPamRVdc
 
This is a Java application using JDBC connection to the PostgreSQL database, allowing users to perform CRUD applications on the command line interface.

From a students table, users can retrieve all students, add a student, update the email address and delete a student.

This program includes four different classes
* DatabaseCredentials: this is a helper class containing database credentials for thee JDBC connection to be established by inherited classes.
* DatabaseSetup: this class creates the students table and inserts initial data.
* Application: this is the main program allowing users to perform the different functionalities.
* TableDrop: drops the students table from the database.
---

### Set-up and compilation instructions 

1. On `DatabaseCredentials.java`, assign your database credentials to the PostgreSQL database variables. This will be inherited by the `DatabaseSetup` and `Application` classes.

2. You may set up the JDBC Driver (https://jdbc.postgresql.org/documentation/setup/).

3. Compile the program on your shell.
```bash
javac *.java
```
4. To set up the database and create the students table, run the program with
```bash
java -cp postgresql-42.7.0.jar: DatabaseSetup
```
However, if you have already installed the JDBC Driver (with the JAR archive included in the class path), you may skip the classpath option:
```bash
java DatabaseSetup
```

This step creates the `students` table in the database and inserts the initial data.

---

### Running the program

4. To run the main application, run the following command
```bash
java -cp postgresql-42.7.0.jar: Application
# or if you have the JAR file included in the classpath:
java Application
```
The users will be provided with an explanation of the program and will be prompted to choose one of four options by entering entering a number:

(1) Displaying students (`getAllStudents`)

(2) Adding a new student (`addStudent`)

(3) Updating an email address (`updateStudentEmail`)

(4) Deleting a student (`deleteStudent`)

Each number is associated with each function in the Assignment instruction. The first option (1) will retrieve all student record. The second option (2) will prompt users to enter a first name, last name, email address and enrollment date for the student to be added. The third option (3) will prompt users to enter the student ID of the student they'd like to modify the email and the new email address. The fourth option (4) will prompt users to enter the student ID of the student they would like to delete from the records.

After each operation, users will be provided with a prompt with an option to perform another action.

5. Optional: To drop the students table, run the following command
```bash
java -cp postgresql-42.7.0.jar: TableDrop
# or
java TableDrop
```
