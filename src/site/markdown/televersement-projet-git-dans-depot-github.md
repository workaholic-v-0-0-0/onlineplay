# Téléversement du projet Git dans un dépôt de GitHub

On crée un compte GitHub et un répertoire d'url suivant :
`https://github.com/workaholic-v-0-0-0/onlineplay`

On lie le répertoire local `onlineplay` au répertoire distant du même nom, on
vérifie que cette liaison a bien été établie et on crée un token avec le scope
`repo` et la date d'expiration `2024-06-10` puis on pousse (téléverse) le projet
dans le dépôt distant via :
```sh
cd $HOME/env/maven/maven-workspace/onlineplay
git remote add origin https://github.com/workaholic-v-0-0-0/onlineplay.git
git push origin master
# Username for 'https://github.com': workaholic-v-0-0-0
# Password for 'https://workaholic-v-0-0-0@github.com': ghp_████████████████████
```
La collaboration à distance entre développeurs est désormais possible.
