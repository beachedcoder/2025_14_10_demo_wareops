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
Create input directory in test resources and copy warehouses.json then rename it to test-warehouses.json

copy application.properties to test resources directory
create a test for WarehouseDTOCacheService to audit data loading, prove bad file name gets logged, prove missing file gets logged
