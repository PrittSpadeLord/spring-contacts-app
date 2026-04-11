1. Update Containerfile to not run as root (by adding a user)
2. Update status codes
   - Use 422 if the JSON is properly formatted but invalid
3. Move sensitive data into a properties file and create a template properties file
4. See if we can improve the performance of the user authentication by reducing the delay further
5. Write tests for Contacts
6. Research on how to test for controllers and other classes. Also research on how we can incorporate IoC in tests instead of manually initializing them.
7. Write tests for Logging in Functionality, and make sure non-existent username case is handled
8. Set up JSpecify static analysis using ErrorProne and NullAway
9. Set up JaCoCo to ensure all areas of our application get tested
10. Experiment with Open J Proxy