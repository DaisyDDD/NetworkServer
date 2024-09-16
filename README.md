CS5001  Practical3  05.11.2021

Author: Yujie

1.Program File Structure
-----------------------------------
<WebServerMain.class>: Implement main method.
<Server.class>: Create a server.
<ConnectionHandler.class>: All client requests and server response are handled in this class.
<Help.class>: With some helpful method to help checking validity and create a log file.

2. Project Structure
-----------------------------------
-src: All source code can be found in it.
-www: Original html pages can be found in it.
-Exception: Some error pages created by myself can be found in it.
-doc: All javadoc generated into webpages can be found in it.

3. Extension
-----------------------------------
1) Using Thread. A server can get connections of multiple clients.
2) All binary images can be shown correctly.
3) Create a log file, which indicates client sending request, request time, request type, response code and 
    response content length.
    This log file will be created and cleaned when the server starts and be closed when the server stops. Data 
    will be updated after handling each request from clients.
4) Create some error pages to be sent back to clients, including not found page and not implemented page.
