1. There is significant delay in database queries, perhaps due to querying during the auth checks. This must be addressed, perhaps by using an Object to store the value after being passed.
2. Move sensitive data into a properties file and create a template properties file
3. Write tests for Contacts
4. Research on how to test for controllers and other classes. Also research on how we can incorporate IoC in tests instead of manually initializing them.
5. Write tests for Logging in Functionality, and make sure non-existent username case is handled
6. Set up JSpecify static analysis using ErrorProne and NullAway
7. Set up JaCoCo to ensure all areas of our application get tested