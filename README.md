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
* /users
    * GET -> Scoreboard
    * POST -> Créer un user, payload (JSON): ```{"email":"zb@hotmail.fr","name":"BondAge"}```

### Push an update to the magic cloud

```bash
mvn appengine:update
```
