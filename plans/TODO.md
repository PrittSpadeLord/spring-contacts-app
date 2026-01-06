1. There is significant delay in database queries, perhaps due to querying during the auth checks.
   - The database query can be performed in the Authorization interceptor itself.
   - The goal is simple: fetch the data, then check if the user id of the fetched data matches the identity of the user.
   - If the above satisfies, pass in the fetched data to the next stage.
   - However, this means the database layer is interacted alongside the authorization layer, muddying the separation of concerns, and leaves room for unauthorized db access should any vulnerability in this code be discovered.
   - Perhaps we shall bypass the check to see if the user owns the data entirely and simply bake it into the SQL. If the data is not found, it is returned as 404, even if it belongs to someone else. This would be Resource Obfuscation at play.
2. Move sensitive data into a properties file and create a template properties file
3. Write tests for Contacts
4. Research on how to test for controllers and other classes. Also research on how we can incorporate IoC in tests instead of manually initializing them.
5. Write tests for Logging in Functionality, and make sure non-existent username case is handled
6. Set up JSpecify static analysis using ErrorProne and NullAway
7. Set up JaCoCo to ensure all areas of our application get tested