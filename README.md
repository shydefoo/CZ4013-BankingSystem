#CZ4013-Assignment README
##Server side:
Folder - '/BankSystemServer/'

Directory | Contents
----------| ---------
'BankSystem' | Codes for client side application
'BankSystemServer '| Codes for server side application
'BankSystem/src/message' 'BankSystemServer/src/message'  | Code for marshalling and unmarshalling
'BankSysten/src/socket' 'BankSystemServer/src/socket' | Code for different sockets which the client/server can use
'BankSysten/src/services' 'BankSystemServer/src/services' | Code for different services that the client can make and that the server provides


###Steps to start server: 
1. main.ServerApplication.java contains main method of BankSystemServer. Instructions on console 
2. Input IP address 
3. Input port number
4. Configure semantics:  
	* For At-Least-Once semantics, choose option 1. 
	* For At-Most-Once semantics, choose option 2. 
5. Configure Socket type:
	*  For normal conditions (no simulations of packet loss): choose option 1, NormalSocket
	* To simulate packet loss on sending: choose option 2, SendingLossSocket
	* To simulate receiving/sending corrupted packets: choose option 3, CorruptedSocket

Example of console when starting server:
----------------------------------------------------------------------------------------
java main.ServerApplication
Starting server
Input IP address hosting server on:
127.0.0.1
Input port number for server to listen at:
8000
Select Server type: 
1)At-Least-Once
2)At-Most-Once
1
Select Socket Type: 
1)Normal Socket
2)SendingLossSocket 
3)CorruptedSocket
1
Service added
Service added
Service added
Service added
Service added
Service added
----------------------------------------------------------------------------------------
##Client side:
Folder - '/BankSystem/'

###Steps to start client
1. main.Application.java contains main method of client side application.
2. Input IP address 
3. Input port number
4. Input timeout
5. Configure Socket type:
	* For normal conditions (no simulations of packet loss): choose option 1, NormalSocket
	* To simulate packet loss on sending: choose option 2, SendingLossSocket
	* To simulate packet loss on receiving: choose option 3, ReceivingLossSocket
	* To simulate receiving/sending corrupted packets: choose option 4, CorruptedSocket

Example of console when starting client:
----------------------------------------------------------------------------------------
Shides-MacBook-Pro:bin Shide$ java main.Application
Input server ip address:
127.0.0.1
Input server port number
8000
Input socket timeout
1
Select Socket Type: 
1)Normal Socket
2)SendingLossSocket
3)ReceivingLossSocket
4)CorruptedSocket
1
0: Create Account
1: Close Account
2: Make Deposit/Withdrawal
3: Transfer Funds
4: Register Callback
5: Check Balance
Enter service request: 

----------------------------------------------------------------------------------------
