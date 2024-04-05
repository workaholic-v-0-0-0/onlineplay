# Déploiement sur un droplet de Digital Ocean

## Création d'un droplet

Le web GUI de Digital Ocean propose les services suivants (après avoir créé un
compte) :
1. Deploy a web application
2. Host a website or static site
3. Deploy a container-based app
4. Deploy a database
5. Deploy a virtual machine
6. Store static object

On pourrait choisir 1) ou 3) mais, pour une compréhension approfondie de la
gestion de serveurs, y compris l'installation et la configuration de logiciels,
la sécurisation des systèmes, la performance et le monitoring, on choisit 5).

On crée un droplet dont les caractéristiques suivantes sont peu onéreuses (6 euros
par mois) :
- OS Ubuntu version 22.04 LTS x64
- shared CPU
- Disk type : SSD
- Add improved metrics monitoring and alerting (free) (Collect and graph
  expanded system-level metrics, track performance, and set up alerts instantly
  within the control panel.)
- Hostname : onlineplay
- SSH Key (Connect to your Droplet with an SSH key pair)

Sur machine locale, on crée une paire de clés cryptographiques :
```sh
ssh-keygen
ls -l ~/.ssh/*
```

On copie dans le formulaire du GUI de Digital Ocean le contenu de
$(HOME)/.ssh/id_rsa.pub et on choisit de nommer cette clé publique
workaholic-laptop-deploy-240312.

## Connexion au droplet

L'adresse IP du droplet onlineplay est communiquée par le web GUI de Digital Ocean
après sa création : `64.23.187.50`

On s'y connecte en ssh via :

```sh
ssh root@64.23.187.50
```

```plaintext
$ ssh root@64.23.187.50
The authenticity of host '64.23.187.50 (64.23.187.50)' can't be established.
ED25519 key fingerprint is SHA256:1yHlZz6xv0CAKk/Q2kgVVjW5eSbsFEr3dAlqsMknc9E.
This key is not known by any other names
Are you sure you want to continue connecting (yes/no/[fingerprint])? yes
Warning: Permanently added '64.23.187.50' (ED25519) to the list of known hosts.
Welcome to Ubuntu 22.04.2 LTS (GNU/Linux 5.15.0-67-generic x86_64)

 * Documentation:  https://help.ubuntu.com
 * Management:     https://landscape.canonical.com
 * Support:        https://ubuntu.com/advantage

  System information as of Tue Mar 12 13:10:23 UTC 2024

  System load:  0.26416015625     Users logged in:       0
  Usage of /:   6.9% of 24.05GB   IPv4 address for eth0: 64.23.187.50
  Memory usage: 21%               IPv4 address for eth0: 10.48.0.5
  Swap usage:   0%                IPv4 address for eth1: 10.124.0.2
  Processes:    94

Expanded Security Maintenance for Applications is not enabled.

17 updates can be applied immediately.
13 of these updates are standard security updates.
To see these additional updates run: apt list --upgradable

Enable ESM Apps to receive additional future security updates.
See https://ubuntu.com/esm or run: sudo pro status


The list of available updates is more than a week old.
To check for new updates run: sudo apt update


The programs included with the Ubuntu system are free software;
the exact distribution terms for each program are described in the
individual files in /usr/share/doc/*/copyright.

Ubuntu comes with ABSOLUTELY NO WARRANTY, to the extent permitted by
applicable law.

root@onlineplay:~#
```

## Mise à jour du droplet

Pour mettre à jour le droplet, exécutez les commandes suivantes dans le terminal :

```sh
sudo apt update
sudo apt upgrade
```

## Architecture sur le droplet

Cette architecture intègre un kit de développement Java (JDK (Java Development Kit)), un client et un serveur MySql 
et un serveur Tomcat :

```plaintext
/opt
├── digitalocean
│   ├── bin
│   │   └── droplet-agent
│   └── droplet-agent
│       └── scripts
├── java
│   └── jdk-21.0.2
│       ├── LICENSE -> legal/java.base/LICENSE
│       ├── README
│       ├── bin
│       ├── conf
│       ├── include
│       ├── jmods
│       ├── legal
│       ├── lib
│       ├── man
│       └── release
└── tomcat
    └── apache-tomcat-10.1.19
        ├── BUILDING.txt
        ├── CONTRIBUTING.md
        ├── LICENSE
        ├── NOTICE
        ├── README.md
        ├── RELEASE-NOTES
        ├── RUNNING.txt
        ├── bin
        ├── conf
        ├── lib
        ├── logs
        ├── temp
        ├── webapps
        └── work
/etc
├── [...]
├── apache2
│   ├── apache2.conf
│   ├── conf-available
│   ├── conf-enabled
│   ├── nginx.conf
│   ├── mods-available
│   ├── mods-enabled
│   ├── ports.conf
│   ├── sites-available
│   ├── sites-enabled
│   ├── [...]
├── [...]
├── mysql
│   ├── conf.d
│   ├── debian-start
│   ├── debian.cnf
│   ├── my.cnf -> /etc/alternatives/my.cnf
│   ├── my.cnf.fallback
│   ├── mysql.cnf
│   └── mysql.conf.d
├── nginx
│   ├── conf.d
│   ├── modules-available
│   ├── modules-enabled
│   ├── nginx.conf
│   ├── sites-available
│   ├── sites-enabled
│   ├── [...]
├── [...]
[...]
/usr
├── [...]
├── bin
│   ├── [...]
│   ├── mysql
│   ├── [...]
├── [...]
├── lib
│   ├── [...]
│   ├── apache2
│   ├── [...]
│   ├── mysql
│   ├── [...]
│   ├── nginx
├── [...]
├── sbin
│   ├── [...]
│   ├── apache2
│   ├── [...]
│   ├── nginx
│   ├── [...]
├── share
│   ├── [...]
│   ├── apache2
│   ├── [...]
│   ├── mysql
│   ├── [...]
│   ├── nginx
│   ├── [...]
├── [...]
[...]
```

## Installation sur le droplet

Création du répertoire pour Java, téléchargement du JDK, extraction du JDK dans le répertoire cible et suppression de 
l'archive :

```sh
mkdir -p /opt/java
wget https://download.oracle.com/java/21/latest/jdk-21_linux-x64_bin.tar.gz
tar -xzvf jdk-21_linux-x64_bin.tar.gz -C /opt/java/
rm jdk-21_linux-x64_bin.tar.gz
```

Préparation de l'environnement pour Tomcat, téléchargement de Tomcat 10, extraction de Tomcat dans le répertoire cible 
et suppression de l'archive :

```sh
mkdir -p /opt/tomcat
wget https://dlcdn.apache.org/tomcat/tomcat-10/v10.1.19/bin/apache-tomcat-10.1.19.tar.gz
tar -xzvf apache-tomcat-10.1.19.tar.gz -C /opt/tomcat
rm apache-tomcat-10.1.19.tar.gz
```

Configuration de JAVA_HOME et ajout au PATH en ajoutant à la fin du fichier /root/.bashrc les lignes suivantes :

```sh
# Java
export JAVA_HOME="/opt/java/jdk-21.0.2"
export PATH="$JAVA_HOME/bin:$PATH"
```

Installation de MySQL, configuration initiale et modification du mot de passe utilisateur root de MySQL :

```sh
apt install mysql-server
mysql_secure_installation
mysql -u root -p
ALTER USER 'root'@'localhost' IDENTIFIED WITH mysql_native_password BY '███████████████';
exit;
```

Création de la base de données de l'application web et de ses tables :

```sh
mysql -u root -p

CREATE DATABASE onlineplay default character set utf8 collate utf8_general_ci;

USE onlineplay;

CREATE TABLE users (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(25) NOT NULL,
    password_hash VARCHAR(128) NOT NULL,
    email VARCHAR(254),
    message VARCHAR(254)
);

CREATE TABLE connections (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    ip_address VARCHAR(45) NOT NULL,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    user_id INT,
    is_allowed TINYINT(1),
    FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE games (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    state ENUM('in_progress', 'finished', 'pending', 'cancelled') NOT NULL,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    first_player_id INT,
    second_player_id INT,
    winner_id INT,
    FOREIGN KEY (first_player_id) REFERENCES users(id),
    FOREIGN KEY (second_player_id) REFERENCES users(id)
);

CREATE TABLE moves (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    game_id INT NOT NULL,
    user_id INT NOT NULL,
    number INT NOT NULL,
    move INT NOT NULL,
    FOREIGN KEY (game_id) REFERENCES games(id),
    FOREIGN KEY (user_id) REFERENCES users(id)
);
```

Configuration de CATALINA_HOME pour Tomcat en ajoutant à la fin de /root/.bashrc les lignes suivantes :

```sh
# Tomcat
export CATALINA_HOME="/opt/tomcat/apache-tomcat-10.1.19"
```

Envoi du fichier WAR vers le serveur Tomcat distant (sur le droplet) :

```sh
scp \
~/env/maven/maven-workspace/onlineplay/webapp/target/webapp.war \
root@64.23.187.50:/opt/tomcat/apache-tomcat-10.1.19/webapps/
```

Connexion au droplet et démarrage de Tomcat :

```sh
ssh root@64.23.187.50
/opt/tomcat/apache-tomcat-10.1.19/bin/startup.sh
exit
```

On peut maintenant se connecter au serveur Tomcat du droplet qui 
déploie l'application web via l'url :

```
http://64.23.187.50:8080/webapp/home
```

## Configuration DNS

On achète au registraire de domaine Namecheap le nom de domaine :

```
caltuli.online
```

Via le web GUI de NameCheap, on enregistre un enregistrement A dans la zone DNS
du domaine `caltuli.online` qui pointe vers l'adresse IP publique du droplet
(64.23.187.50)

On attend la propagation DNS (Celle-ci a duré moins d'une minute).

On peut maintenant se connecter au serveur Tomcat hébergé sur le droplet via l'url :

```
http://caltuli.online:8080/webapp/home
```

## Sécurisation du serveur Tomcat hébergé sur le droplet

### Obtention d'un certificat SSL/TLS

```sh
ssh root@64.23.187.50
apt install certbot
sudo certbot certonly --standalone -d caltuli.online
exit
```

### Préparation du Keystore pour Tomcat

```sh
ssh root@64.23.187.50
openssl pkcs12 \
-export \
-in /etc/letsencrypt/live/caltuli.online/cert.pem \
-inkey /etc/letsencrypt/live/caltuli.online/privkey.pem \
-out keystore.p12 \
-name tomcat \
-CAfile /etc/letsencrypt/live/caltuli.online/chain.pem \
-caname root \
-chain
keytool \
-importkeystore \
-destkeystore mykeystore.jks \
-srckeystore keystore.p12 \
-srcstoretype pkcs12 \
-alias tomcat
exit
```

### Configuration de Tomcat

```sh
ssh root@64.23.187.50
apt-get install libtcnative-1
nano /opt/tomcat/apache-tomcat-10.1.19/conf/server.xml
exit
```

On ajoute dans `/opt/tomcat/apache-tomcat-10.1.19/conf/server.xml` :

```xml
<Connector port="80" protocol="HTTP/1.1"
           connectionTimeout="20000"
           redirectPort="443"
           />

<!-- Define an SSL Coyote HTTP/1.1 Connector on port 8443 -->
<Connector
    protocol="org.apache.coyote.http11.Http11NioProtocol"
    port="443"
    maxThreads="150"
    SSLEnabled="true"
    maxParameterCount="1000"
    >
  <SSLHostConfig>
    <Certificate
      certificateKeystoreFile="/root/mykeystore.jks"
      certificateKeystorePassword="<mot de passe>"
      type="RSA"
      />
    </SSLHostConfig>
</Connector>
```

Via le web GUI de Digital Ocean, on ajoute un firewall pour autoriser les flux SSH, HTTP et HTTPS entrant 
respectivement sur les ports 22, 80 et 443 du droplet.

On redémarre le serveur Tomcat sur le droplet :

```sh
ssh root@64.23.187.50
/opt/tomcat/apache-tomcat-10.1.19/bin/shutdown.sh
/opt/tomcat/apache-tomcat-10.1.19/bin/startup.sh
exit
```

On peut maintenant se connecter au serveur Tomcat hébergé sur le droplet via l'url :

```
https://caltuli.online/webapp/home
```

### Implémentation de la Redirection HTTPS

Nous implémentons l'interface `Filter` via la classe `HttpsRedirectFilter` pour
rediriger automatiquement les requêtes HTTP vers HTTPS, renforçant ainsi la
sécurité des applications web. Ce filtre intercepte toutes les requêtes entrantes
et vérifie si elles sont sécurisées (c'est-à-dire, utilisant HTTPS). Si une
requête n'est pas sécurisée, le filtre construit une nouvelle URL avec le schéma
HTTPS et redirige le client vers cette URL, garantissant ainsi que la
communication est cryptée et plus sécurisée contre l'interception ou l'écoute
clandestine.

```java
package online.caltuli.webapp.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

// Implements a filter to redirect HTTP requests to HTTPS, enhancing security.
public class HttpsRedirectFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        if (!request.isSecure()) {
            String url = "https://" + request.getServerName() + request.getRequestURI();
            if (request.getQueryString() != null) {
                url += "?" + request.getQueryString();
            }
            response.sendRedirect(url);
        } else {
            chain.doFilter(req, res);
        }
    }
}
```

Le retour de la commande `curl` suivant témoigne du bon fonctionnement de ce mécanisme :

```plaintext
$ curl -v http://caltuli.online/webapp/home
*   Trying 64.23.187.50:80...
* Connected to caltuli.online (64.23.187.50) port 80 (#0)
> GET /webapp/home HTTP/1.1
> Host: caltuli.online
> User-Agent: curl/7.81.0
> Accept: */*
> 
* Mark bundle as not supporting multiuse
< HTTP/1.1 302 
< Location: https://caltuli.online/webapp/home
< Content-Length: 0
< Date: Sat, 16 Mar 2024 16:41:01 GMT
< 
* Connection #0 to host caltuli.online left intact
$ 
```

## Déploiement du site de documentation du projet

### Mise en place d'un proxy inversé Nginx transparent autour de Tomcat

Dans le fichier de configuration de Tomcat `/opt/tomcat/apache-tomcat-10.1.19/conf/server.xml`, on ajuste pour que 
Tomcat écoute sur le port `8443` plutôt que le port `443`:

```xml
<!--
<Connector port="80" protocol="HTTP/1.1"
           connectionTimeout="20000"
           redirectPort="8443"
           />
-->
<!-- Define an SSL Coyote HTTP/1.1 Connector on port 8443 -->
<Connector
    protocol="org.apache.coyote.http11.Http11NioProtocol"
    port="8443"
    maxThreads="150"
    SSLEnabled="true"
    maxParameterCount="1000"
    >
  <SSLHostConfig>
    <Certificate
      certificateKeystoreFile="/root/mykeystore.jks"
      certificateKeystorePassword="Nmdp=jaeuop+2633"
      type="RSA"
    />
  </SSLHostConfig>
</Connector>
```

On installe le serveur Nginx puis on le configure de façon à ce que :

1. Le serveur Nginx n'écoute que les ports `80` et `443`.
2. Il redirige toutes les requêtes HTTP vers HTTPS.
3. Tout comme le serveur Tomcat, hormis cette redirection, il n'accepte 
que les échanges avec les clients via le protocole SSL/TLS.
4. Tout comme le serveur Tomcat, il s'authentifie auprès des clients via le 
certificat Let's Encrypt généré dans la section "Obtention d'un certificat SSL/TLS"
et stocké dans le fichier '/etc/letsencrypt/live/caltuli.online/fullchain.pem'.
5. Il déchiffre les messages des clients via la clé privée associée à la clé publique 
contenue dans ce certificat et stockée dans le fichier 
/etc/letsencrypt/live/caltuli.online/privkey.pem.
6. Il réachemine le traffic HTTPS entrant sur le port '443' vers le port '8443' écouté 
par le serveur Tomcat.
7. Avant ce réacheminement, il ajoute des en-têtes HTTP Host, X-Real-IP, X-Forwarded-For 
et X-Forwarded-Proto pour conserver l'information originale sur l'hôte demandé, 
l'adresse IP réelle du client, les adresses IP dans la chaîne de proxies et le 
protocole utilisé par le client (HTTP ou HTTPS). Ces informations sont essentielles 
pour que le serveur Tomcat derrière Nginx puisse comprendre le contexte de la requête 
originale malgré le proxy, notamment pour le pare-feu applicatif implémenté dans le 
package online.caltuli.consumer.api.abuseipdb reste fonctionnel.

```sh
ssh root@64.23.187.50
apt update
apt upgrade
apt install nginx -y
cat > /etc/nginx/sites-available/default <<EOF
server {
    listen 80;
    server_name caltuli.online;
    return 301 https://\$server_name\$request_uri;
}
server {
    listen 443 ssl;
    server_name caltuli.online;

    ssl_certificate /etc/letsencrypt/live/caltuli.online/fullchain.pem;
    ssl_certificate_key /etc/letsencrypt/live/caltuli.online/privkey.pem;

    location / {
        proxy_pass https://localhost:8443;
        proxy_set_header Host \$host;
        proxy_set_header X-Real-IP \$remote_addr;
        proxy_set_header X-Forwarded-For \$proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto \$scheme;
    }
}
EOF
sudo nginx -t
systemctl reload nginx
exit
```

### Installation et configuration du serveur Apache 2 entouré par Nginx

On installe le serveur Apache2 puis on configure un hôte virtuel dans le fichier
`/etc/apache2/sites-available/onlineplay-documentation.conf` de façon à ce que :
- Cet hôte virtuel n'écoute que le port `9443` suivant le protocole SSL/TLS en
  présentant le certificat Let's Encrypt généré dans la section "Obtention d'un
  certificat SSL/TLS".
- Cet hôte virtuel sert le site statique stocké sous le répertoire `/var/www/html`.
  C'est donc dans ce répertoire que l'on va téléverser le site de documentation
  généré par Maven dans le répertoire onlineplay/target/site pendant le cycle de vie
  `site`.

```sh
ssh root@64.23.187.50
apt update
apt upgrade
apt install apache2
cat > /etc/apache2/sites-available/onlineplay-documentation.conf <<EOF
<IfModule mod_ssl.c>
    <VirtualHost *:9443>
        ServerAdmin webmaster@localhost
        DocumentRoot /var/www/html/

        ErrorLog \${APACHE_LOG_DIR}/error.log
        CustomLog \${APACHE_LOG_DIR}/access.log combined

        SSLEngine on
        SSLCertificateFile /etc/letsencrypt/live/caltuli.online/fullchain.pem
        SSLCertificateKeyFile /etc/letsencrypt/live/caltuli.online/privkey.pem

        DirectoryIndex index.html
        ServerName caltuli.online
    </VirtualHost>
</IfModule>
EOF
cat > /etc/apache2/ports.conf <<EOF
<IfModule ssl_module>
	Listen 9443
</IfModule>

<IfModule mod_gnutls.c>
	Listen 9443
</IfModule>
EOF 
a2enmod ssl
a2dissite 000-default.conf
a2ensite onlineplay-documentation.conf
systemctl restart apache2
exit
```

On modifie la configuration du serveur Nginx dans 
`/etc/nginx/sites-available/default` pour qu'il réachemine le 
traffic HTTPS entrant sur le port 443 vers le port 9443 écouté 
par le serveur Apache2 en ajoutant les lignes suivantes :

```nginx
location /docs {
    proxy_pass https://localhost:9443;
    proxy_set_header Host \$host;
    proxy_set_header X-Real-IP \$remote_addr;
    proxy_set_header X-Forwarded-For \$proxy_add_x_forwarded_for;
    proxy_set_header X-Forwarded-Proto \$scheme;
}
```

On vérifie et on recharge la configuration du serveur Nginx :

```sh
sudo nginx -t
sudo systemctl reload nginx
```

On téléverse le site généré par Maven sur le droplet :

```sh
scp -r \
/home/workaholic/env/maven/maven-workspace/onlineplay/target/staging/* \
root@64.23.187.50:/var/www/html/docs
```

Le site de documentation du projet généré par Maven est maintenant accessible via l'url :

```
https://caltuli.online/docs
```
