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

## Create Bucket on Google Cloud Storage
```
gsutil mb gs://polar-winter-218511
gsutil defacl set public-read gs://polar-winter-218511
```

## Routes

Host: https://polar-winter-218511.appspot.com/

* `/users`
    * GET → Scoreboard
    * POST → Create a user, payload (JSON): ```{"email":"zb@hotmail.fr","name":"BondAge"}```
* `/admin`
    * POST → Force create user, 
        - payload (JSON): ```{"email":"casu@hotmail.fr","name":"testCasu","score":120.0}```
        - ```{"email":"leet@hotmail.fr","name":"testlee7","score":1337.7}```
    * DELETE → Delete all data.   
* `/createfile/polar-winter-218511/<filename>?size=<size>`
    * GET → Create a file named `<filename>` of size `<size>` containing random data.
* `/email`
    * POST → Send an email.
        - payload (`application/json`) :
        - ```{"to":"<email>@gmail.com","to_meta":"Firstname LASTNAME","subject":"Testing Email Service Subject","body":"This is an example of email body"}```

### Push an update to the magic cloud

```bash
mvn appengine:update
```
