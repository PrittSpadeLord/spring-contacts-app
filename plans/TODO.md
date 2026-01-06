1. Replace the raw SQL in `src/main/java/.../data/dao` with the sqls in `src/main/resources`
1. Move sensitive data into a properties file and create a template properties file
2. Write tests for Contacts
3. Research on how to test for controllers and other classes. Also research on how we can incorporate IoC in tests instead of manually initializing them.
4. Write tests for Logging in Functionality, and make sure non-existent username case is handled
5. Set up JSpecify static analysis using ErrorProne and NullAway
6. Set up JaCoCo to ensure all areas of our application get tested