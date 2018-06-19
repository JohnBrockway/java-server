# Specify compiler
JCC = javac

# Specify flags (-g is compile with debugging)
FLAGS = -g

default: Client.java Server.java
	$(JCC) $(FLAGS) Client.java Server.java

clean:
	$(RM) Client.class Server.class