# distibutedSystemDemo

## Learnings:

### Picocli:
1. Different CLI commands can be written altogether in a new class which has all the commands in it.
2. Each new command in this class should be declared a new static class within it and this new class should be declared
as a subcommand class in the parent class.
3. The picocli handler class which sets the commandLine runner should refer to the parent class of the commands only.
It should only initialize the parent command class and pass this class as the argument to ***CommandLine().execute*** in the
***run()*** method for it to recognize and trigger relevant commands passed as command line arguments when the
application is executed.
4. The most simple way command can be coded is:
    >@Command(name = "sampleCommand", description = "A sample command)   
   > public class SampleCommand implements Runnable {  
   > @Parameter(index = "0", description = "Some description", defaultValue = "Some default value")  
   > private String param;         
   >          
   > @Option(names = { "-o" , "--option" }, description = "Some Option")  
   > private string option;    
   >         
   > @Override  
   > public void run() {  
   > // The main logic you want to execute when this command is executed should be written here as the command would
   > trigger run()   
   > System.out.prinlnt("The parameter passed is: " + param + "The option value passed is: " + option);  
   > }           


### Http Requests using SpringBoot RestTemplate:

1. SpringBoot offers 3 different types of clients for Http operations.
    1. WebClient - Asynchronous
   2. RestTemplate - Synchronous and non-fluent api
   3. RestClient - Synchronous and fluent api
   
2. For this project I used ***RestTemplate***. 
3. You need 4 things to make a http request.
    1. First you need a requestBody like String which contains information about the POST content.
   2. HttpHeaders object which has required header options set on it like .setContentType, etc.
   3. HttpEntity object built using optional requestBody (optional for GET requests) and HttpHeaders Object. The 
   type of HttpEntity object here is the type of the requestBody.
   4. RestTemplate.exchange() api which takes in the following parameters to make request:
        1. URL
      2. HttpMethod.Type like HttpMethod.POST
      3. HttpEntity object created above
         4. Type of response body this request would receive like String.class
   5. The exchange() api returns a ResponseEntity which is same like HttpEntity but also has StatusCode attached to it.
   The server endpoint should return a ResponseEntity in this case to match up. The type of ResponseEntity here would be
   the same type as passed into exchange api, which is String in this case.  
      
    Example: 
    >restTemplate.exchange(String url, HttpMethod method, HttpEntity<T> requestEntity, Class<T> responseType)  
   

### Better Coding Practices:
1. To declare a ***Constants*** class in the Java package which has constants that can be accessed across the package
can be created by adding a new class.
2. Then add a private //no-op constructor.
3. Add the constants variable you want like
> public static final String message =  "HelloWorld";
4. Then you can import this constant where-ever required like
>import static com.org.package.constant.Constant.message;