step 1: create gradle structure this project in current directory using spring boot bom with actuator, devtools and test dependencies

step 2: update the build.grade to add google-java-format integrated into build process and convenience manual task

step 3:
create warehouseDTO java record in the domain subpackage for reading data from input/warehouses.json

step 4:
create WarehouseDTOPatterns enum in domain, each field in warehouseDTO should have compiled regex and error message
name: should be a-z characters only and allow for multiple names
address: single line US address format
city: multi-part name for city with space character separators, abbreviation punctuation allowed
state: two letter abbreviation
postalCode: US postal code with plus four optional
warehousePhone: US phone numbers with or without parenthesis and dash
squareFootage: whole number value between 1000-3000000
loadingDocks: whole number value between 1-100

step 5:

Create ExtDataValidationService in the service subpackage with validate method that accepts T object to validate in service

validate all fields in object and log failed objects as error reporting each filed that failed validation

step 6: 
create InputProperties.java in config subpackage, annotate with the @Component stereotype, create a configuration properties class with the following specifications:
public Java record
annotated with @ConfigurationProperties and linked to the prefix input
Inside, define a nested public record named File
File record must contain properties: String warehouses, String employees, int maxCount, provide a @DefaultValue of "20" for the maxCount property

step 7:
create a BeanConfiguration class in config subpackage that uses Logger and has a @Bean method named createObjectMapper() to provide ObjectMapper from jackson library.  this method should log creation for debugging.  mark the class as a configuration component.

Step 8:
Create @Service WarehouseDTOCacheService in service subpackage, using constructor injection for InputProperties, ResourceLoader, ObjectMapper
store WarehouseDTO in list
use @PostConstruction on loadWarehouseDtos method using try with resources that:
gets warehouse path from input properties
uses resource loader for path
use Bean ObjectMapper readvalues
validate with ExtDataValidationService before caching in WarehouseDTOCacheService
use logging for error and debug in this service


step 9:
Create input directory in test resources directory hierachy and copy warehouses.json then rename it to test-warehouses.json

step 10:
create a test for WarehouseDTOCacheService in service subpackage of test to audit data loading, prove bad file name gets logged, prove missing file gets logged

steps 11: add WarehouseOperationsManagementApplication class to the subpackage above config annotated as spring boot application with spring application run in main method

step 12:
add WarehouseManagementController in the controller subpackage, annotate as a restcontroller and Request mapping of "/whoms/v2/" only accepting json inbound data and returning json outbound data

step 13: 
add a ControllerAdvice class named GlobalExceptionHandler in config subpackage that handles all exceptions from controllers to map bad request, bad content type, no matching request, exceptions, runtime exceptions.  Create a ErrorResponseDTO record in domain subpackage with fields: String message, int errorCode, UUID messageId.  Use the uuid returned in the message in the log entry of the exception captured.  create a single generic error message "Our apologies for fumbling your request" in the global exception handler

step 14:
create a warehouse bean entity for persistence in subpackage domain that has properties of WarehouseDTO plus uuid id. this entity will be used with spring jpa repository interface named WarehouseJpaRepository in the repository subpackage with findByName, findByPostalCode, findByPhone

step 15:
create a WarehouseManagerService with interface in service subpackage with method to persistWarehouseDTO, this method will validate inbound WarehouseDTO using ExtDataValidationService before calling private internal method to convert WarehouseDTO to entity Warehouse to persist the entity.  another method warehouseLookUp that accepts generic parameter, validates that parameters using WarehouseDTOPatterns using method of ExtDataValidationService, if valid then uses JpaRepository to search by name, by postal code, or by phone.  logging any exceptions as they occur but throwing exceptions back to caller.  another method for addPerspectiveWarehouse that validates the entity prior to persisting with the jpa repository.  another method updateWarehouseInformation that accepts and entity, validates it and then uses jpa to update persisted entity.