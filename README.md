# java-server

A demonstration of a client-server architecture implemented using Java Sockets, with an authentication stage implemented with TCP and the data transfer stage with UDP. The client sends a string to the server, and the server will respond with the input string reversed, which the client then prints to the console.

If available, run 'make' to compile the requisite files, or 'javac client.java sever.java' otherwise.

Then, start the server by invoking the server.sh script:
```
./server.sh auth_code
```

Run a client instance by invoking the client.sh script:
```
./client.sh server_host negotiation_port auth_code msg
```

* __auth_code__ is the integer used for authentication of the client by the server. When the server is spun up, it is given a random auth_code chosen by the user. If a client connects to a server with a code that does not match the server's code, the connection will immediately be closed.
* __server_host__ is the address of the server, either hostname or IP address.
* __negotiation_port__ is the port on which the server is listening for new connections; the server will print this port when it is first spun up, and it is the client's responsibility to know this information.
* __msg__ is the arbitrary string that the user wishes to have reversed; any string less than 1024 bytes will be successfully returned.