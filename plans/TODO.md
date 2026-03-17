1. Update status codes
   - Use 201 when resource is created
   - Use 422 if the JSON is properly formatted but invalid
2. Follow RESTful naming practices for URL endpoints
2. Move sensitive data into a properties file and create a template properties file
3. See if we can improve the performance of the user authentication by reducing the delay further
4. Write tests for Contacts
5. Research on how to test for controllers and other classes. Also research on how we can incorporate IoC in tests instead of manually initializing them.
6. Write tests for Logging in Functionality, and make sure non-existent username case is handled
7. Set up JSpecify static analysis using ErrorProne and NullAway
8. Set up JaCoCo to ensure all areas of our application get tested
9. When it's time to create UI endpoints, make sure `/register` returns a 201 (Resource created) instead of 200.
10. Experiment with Open J Proxy