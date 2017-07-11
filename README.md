# Stone wallet manager API #

API used to manage Credit Cards for a user like a wallet.

It's main features are:

* User management
* Multi Credit Card management
* Wallet management
* Pay amount of a card in the wallet
* Set your own wallet limit (based on wallet's cards)

Purchase an item using the wallet business rules described below:
https://slack-files.com/T06M9ENDT-F5XK4J0P2-532510c5c0


## Wallet manager contains 3 modules ##

* walletmanager (connects with an in memory database (H2) and contains the all the CRUD features)
* walletpurchase (contains the purchase logic described in the requirement)
* walletwebtoken (contains the simple webtoken authentication mechanism)


### Building ###
```
walletmanager\walletwebtoken> $ mvn clean package
walletmanager\walletpurchase> $ mvn clean package
walletmanager\walletwebtoken> $ mvn clean package

```

### Running ###
Pre-requisites: jdk-8 installed, run all the modules jar

**Windows**
```
walletmanager\modules>runwallet.bat
```
**Linux**
```
walletmanager/modules/runwallet.sh
```
All modules jars are inside root <modules> folder.

In order to test the API the chrome extension postman (https://chrome.google.com/webstore/detail/postman/fhbjgbiflinjbdggehcddcbncdddomop)
can be used.

The file "Stone Wallet Manager.postman_collection.json" file can be imported into the extension for sample usage and testing purposes. It contains the authentication mechanism samples as well (both valid and invalid tokens).

Note: This project uses lombok (https://projectlombok.org/) for code generation.