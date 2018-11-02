# S9_SACC_Poly411_L5

## Team members :

	- Bounouas Nassim
	- Cancela Vaz Joël
	- Mortara Johann
	- Novac Pierre-Emmanuel
	- Rousseau Nikita

## Launch

### Launch DB

```bash
gcloud beta emulators datastore start # Local only
```

### Launch server

```bash
launch_server.cmd
```

## Routes

Host: https://polar-winter-218511.appspot.com/

* /users
    * GET -> Scoreboard
    * POST -> Créer un user, payload (JSON): ```{"email":"zb@hotmail.fr","name":"BondAge"}```
* /admin
    * POST -> Force create user, 
        - payload (JSON): ```{"email":"casu@hotmail.fr","name":"testCasu","score":120.0}```
        - ```{"email":"leet@hotmail.fr","name":"testlee7","score":1337.7}```
    * DELETE -> Delete all data.   
### Push an update to the magic cloud

```bash
mvn appengine:update
```
