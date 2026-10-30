# King

Application de messagerie intelligente avec bulle flottante (corbeau noir) et fenêtre flottante de messages.

## État actuel
Version de départ (0.1.0) :
- écran d'accueil Compose avec demande d'autorisation d'affichage par-dessus les autres applis ;
- bulle flottante cliquable qui ouvre et ferme une petite fenêtre de messages ;
- écoute des notifications des autres applis (service NotificationListener).

À faire : messagerie réelle, réponse depuis les notifications (RemoteInput), backend Node.js, Gemini, icône corbeau vectorielle.

## Compilation
Ouvrir le dossier dans Android Studio et lancer « Run », ou exécuter `./gradlew assembleDebug`.
Autoriser ensuite l'accès aux notifications de King dans les paramètres Android.
