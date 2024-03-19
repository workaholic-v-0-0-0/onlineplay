# Présentation générale du projet

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

## Étude et expérimentation d'un framework pour la réalisation collaborative d'une application web

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

## Spécification Jakarta EE

Pour ce projet, nous avons choisi de nous appuyer sur la spécification Jakarta
EE, l'évolution de Java EE, qui représente le pilier des applications
d'entreprise modernes en Java. Une spécification est un ensemble de directives
et de normes qui définissent comment une technologie doit être utilisée et mise
en oeuvre, garantissant la standardisation et la compatibilité entre différents
développements.

Jakarta EE est une spécification riche et étendue, conçue pour fournir un cadre
robuste pour le développement d'applications d'entreprise. Elle englobe une
variété de technologies et de composants essentiels pour construire des
applications web et d'entreprise évolutives et performantes, avec les servlets
et les JavaServer Pages (JSP) comme éléments centraux.

Dans notre projet, l'adoption de Jakarta EE nous permettra de tirer parti de ses
nombreuses fonctionnalités pour développer une application web Puissance 4,
mettant un accent particulier sur la modularité, la sécurité, et la performance.

Nous explorerons l'utilisation de Jakarta EE en étudiant le cours "Développez
des sites web avec Java EE" sur openclassrooms.com, permettant ainsi d'acquérir
une compétence valorisée dans le milieu professionnel.

## Utilisation du logiciel de gestion de développement de projets logiciels Apache Maven

Apache Maven jouera un rôle central dans notre projet, grâce à sa capacité à
standardiser et gérer des structures de projets complexes. Son approche de la
gestion des dépendances et de la standardisation des processus de construction
est hautement appréciée dans le secteur professionnel.

Maven structure les projets de manière claire et ordonnée, favorisant les bonnes
pratiques de développement et assurant la cohérence et la maintenabilité du
code. Il facilite également l'intégration continue et la livraison continue,
automatisant les tests et le déploiement pour minimiser les erreurs humaines.

Une caractéristique notable de Maven est sa capacité à générer un site de
documentation automatique et des rapports détaillés sur la qualité du code, ce
qui améliore la compréhension du projet et encourage le maintien d'un code de
haute qualité.

L'utilisation d'Apache Maven équipera les participants d'une compétence prisée
par les employeurs, tout en fournissant les outils nécessaires pour gérer
efficacement des projets logiciels. Nous apprendrons à utiliser Maven à travers
le cours "Organisez et packagez une application Java avec Apache Maven" sur
openclassrooms.com.

## Conception

### Architecture multi-tiers

L'architecture de notre application sera structurée selon un modèle multi-tiers,
divisant l'application en trois niveaux d'abstraction distincts : la
présentation, la logique métier, et la persistance des données. Cette approche
garantit une séparation nette des responsabilités et facilite la maintenance du
code.

Nous utiliserons Maven pour créer cinq modules distincts : batch, webapp,
business, consumer, et model. Ces modules correspondent aux trois niveaux
d'abstraction, avec tous les modules partageant le module model, qui gère les
objets concrets de l'application.

- **Couche de persistance (consumer)**: Cette couche gère le stockage et la
  récupération des données, permettant d'économiser la mémoire et de sauvegarder
  les informations importantes.

- **Couche métier (business)**: Chargée de la logique métier de l'application,
  cette couche utilise les services de la couche de persistance pour déterminer
  la légalité des coups dans une partie de Puissance 4.

- **Couche de présentation (batch et webapp)**: Contenant les modules batch et
  webapp, cette couche offre une interface utilisateur interactive et un
  simulateur d'utilisateur qui exécute un programme automatisé pour simuler une
  partie contre l'IA, utilisant l'algorithme min-max.

Cette structure modulaire assure une organisation claire du projet, facilitant
le développement et la maintenance. Chaque module a un rôle défini et interagit
avec les autres de manière cohérente, reflétant la puissance et la flexibilité
de l'architecture multi-tiers.

Nous apprendrons à utiliser Apache Maven pour construire un projet multi-module
respectant cette architecture en étudiant le cours "Organisez et packagez une
application Java avec Apache Maven" sur openclassrooms.com.

### Les patrons de conception DAO et Singleton

Les patrons de conception DAO (Data Access Object) seront utilisés pour
encapsuler la logique d'accès aux données, offrant une abstraction et une
isolation vis-à-vis de la source de données. Cela facilite une meilleure
organisation du code et simplifie les modifications et la maintenance des
opérations de base de données.

Dans le package consumer, une classe DaoFactory suivant le patron de conception
Singleton sera créée. Cette méthode garantira qu'une seule instance de DaoFactory
soit créée et partagée à travers toute l'application, centralisant ainsi la
création des objets DAO. Ceci permet une gestion cohérente et efficace des accès
aux données et réduit l'overhead lié à la création répétée d'instances DAO.

Nous approfondirons l'utilisation de ces deux patrons de conception en étudiant
le cours "Développez des sites web avec Java EE" de Mathieu Nebra sur
openclassrooms.com. Ce cours couvre le développement d'une application web qui
propose des services de lecture et d'écriture dans une table d'une base de
données MySQL tout en respectant les principes MVC et DAO.

### Diagramme de cas d'utilisations et diagrammes UML

Les diagrammes de cas d'utilisation, entités-associations, et de classes joueront
un rôle crucial dans la conception et la documentation de notre projet. Ils
aideront à visualiser et à structurer clairement les différents aspects du
système.

#### Diagramme de cas d'utilisation

Ce diagramme est essentiel pour comprendre l'interaction des utilisateurs avec
notre système. Il joue un rôle crucial dans la conception de la couche de
présentation, en illustrant les interactions possibles avec l'application. Cela
nous offre une vue d'ensemble des fonctionnalités requises et des flux de
travail, clarifiant ainsi les besoins et les exigences tant de la couche de
présentation que de la couche métier.

#### Diagramme entités associations

Crucial pour la conception du module model et la structure de la base de données,
ce diagramme nous aide à organiser la base de données en respectant la théorie de
la normalisation. Il permet d'éviter les redondances et assure une maintenance
facile, offrant une compréhension claire de l'organisation et de l'interconnexion
des données.

#### Diagramme de classes

Utilisé pour détailler la structure des classes dans le système, ce diagramme
montre les relations entre elles ainsi que leurs attributs et méthodes. Il est
clé pour assurer une conception orientée objet cohérente et facilite
l'implémentation du code.


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

## Application du framework à la collaboration

### Collaboration avec les créateurs du framework

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

### Rôle de la conception dans la collaboration

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
collaboration étendue avec toutes les parties prenantes du projet. Ils garantissent
que les développeurs, les représentants des utilisateurs, les investisseurs,
l'équipe marketing, l'équipe de vente, les spécialistes de la publicité, et même
les juristes impliqués dans les questions de propriété intellectuelle et de
conformité légale partagent une vision commune du projet.

Comme l'explique en détail le livre "Use Case Modeling" de Kurt Bittner et Ian
Spence téléchargeable depuis dokumen.pub/qdownload, le diagramme de cas d'utilisation
est particulièrement utile pour cette collaboration étendue. Il présente une vue
globale et accessible de l'application, en termes compréhensibles pour tous.
Cela aide les parties prenantes non techniques, telles que les équipes de vente
et de marketing, à comprendre le produit et à élaborer des stratégies efficaces
pour sa promotion et sa vente. De plus, il fournit aux investisseurs et aux
juristes une vision claire de la portée et des fonctionnalités du projet,
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

Nous intégrerons MySQL, un système de gestion de base de données relationnelle,
pour le stockage et la gestion des données de notre application.

## Utilisation des environnements de développement Eclipse et IntelliJ

Le développement de notre application s'effectuera en utilisant les IDE Eclipse
et IntelliJ IDEA, qui offrent des fonctionnalités avancées pour la programmation
et la gestion de projets.

## Déploiement pour le travail en développement

### Utilisation d'une plateforme de déploiement fournie par le serveur d'application Tomcat

Notre projet fera appel à Apache Tomcat, un serveur d'applications web léger et
open-source, qui servira de plateforme de déploiement pour notre application web.

## Utilisation du système de gestion de bases de données relationnelles MySQL

Nous intégrerons MySQL, un système de gestion de base de données relationnelle,
pour le stockage et la gestion des données de notre application.

## Utilisation des environnements de développement Eclipse et IntelliJ

Le développement de notre application s'effectuera en utilisant les IDE Eclipse
et IntelliJ IDEA, qui offrent des fonctionnalités avancées pour la programmation
et la gestion de projets.

## Déploiement pour le travail en développement

### Utilisation d'une plateforme de déploiement fournie par le serveur d'application Tomcat

Notre projet fera appel à Apache Tomcat, un serveur d'applications web léger et
open-source, qui servira de plateforme de déploiement pour notre application web.

## Déploiement pour le travail en production

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

[TO DO]


-   [Outils utilisés](outils-utilises/outils-utilises.md)
