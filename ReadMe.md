#### Android chat
Vesica is an android chat application that uses a decentralized chat server to relay information between clients. Libp2p from IPFS module is used to act as the server

- Repo is no longer maintained and uses deprecated version of libp2p from IPFS module

#### TODO:
- Add ciphertext <--> plaintext on android app
- Add auto routing mechanism to server nodes (sadly clusters came out after we stopped our work)
- UPDATE: Kotlin is the new default language for Android, so lot of refractoring is now required

#### Workflow
- User registers on the login page. This generates username password on the server db which can be accessed by several nodes
- SQLBrite used to store chat details and messages (native event emitters suck!)
- User then logs in and adds another valid user to the chat (like adding a mobile number to WhatsApp). To maintain anonymity we don't use mobile numbers anywhere
- Once a chat room is established, all clients are connected via websockets to a node that is part of the p2p network. When a message is sent it contains meta-data about the sender and receiver which is used for the routing process
- IMP: There is no offline syncing as messages are not cached anywhere. As long as your phone is online it will receive messages and populate once you start the app

#### Deployment
- Go to `com.example.varun.vesica.Constants` and change the IP address to that of the auth server and node


#### Libraries Used
- Pretty much all of Jake Wharton's work (ButterKnife, RxJava, SQLBrite, Observables, Retrofit2, RxBinding, GSON, Timber)
- Stethoscope from FaceBook for debugging
- Spongy Castle and AES for generation of keys through Diffie Hellman key exchange (not fully integrated)
-



MIT License - 2016 - Varun Agarwal
Do whatever you want with this
