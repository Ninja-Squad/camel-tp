== Objectif
Parvenir a lire un fichier de commande, à les traiter selon le pays du client et déclencher la facturation.

== Etapes

=== Initialiser le projet
Cloner le projet

[source, bash]
----
$ git clone https://github.com/Ninja-Squad/camel-tp.git
----

=== Importer le projet
Ouvrir le projet avec Eclipse (ou votre IDE préféré).

=== Lancer le projet
Le projet peut être lancé en ligne de commande avec Maven.
[source, bash]
----
$ mvn clean camel:run
----

=== Lire le fichier

La première étape consiste à lire le contenu du fichier de commandes.

Ajouter une premiere route (classe étendant RouteBuilder) nommée OrderRouteBuilder.
Cette classe doit également être ajoutée à la classe Main, avec la méthode addRouteBuilder.

Cette classe doit posséder une méthode configure, qui contiendra la définition des routes.

Créer une premiere route qui va lire le dossier 'orders' et logger le contenu.

Voir dans la [doc](http://camel.apache.org/file2.html) comment lire un fichier depuis un répertoire et ne pas supprimer le fichier une fois celui-ci lu. 

Pour logger le contenu il est possible d'utiliser la méthode 'log' dans la route, en précisant le niveau de log (LoggingLevel.INFO) et le contenu à logger. Pour logger le body complet du message, utiliser "${body}".

=== Convertir les commandes en objets Java

Nous voulons maintenant convertir les commandes en objets Java pour les manipuler.

Créer une classe Java nommé 'Order' avec les champs nécessaires, les getters et une méthode toString. +
Ajouter la dépendance Maven vers [camel-bindy](http://camel.apache.org/bindy.html). +
Ajouter les annotations sur chaque champ pour que Camel fasse la conversion. +
Ajouter au début de la méthode configure :
[source, java]
----
DataFormat bindy = new BindyCsvDataFormat("camel");
----
afin de préciser à Camel quelles sont les classes gérées par Bindy (ici dans le package "camel").

Ajouter une étape de unmarshalling à la route. (On appelle marshalling le fait de convertir un objet Java dans un autre format et unmarshalling celui de convertir un objet dans un autre format en un objet Java).

Chercher et ajouter l'option permettant de ne pas lire la première ligne du fichier.

=== Afficher chaque ligne

Nous allons manipuler chaque commande individuellement.

Le pattern [splitter](http://camel.apache.org/splitter.html) permet de diviser un message. L'utiliser afin de logguer chaque ligne individuellement.

=== Créer une sous route

Placer le logger précédent dans une sous route, nommée 'direct:orders'.

=== Créer un processor

Nous voulons créer un processeur qui pour chaque commande ajoute des informations aux headers du messsage. Ces headers serviront par la suite.

Créer un processor qui pour chaque ligne devra extraire l'order de l'exchange (lire la doc de [camel-bindy](http://camel.apache.org/bindy.html) sur la partie unmarshalling). Le processor devra ensuite :
* copier l'order dans l'exchange de sortie
* ajouter un header 'country' a l'exchange de sortie, contenant le pays de la commande
* ajouter un header CamelFileName a l'exchange de sortie, contenant l'id et le produit (ex id:1, product:456-hy -> "1-456-hy.csv")

Le header CamelFileName sera le nom du fichier créé quand nous écrirons la commande sur le file system.

=== Trier les commandes

Se baser sur le header 'country' pour trier les messages et les écrire soit vers un dossier facturation/fr, si le pays est FR soit vers un dossier facturation/eu sinon.
le nom du fichier écrit sera celui présent dans le header CamelFileName.


=== Lire les commandes individuelles

Créer une route qui lit les commandes européennes et les sépare par pays. Le fichier d'origine doit cette fois être supprimé.

