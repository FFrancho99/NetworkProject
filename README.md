# NetworkProject
First of all, download all the files of this github in a NetworkProject folder.

// Je fais cette partie en FR parce que je suis fatigué et que c'est pas si simple que ça désolé les amis :

trouvez le document texte "database.txt" dans les fichiers et copiez son chemin d'accès (copy path). Vous allez ensuite devoir coller le chemin d'accès à ce fichier dans certains endroits du code (où il sera indiquer "ADD YOUR PATH TO DATABASE"):
1- Dans le fichier ClientLogin aux lignes 31 et 45
2- Dans le fichier AccountCreator aux 60 et 67

// En gros il faut prendre le path complet de database pour que ça fonctionne par exemple pour moi c'est C:\Users\Antoine\Documents\NetworkProject\src\Model\database.txt mais
// c'est différent sur chaque pc et on mettra "ADD YOUR PATH TO DATABASE" aux bons endroits au moment d'envoyer le code en vrai c'est pas si compliqué

trouvez le document texte "jokeDatabase.txt" dans les fichiers et copiez son chemin d'accès (copy path). Vous allez ensuite devoir modifier le chemin d'accès à ce fichier dans certains endroits du code (où il sera indiquer "ADD YOUR PATH TO JOKEDATABASE"):
1- Dans le fichier ServerThreadNew ligne 165

Pour finir, ouvrez le projet avec Intellij allez dans l'onglet build -> built artefacts -> all artifacts.

Tous vos fichiers sont maintenant bien setup

//

you can now start the Server application: 
open a command prompt and go to NetworkProject\out\artifacts\Server_jar (using cd command in windows prompt)
use the command: 
java -jar NetworkProject.jar

Open a new command prompt for each client you want and go to NetworkProject\out\artifacts\Client_jar (using cd command in windows prompt)
use the command: 
java -jar NetworkProject.jar

You now need to connect to your account or to signup if you don't have account yet.

After authentication on the client application, you can now send a message to any other connected client
Type 'help' if you need help on the various commands

To stop the client and server, press CTRL+C
