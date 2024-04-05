# Présentation générale du projet

## Étude et expérimentation d'un framework pour la réalisation collaborative d'une application web

L'objectif central de ce projet est de fournir une expérience d'apprentissage
pratique et complète sur les outils et méthodes essentiels dans le développement
collaboratif d'applications web. Il couvre l'intégralité du cycle de
développement, depuis la conception initiale et la structuration d'un
environnement de développement, jusqu'à la maintenance en production, en
soulignant l'importance d'une mise en oeuvre collaborative.

En développant une application web inspirée de plateformes telles que chess.com,
mais dédiée au jeu de Puissance 4, ce projet met en pratique un ensemble d'outils
et de méthodologies éprouvés dans le secteur professionnel. Ces outils et
méthodes, ainsi que leur application concrète à chaque étape du développement,
sont présentés dans les sous-sections suivantes.

Cette démarche vise non seulement à fournir aux participants des connaissances
théoriques, mais aussi à les engager dans une application pratique de ces
concepts, les dotant ainsi de compétences directement transférables et valorisées
dans le milieu professionnel, et renforçant par conséquent leur profil auprès
des recruteurs.

## Spécification Jakarta EE

Pour ce projet, nous avons choisi de nous appuyer sur la spécification Jakarta
EE, l'évolution de Java EE, qui représente le pilier des applications
d'entreprise modernes en Java. Une spécification, dans le contexte du
développement logiciel, est un ensemble de directives et de normes qui définissent
comment une technologie doit être utilisée et mise en oeuvre. Elle garantit la
standardisation et la compatibilité entre différents développements et
plateformes.

Jakarta EE est une spécification riche et étendue, conçue pour fournir un cadre
robuste pour le développement d'applications d'entreprise. Elle englobe une
variété de technologies et de composants qui sont essentiels pour construire des
applications web et d'entreprise évolutives et performantes. Parmi ces
composants, les servlets et les JavaServer Pages (JSP) sont des éléments
centraux. Les servlets permettent de créer des pages web dynamiques en répondant
aux requêtes des utilisateurs, tandis que les JSP facilitent l'écriture de pages
web en intégrant du code Java directement dans le HTML.

Dans notre projet, l'adoption de Jakarta EE nous permettra de tirer parti de ses
nombreuses fonctionnalités pour développer une application web Puissance 4. Nous
mettrons un accent particulier sur la modularité, la sécurité et la performance,
qui sont des aspects fondamentaux de cette spécification. L'utilisation de
Jakarta EE assurera que notre application soit non seulement conforme aux
standards de l'industrie, mais aussi qu'elle bénéficie de la robustesse, de la
scalabilité et de la flexibilité nécessaires pour répondre aux exigences d'une
application d'entreprise moderne.

Dans l'annexe B : "Étude du cours 'Développez des sites web avec Java EE' de
Mathieu Nebra sur openclassrooms.com pour développer une application web
proposant les services de lecture et d'écriture dans une table d'une base de
données MySQL tout en respectant MVC et DAO", nous apprendrons à utiliser la
spécification Jakarta EE tout en étudiant le cours "Développez des sites web avec
Java EE" sur openclassrooms.com.

## Utilisation du logiciel de gestion de développement de projets logiciels Apache Maven

Apache Maven, un outil essentiel en gestion de projets logiciels, sera au cœur
de notre projet. Sa portée va bien au-delà de la simple automatisation de la
construction de logiciels. En effet, Maven est réputé dans de nombreux milieux
professionnels pour sa capacité à standardiser et gérer des structures de
projets complexes. Sa compétence dans la gestion des dépendances et la
standardisation des processus de construction est hautement valorisée par les
recruteurs, fournissant ainsi aux participants une compétence directement
transférable dans le monde professionnel.

Maven se distingue par sa capacité à structurer un projet de manière claire et
ordonnée, inculquant les bonnes pratiques de développement. Cette structuration
naturelle selon des conventions éprouvées est cruciale pour assurer la cohérence
et la maintenabilité du code, particulièrement dans les grands projets ou les
environnements collaboratifs.

En outre, Maven excelle dans la facilitation de l'intégration continue et de la
livraison continue, essentielles à un développement logiciel moderne et efficace.
Ces processus automatisent les tests et le déploiement, garantissant une qualité
constante et minimisant les erreurs humaines lors des cycles de release.

Une fonctionnalité remarquable de Maven est sa capacité à générer un site de
documentation automatique pour le projet, ainsi que des rapports détaillés sur la
qualité du code. Ces rapports peuvent inclure des analyses de couverture de tests,
des contrôles de style de codage et d'autres indicateurs de qualité, améliorant la
compréhension du projet et encourageant le maintien d'un code de haute qualité.

L'adoption d'Apache Maven dans ce projet équipe les participants d'une compétence
prisée, augmentant leur attractivité auprès des employeurs potentiels et leur
fournissant les outils nécessaires pour gérer efficacement des projets logiciels.

Dans l'annexe C : "Étude du cours 'Organiser et packager une application Java avec
Apache Maven' de Loïc Guibert sur openclassrooms.com", nous apprendrons à utiliser
Apache Maven tout en étudiant le cours "Organisez et packagez une application Java
avec Apache Maven" sur openclassrooms.com.

## Conception

### Architecture multi-tiers

L'architecture de notre application sera structurée selon un modèle multi-tiers,
divisant l'application en trois niveaux d'abstraction distincts : la présentation,
la logique métier, et la persistance des données. Chacune de ces couches est
conçue pour opérer de manière autonome tout en interagissant efficacement avec les
autres, ce qui garantit une séparation nette des responsabilités et facilite la
maintenance du code.

Pour concrétiser cette architecture, nous utiliserons Maven pour créer cinq
modules distincts : batch, webapp, business, consumer, et model. Ces modules
représentent trois niveaux d'abstraction, partageant tous le module model, ce
dernier module gérant les objets les plus concrets de l'application, dénués
d'analyse et de logique métier. Ces objets, basiques et essentiels, peuvent être
manipulés à travers les trois niveaux d'abstraction suivant :

- Couche de persistance (consumer) :
  Contenant le module consumer, cette couche est responsable de l'interaction
  avec les bases de données, gérant le stockage et la récupération des données
  essentielles à l'application. Cette gestion de données permet d'économiser la
  mémoire et de sauvegarder les informations importantes en cas d'arrêt inattendu
  du serveur.

- Couche métier (business) :
  Utilisant les services de la couche de persistance, le module business au sein
  de cette couche est chargé de toute la logique métier de l'application. Par
  exemple, il détermine la légalité d'un coup dans une partie de Puissance 4.

- Couche de présentation (batch et webapp) :
  Cette couche contient les modules batch et webapp, qui exploitent les services
  de la couche métier. Le module webapp est conçu pour offrir une interface
  utilisateur interactive et attrayante, facilitant l'engagement des utilisateurs
  humains avec l'application. En parallèle, le module batch joue un rôle différent
  : il exécute un programme automatisé qui simule un utilisateur virtuel de
  l'application web. Ce programme, une fois qu'il termine une partie dans l'arène,
  en lance immédiatement une nouvelle, restant ainsi constamment actif pour
  proposer des parties aux utilisateurs réels. Pour prendre ses décisions de jeu,
  ce simulateur d'utilisateur fait appel au sous-package business.ai, exploitant
  ainsi l'algorithme d'intelligence artificielle min-max développé dans le cadre
  d'un autre projet stocké dans le répertoire LegacyMVC_Codebase dont on va
  ré-utiliser le code source comme expliqué dans la section "Réutilisation de code
  source". Cette approche offre une expérience interactive continue aux
  utilisateurs, leur permettant d'affronter une IA sophistiquée à tout moment.

En adoptant cette structure modulaire avec Maven, nous assurons une organisation
claire du projet, facilitant à la fois le développement et la maintenance. Chaque
module a un rôle défini et interagit avec les autres de manière cohérente,
reflétant la puissance et la flexibilité de l'architecture multi-tiers.

Dans l'annexe C : "Étude du cours 'Organiser et packager une application Java avec
Apache Maven' de Loïc Guibert sur openclassrooms.com", nous apprendrons à utiliser
Apache Maven pour construire un projet multi-module respectant l'architecture
multi-tiers tout en étudiant le cours "Organisez et packagez une application Java
avec Apache Maven" sur openclassrooms.com.


### Les patrons de conception DAO et Singleton

Les patrons de conception DAO (Data Access Object) seront mis en œuvre
pour encapsuler la logique d'accès aux données, offrant une abstraction
et une isolation par rapport à la source de données. Cela permet une
meilleure organisation du code et facilite les modifications et la
maintenance des opérations de base de données. Dans le package consumer,
nous créerons une classe DaoFactory qui suivra le pattern Singleton.
Cette approche garantira qu'une seule instance de DaoFactory sera créée
et partagée dans toute l'application. L'objectif est de centraliser la
création des objets DAO, permettant une gestion cohérente et efficace
des accès aux données, ainsi qu'une réduction de l'overhead lié à la
création répétée d'instances DAO.

Dans l'annexe B : "Étude du cours 'Développez des sites web avec Java EE'
de Mathieu Nebra sur openclassrooms.com pour développer une application
web proposant les services de lecture et d'écriture dans une table
d'une base de données MySQL tout en respectant MVC et DAO", nous
apprendrons en particulier à utiliser ces deux patrons de conception
tout en étudiant le cours "Développez des sites web avec Java EE" sur
openclassrooms.com.

### Diagramme de cas d'utilisations et diagrammes UML

Le diagramme de cas d'utilisation, le diagramme entités-associations, le diagramme
de classe et le diagramme de séquence joueront un rôle essentiel dans la conception
et la documentation de notre projet.

Nous apprendrons à utiliser le diagramme de cas d'utilisation en étudiant le
[livre "Use Case Modeling" de Kurt Bittner et Ian Spence](https://dokumen.pub/use-case-modeling-0201709139-9780201709131.html)
téléchargeable depuis [dokumen.pub/qdownload](https://dokumen.pub/qdownload).

Nous apprendrons à utiliser le diagramme entités-associations et le diagramme de
classe en étudiant le cours "UML 2 - De l'apprentissage à la pratique" sur
[laurent-audibert.developpez.com](https://laurent-audibert.developpez.com).

Et nous apprendrons à utiliser le diagramme de séquence en étudiant le cours
n°5 "Diagramme de séquences sur le langage UML" sur
[remy-manu.no-ip.biz](http://remy-manu.no-ip.biz).

#### Diagramme de cas d'utilisation

Ce diagramme est essentiel pour comprendre comment les utilisateurs interagiront
avec notre système. Il joue un rôle crucial dans la conception de la couche de
présentation, illustrant les différentes interactions que les utilisateurs
peuvent avoir avec l'application. Cela inclut la visualisation des
fonctionnalités requises et des flux de travail, offrant ainsi une vue d'ensemble
claire des opérations utilisateur et de l'interface utilisateur. Bien que
principalement axé sur la couche de présentation, ce diagramme aide également à
informer les besoins et les exigences de la couche métier, car il met en lumière
les opérations que cette dernière doit soutenir.

#### Diagramme entités associations

Ce diagramme est crucial pour la conception du module model et la structure de
la base de données de l'application. Il nous permet de structurer la base de
données en respectant la théorie de la normalisation, en évitant les redondances
et en assurant une maintenance aisée. Ce diagramme facilite une compréhension
claire de la manière dont les données sont organisées et interconnectées.

#### Diagramme de classes

Ce diagramme est utilisé pour détailler la structure des classes dans le
système, montrant les relations entre elles, leurs attributs et méthodes. Il
joue un rôle clé pour assurer une conception orientée objet cohérente et pour
faciliter l'implémentation du code.

#### Diagramme de séquence

Le diagramme de séquence sera utilisé pour décrire les interactions entre les
acteurs et le système dans le cadre de scénarios spécifiques. Il montre comment
les objets interagissent entre eux en termes de messages échangés, en suivant
l'ordre chronologique de ces interactions. Ce diagramme est particulièrement
utile pour modéliser le comportement des systèmes en visualisant la séquence des
appels de méthodes, les conditions de décision, et le flux de travail au sein
du système.

En représentant les interactions au niveau des instances d'objets dans un
scénario particulier, le diagramme de séquence aide à comprendre le flux de
l'application et les relations dynamiques entre ses différents composants. Cela
est essentiel pour la conception des fonctionnalités complexes et pour assurer
une coordination efficace entre les couches de l'application lors de
l'implémentation du code.

### Application du framework à la collaboration

#### Collaboration avec les créateurs du framework

Dans le monde complexe et interconnecté du développement moderne, la création en
solo est devenue une notion presque obsolète. Même lorsqu'un projet semble être
l'œuvre d'un individu, il s'inscrit inévitablement dans un réseau de
collaborations implicites et explicites. Prenez l'exemple simple de l'utilisation
d'un outil de développement : chaque fois que nous utilisons un framework, une
bibliothèque ou un logiciel, nous entrons en collaboration indirecte avec ses
créateurs. Ces outils sont le fruit de connaissances accumulées, d'expertises
partagées et d'efforts collectifs. En les utilisant, nous nous appuyons sur le
génie et l'ingéniosité de nombreux autres esprits, créant ainsi une synergie qui
transcende les frontières individuelles. Cette réalité met en lumière
l'importance fondamentale de la collaboration dans le domaine du développement de
logiciels, soulignant que chaque création est, en quelque sorte, un effort
collectif.

#### Rôle de la conception dans la collaboration

La phase de conception dans le développement de logiciels joue un rôle crucial en
matière de collaboration, agissant comme la pierre angulaire pour établir et
partager une vision commune parmi toutes les parties prenantes. Cette étape est
essentielle non seulement pour faciliter la collaboration entre les développeurs,
mais également pour impliquer les parties prenantes non techniques dans le
processus de création. Elle assure une compréhension claire et partagée des
objectifs et fonctionnalités du logiciel, ce qui est vital pour le succès du
projet.

Par exemple, les diagrammes présentés dans la section "Diagramme de cas
d'utilisations et diagrammes UML" ne sont pas uniquement des outils de conception
; ils constituent également des moyens de collaboration efficaces. Ils permettent
aux développeurs de partager une vision commune de la structure et de la logique
du système, favorisant ainsi une communication claire et une compréhension
partagée. Cette approche collaborative est indispensable pour garantir que tous
les développeurs soient alignés sur les objectifs du projet, réduisant ainsi les
risques de malentendus et d'erreurs pendant le développement.

Ces diagrammes, et en particulier le diagramme de cas d'utilisation, jouent un
rôle crucial non seulement pour la conception technique, mais aussi pour la
collaboration étendue avec toutes les parties prenantes du projet. Ils
garantissent que les développeurs, les représentants des utilisateurs, les
investisseurs, l'équipe marketing, l'équipe de vente, les spécialistes de la
publicité, et même les juristes impliqués dans les questions de propriété
intellectuelle et de conformité légale partagent une vision commune du projet.

Comme l'explique en détail le livre "Use Case Modeling" de Kurt Bittner et Ian
Spence téléchargeable depuis dokumen.pub/qdownload, le diagramme de cas
d'utilisation est particulièrement utile pour cette collaboration étendue. Il
présente une vue globale et accessible de l'application, en termes compréhensibles
pour tous. Cela aide les parties prenantes non techniques, telles que les équipes
de vente et de marketing, à comprendre le produit et à élaborer des stratégies
efficaces pour sa promotion et sa vente. De plus, il fournit aux investisseurs et
aux juristes une vision claire de la portée et des fonctionnalités du projet,
facilitant les discussions sur le financement, les droits de propriété
intellectuelle, et la conformité réglementaire.

Cette approche collaborative globale est essentielle pour le succès du projet.
Elle assure que tous les aspects, des attentes des utilisateurs aux contraintes
légales, sont pris en compte dès le début du processus de développement. Cela
minimise les risques de malentendus, d'ajustements tardifs coûteux et de
non-conformité.


### Rôle de la spécification dans la collaboration

Dans notre projet, l'adoption de la spécification Jakarta EE joue un rôle crucial
dans la facilitation de la collaboration. Un exemple concret est l'utilisation
des Jakarta Servlets, une partie intégrante de Jakarta EE, pour le traitement des
requêtes et des réponses sur le serveur. Cette spécification offre un cadre
standardisé pour développer ces composants, garantissant ainsi que tous les
développeurs suivent les mêmes pratiques et standards.

Cette uniformité est essentielle pour la collaboration. En travaillant avec les
Jakarta Servlets, chaque membre de l'équipe comprend non seulement comment
implémenter les fonctionnalités serveur de manière cohérente, mais aussi comment
ces composants s'intègrent dans l'architecture globale de l'application. Cela
réduit significativement les risques de conflits dans le code et assure une
meilleure cohérence dans le développement.

En somme, la spécification Jakarta EE, notamment à travers les Jakarta Servlets,
offre un langage commun et des directives claires, facilitant une collaboration
efficace et harmonieuse au sein de notre équipe de développement.

### Réutilisation de code source

La réutilisation de code source est un aspect fondamental de la collaboration en
développement logiciel, que ce soit avec d'autres développeurs ou pour soi-même
dans des projets futurs. Elle souligne l'importance d'une bonne conception,
respectueuse des bonnes pratiques et accompagnée de commentaires pertinents, pour
faciliter la compréhension et l'adaptation du code.

Dans le cadre de notre projet, nous avons intégré une application préexistante,
contenue dans le répertoire `LegacyMVC_Codebase`. Ce code, développé en Java pour
un cours de programmation objet et basé sur le modèle MVC (Modèle-Vue-Contrôleur),
propose une expérience de jeu contre un autre utilisateur ou une intelligence
artificielle utilisant l'algorithme min-max. La réutilisation de ce code
préexistant offre une opportunité précieuse d'apprendre à travailler avec des
bases de code existantes, mettant en lumière l'importance de la documentation et
de la qualité de conception initiale.

En réutilisant ce code, nous pouvons non seulement gagner du temps de
développement, mais aussi analyser et apprendre de la structure MVC utilisée.
Cela nous permet de contraster cette architecture avec l'architecture trois-tiers
que nous adoptons pour notre nouveau projet, fournissant ainsi une compréhension
approfondie des différences et avantages de chaque approche.

Pour construire, exécuter puis nettoyer le projet du répertoire
`LegacyMVC_Codebase`, suivez ces étapes :
- installer Java
- se positionner dans le répertoire `LegacyMVC_Codebase`
- pour compiler le programme, soumettre au terminal la commande :
  `javac *.java $(find ../LegacyMVC_Codebase -type f -name "*.java") -cp "../:*"`
- pour exécuter le programme, soumettre au terminal la commande :
  `java Main`
- pour nettoyer le projet, soumettre au terminal la commande :
  `find ../LegacyMVC_Codebase -type f -name "*.class" -delete`

### Application de la gestion des versions à la collaboration

#### Utilisation du logiciel de gestion des versions Git

Dans notre projet, Git joue un rôle clé en facilitant la collaboration des
développeurs. Git permet à chaque développeur de travailler sur une fonctionnalité
spécifique dans une branche séparée. Ces branches permettent aux développeurs de
travailler de manière isolée, sans affecter le code principal. Lorsqu'un
développeur termine son travail sur une fonctionnalité, il effectue une fusion de
sa branche avec la branche principale. Cette fusion est le processus d'agrégation
du travail effectué dans les différentes branches, permettant d'assembler les
fonctionnalités développées séparément en un produit cohérent. Cette méthode
assure que le code principal reste stable tout en permettant l'intégration
progressive des nouvelles fonctionnalités.

#### Utilisation d'un service de gestion de version et d'hébergement de code source GitHub

GitHub, quant à lui, agit comme une plateforme centralisée où ce code est stocké
et partagé. Il permet à tous les membres de l'équipe de voir le travail en cours,
d'accéder aux différentes branches et de suivre les modifications effectuées via
les commits. En utilisant GitHub, les développeurs peuvent facilement collaborer,
peu importe leur emplacement géographique. Ils peuvent également voir l'historique
complet des changements et gérer les différentes versions du projet. Ceci est
crucial pour synchroniser le travail d'équipe, intégrer les contributions de
chaque membre et maintenir une vue d'ensemble claire de l'évolution du projet.

### Rôle des bonnes pratiques dans la collaboration

Pour garantir le succès d'un projet de développement logiciel, il est crucial
d'adopter des bonnes pratiques de conception et de développement. Chacune de ces
pratiques joue un rôle spécifique dans la facilitation de la collaboration au
sein de l'équipe. Notre projet intègre ces bonnes pratiques pour créer un logiciel
modulaire et facile à maintenir, ce qui est fondamental dans un environnement de
développement agile.

#### Séparation des préoccupations

Dans notre projet, la séparation des préoccupations est illustrée par l'architecture
multi-tiers, où nous avons distinctement séparé les couches Présentation, Métier
et Persistence. Par exemple, les modules webapp et webapp.servlet dans la couche
Présentation sont clairement dissociés des modules business et business.ai dans
la couche Métier. Cette séparation facilite les mises à jour indépendantes et
améliore la maintenabilité du code.

#### Couplage faible

Le principe de couplage faible est mis en œuvre dans notre projet par la réduction
des dépendances entre les différents modules et packages. Par exemple, le module
consumer dans la couche Persistence fonctionne indépendamment des modules dans
les autres couches. Cela permet des développements et des tests isolés, réduisant
les risques de conflits dans le code.

#### Gestion efficace des versions

Notre utilisation de Git et GitHub pour la gestion de versions illustre l'importance
de cette bonne pratique. Ces outils nous permettent de gérer efficacement les
différentes branches de développement, facilitant ainsi la collaboration et
l'intégration des fonctionnalités développées parallèlement.

#### Autres Pratiques Essentielles

D'autres pratiques, comme la génération de documentation et de rapports sur la
qualité du code via Maven, jouent un rôle crucial dans notre projet. Elles garantissent
que le code reste accessible et compréhensible pour tous les membres de l'équipe,
favorisant ainsi une collaboration continue et adaptative. De plus, l'adoption de
patrons de conception tels que DAO et Singleton dans notre architecture contribue
à une structure de code cohérente et efficace.

## Utilisation du système de gestion de bases de données relationnelles MySQL

Nous avons choisi MySQL, un système de gestion de bases de données relationnelles,
pour le stockage et la gestion des données de notre application. Cette technologie
nous permet de structurer les données de manière efficace, en assurant l'intégrité,
la pérennité et la sécurité des informations traitées.

Dans la section "Création de la base de donnée", nous détaillons le processus de
création de la base de données onlineplay, ainsi que l'implémentation de quatre
tables principales : connections, users, games, et moves. Ces tables sont conçues
pour stocker respectivement les données concernant les connexions (y compris
celles non authentifiées), les utilisateurs, les parties de jeu, et les mouvements
effectués pendant les jeux.

Un aspect crucial de la conception de notre base de données est l'engagement à
respecter les principes de la normalisation. La normalisation est une théorie
fondamentale dans la conception de bases de données relationnelles, visant à
réduire la redondance des données et à améliorer l'intégrité des données à travers
une série de règles normalisées ou formes normales. En suivant ces principes, nous
avons structuré nos tables de manière à éliminer la duplication des informations,
faciliter la maintenance de la base de données et garantir la cohérence des données.

Chaque table a été méticuleusement conçue pour suivre les formes normales, avec
l'objectif de minimiser les dépendances inutiles, faciliter les requêtes, et
assurer que les mises à jour de la base de données ne conduisent pas à des
anomalies. Cette approche de la conception garantit non seulement une performance
optimale de notre système de gestion de base de données mais contribue également à
une meilleure expérience utilisateur en fournissant des données précises et à jour.

## Utilisation de l'API AbuseIPDB pour la sécurité Web

Pour renforcer la sécurité de notre application web, nous avons intégré l'API
AbuseIPDB, un outil en ligne qui aide à identifier les adresses IP associées à des
comportements malveillants. Cette API nous permet de vérifier si une adresse IP
tentant d'accéder à notre application a été impliquée dans des activités suspectes
ou nuisibles.

Nous avons choisi d'intégrer l'API directement dans notre application au lieu de
l'utiliser via un proxy inversé autour du serveur Tomcat. Cette décision nous a
permis de gérer plus directement les réponses de l'API et d'ajouter cette
fonctionnalité comme une expérience d'apprentissage dans la gestion des API tierces.
Les interactions avec AbuseIPDB sont gérées par le sous-package
consumer.api.abuseipdb, qui fait partie du package plus large
consumer.api, dédié à l'exploitation des services proposés par des
API externes.

Bien qu'intégrer l'API AbuseIPDB directement dans l'application ait des avantages
éducatifs, en rétrospective, placer cette fonctionnalité derrière un proxy inversé
aurait pu offrir une séparation plus claire entre les préoccupations de sécurité et
les fonctionnalités de l'application. Cela aurait également permis une
centralisation de la journalisation des incidents de sécurité, séparée des journaux
standards de l'application web.

## Utilisation des environnements de développement Eclipse et IntelliJ

Le développement de notre application s'effectuera en utilisant les IDE Eclipse
et IntelliJ IDEA, qui offrent des fonctionnalités avancées pour la programmation
et la gestion de projets.

## Déploiement pour le travail en développement

### Utilisation d'une plateforme de déploiement fournie par le serveur d'application Tomcat

Notre projet fera appel à Apache Tomcat, un serveur d'applications web léger et
open-source, qui servira de plateforme de déploiement pour notre application web.

## Déploiement pour le travail en production

Le déploiement de l'application web par un serveur Tomcat hébergé sur un droplet
fournie par le service d'hébergement de machine virtuelle Digital Ocean est décrit
en détail dans la section "Déploiement sur un droplet de Digital Ocean".

### Utilisation du service d'hébergement de machine virtuelle DigitalOcean

Pour l'hébergement de notre application web, nous avons choisi DigitalOcean en
raison de sa simplicité d'utilisation, de sa tarification transparente et de ses
performances fiables. DigitalOcean offre une gamme de droplets (machines
virtuelles), ce qui nous permet de sélectionner une configuration qui correspond
aux besoins spécifiques de notre projet en termes de mémoire, de CPU, et de
stockage.

L'avantage principal de DigitalOcean réside dans sa facilité de configuration et
de gestion des machines virtuelles. Cela nous permet de déployer rapidement notre
application web sans la complexité souvent associée à la configuration et à la
gestion d'infrastructures serveur. De plus, DigitalOcean propose une intégration
facile avec divers outils de développement et de déploiement, ce qui est idéal
pour automatiser et simplifier nos processus de CI/CD (Intégration Continue et
Déploiement Continu).

Un autre aspect important est la tarification prévisible de DigitalOcean, ce qui
aide à maîtriser les coûts du projet, surtout important pour les projets ayant
des budgets limités. En outre, DigitalOcean offre un excellent support technique
et une vaste communauté d'utilisateurs, ce qui est un atout précieux pour
résoudre rapidement les problèmes et partager les meilleures pratiques.

### Utilisation du registraire de domaine Namecheap

Pour la gestion de notre nom de domaine, nous avons opté pour Namecheap, un
registraire de domaine réputé pour sa facilité d'utilisation, ses prix compétitifs,
et son support client de qualité. Namecheap offre une large gamme de services en
plus de l'enregistrement de domaines, incluant l'hébergement web, la sécurité par
SSL, et les services de messagerie professionnelle, ce qui en fait une solution
complète pour les besoins en ligne de notre projet.

Un aspect crucial de l'utilisation de Namecheap est sa facilité de gestion des
enregistrements DNS, ce qui permet de diriger facilement le trafic de notre nom de
domaine vers notre application hébergée sur DigitalOcean. C'est l'objet de la
section "Configuration DNS".

Bien que Namecheap offre également la possibilité de protéger notre domaine avec
des certificats SSL, essentiels pour sécuriser notre application web et gagner la
confiance des utilisateurs, nous utiliserons plutôt l'autorité de certification
*Let's Encrypt* dans la section "Obtention d'un certificat SSL/TLS" pour obtenir une
clé privée et le certificat correspondant (comprenant notamment la clé publique
associée) attestant de l'identité de notre entreprise et permettant de sécuriser
les messages en les chiffrant via le protocole SSL/TLS.

### Utilisation des serveurs DNS

Dans la section "Configuration DNS", nous utilisons les services offerts par le web
GUI de Digital Ocean pour faire en sorte que les requêtes DNS résolvent le nom de
domaine `caltuli.online` en l'adresse IP du droplet hébergeant le serveur Tomcat
déployant l'application web. Nous utilisons ainsi les serveurs DNS du registraire
de domaine NameCheap qui vont clore le processus récursif de résolution du nom de
domaine mais aussi tous les autres serveurs DNS correspondant au niveau plus
élevés, les serveurs DNS TLD (Top Domain Level) pour résoudre le nom de domaine
`online` et les serveurs DNS Root pour commencer la requête récursive ("pour
résoudre le domaine racine `.`").

### Utilisation de l'autorité de certification Let's Encrypt

Dans la section "Obtention d'un certificat SSL/TLS", nous utiliserons l'autorité
de certification *Let's Encrypt* pour générer notamment une clé privée et le
certificat correspondant (comprenant notamment la clé publique associée) attestant
de l'identité de notre entreprise.

Ce certificat permet d'une part de fournir une preuve de notre identité auprès des
clients et, d'autre part, de chiffrer les échanges avec eux afin qu'ils puissent
par exemple en toute sécurité transmettre des informations sensibles comme leur
mot de passe puisque transportées dans des trames encapsulant des SDU(7) qui ne
peuvent être déchiffrés que par le détenteur de la clé privée, notre entreprise.

### Utilisation des filtres servlet de la spécification Jakarta EE

Dans la section "Implémentation de la Redirection HTTPS", nous avons intégré un
filtre, via la classe HttpsRedirectFilter, qui joue un rôle crucial dans la
sécurisation des communications entre le client et le serveur. Ce filtre est
conçu pour intercepter toutes les requêtes HTTP et rediriger automatiquement les
utilisateurs vers l'URL sécurisée en HTTPS, garantissant ainsi que les données
échangées sont toujours chiffrées.


-   [Accès et ressources du projet](acces-ressources-projet.md)
-   [Inventaire des outils et technologies utilisés](inventaire-outils-technologies-utilises.md)
-   [Construction de l'environnement de développement](construction-environnement-developpement.md)

-   [Création de la base de données](creation-base-de-donnees.md)
-   [Initialisation du projet Maven et référencement du premier commit Git](initialisation-projet-maven-et-referencement-premier-commit-git.md)
-   [Téléversement du projet Git dans un dépôt de GitHub](televersement-projet-git-dans-depot-github.md)
-   [Déploiement sur un droplet de Digital Ocean](deploiement-sur-droplet-de-digitalocean.md)

-   [Annexe A : Liens étudiés](annexe-a-liens-etudies.md)
-   [Annexe B : Étude du cours "Développez des sites web avec Java EE" de Mathieu
    Nebra sur openclassrooms.com pour développer une application web pro-
    posant les services de lecture et d’écriture dans une table d’une base de
    données MySQL tout en respectant MVC et DAO](annexe-b-etude-cours-nebra.md)
-   [Annexe C : Étude du cours "Organiser et packager une application Java avec 
Apache Maven" de Loïc Guibert sur openclassrooms.com](annexe-c-etude-cours-loic-guibert.md)

## Utilisation du mécanisme CDI (Context and Dependency Injection)

TO DO

## Utilisation des websockets

TO DO

## Utilisation de React

TO DO


