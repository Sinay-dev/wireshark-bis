# wireshark-bis

Visualiseur de Trafic

La Structure de notre programme est composé de 3 dossier :
bin, lib, src. 
bin contient les fichiers binaires utilisé à l'exécution du programme.
lib contient les librairies utilisé pour ce projet.
et src contient les fichiers code du programme.

Pour visualiser le trafic entre des clients et des serveurs on commence tout d'abord par
décoder les informations des trames reçus, les informations décodées sont Ethernet,
IPv4, TCP et HTTP. (Trame.java, Packet.java, Segment.java et Message.java)

Ensuite on dessine une flèche entre le client et le serveur dans la direction de la 
communication et on ajoute en commentaire les informations utiles de l'entête la plus
haute encapsulé . (ListeTrame.java)

Notre code permet facilement à un programmeur de rajouter un programme qui décode de
nouveaux protocoles grâce à nos interfaces. (ITrame.java, IPacket.java, ISegment.java et
IMessage.java) 

Comment installer notre Visualiseur de Trafic ?

Télécharger le projet sur moodle sous le format .zip
Puis l’extraire où bon vous semble .
Et vous ouvrez un terminal à l’intérieur du dossier extrait et écrivez : 
java -jar reseaux.jar -h
Un menu help s'affiche avec toutes les indications à savoir.