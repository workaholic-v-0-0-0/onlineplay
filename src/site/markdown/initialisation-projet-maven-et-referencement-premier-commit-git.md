# Initialisation du projet Maven et référencement du premier commit Git

- **Configuration de Git**
    ```sh
    git config --global user.name "Caltuli"
    git config --global user.email "lucas.caltuli@gmail.com"
    ```

- **Création d'un projet Maven**
  - **Initialisation d'un nouveau projet Maven dans $HOME/env/maven/maven-workspace/**
  ```sh
  cd $HOME/env/maven/maven-workspace
  mvn archetype:generate \
  -DarchetypeGroupId=org.apache.maven.archetypes \
  -DarchetypeArtifactId=maven-archetype-quickstart \
  -DarchetypeVersion=1.1 \
  -DgroupId=online.caltuli \
  -DartifactId=onlineplay \
  -Dversion=1.0-SNAPSHOT
  rm -r onlineplay/src/
  ```
  - **Dans $HOME/env/maven/maven-workspace/onlineplay/pom.xml, on change le &lt;packaging&gt; en pom.**

- **Ajout des modules :**
  - **Dans \$HOME/env/maven/maven-workspace/onlineplay, ajout des modules suivants :**
    ```sh
    cd $HOME/env/maven/maven-workspace/onlineplay
    ## module : batch
    mvn -B archetype:generate \
      -DarchetypeGroupId=org.apache.maven.archetypes \
      -DarchetypeArtifactId=maven-archetype-quickstart \
      -DarchetypeVersion=1.1 \
      -DgroupId=online.caltuli \
      -DartifactId=batch \
      -Dpackage=online.caltuli.batch
    
    ## module : webapp
    mvn -B archetype:generate \
      -DarchetypeGroupId=org.apache.maven.archetypes \
      -DarchetypeArtifactId=maven-archetype-webapp \
      -DarchetypeVersion=1.4 \
      -DgroupId=online.caltuli \
      -DartifactId=webapp \
      -Dpackage=online.caltuli.webapp
    
    ## module : business
    mvn -B archetype:generate \
      -DarchetypeGroupId=org.apache.maven.archetypes \
      -DarchetypeArtifactId=maven-archetype-quickstart \
      -DarchetypeVersion=1.1 \
      -DgroupId=online.caltuli \
      -DartifactId=business \
      -Dpackage=online.caltuli.business
    
    ## module : consumer
    mvn -B archetype:generate \
      -DarchetypeGroupId=org.apache.maven.archetypes \
      -DarchetypeArtifactId=maven-archetype-quickstart \
      -DarchetypeVersion=1.1 \
      -DgroupId=online.caltuli \
      -DartifactId=consumer \
      -Dpackage=online.caltuli.consumer
    
    ## module : model
    mvn -B archetype:generate \
      -DarchetypeGroupId=org.apache.maven.archetypes \
      -DarchetypeArtifactId=maven-archetype-quickstart \
      -DarchetypeVersion=1.1 \
      -DgroupId=online.caltuli \
      -DartifactId=model \
      -Dpackage=online.caltuli.model
    ```

- **Définition des dépendances entre les modules :**
  - **Dans le POM du projet parent onlineplay/pom.xml, on liste les modules en tant que dépendances pour le projet :**
    ```xml
    <!-- =============================================================== -->
    <!-- Dependency Management -->
    <!-- =============================================================== -->
    <dependencyManagement>
      <dependencies>
        <!-- ===== Modules ===== -->
        <dependency>
          <groupId>online.caltuli</groupId>
          <artifactId>batch</artifactId>
        </dependency>
        <dependency>
          <groupId>online.caltuli</groupId>
          <artifactId>webapp</artifactId>
        </dependency>
        <dependency>
          <groupId>online.caltuli</groupId>
          <artifactId>business</artifactId>
        </dependency>
        <dependency>
          <groupId>online.caltuli</groupId>
          <artifactId>consumer</artifactId>
        </dependency>
        <dependency>
          <groupId>online.caltuli</groupId>
          <artifactId>model</artifactId>
        </dependency>
      </dependencies>
    </dependencyManagement>
    ```
  - **On ajoute des dépendances aux modules suivant l'architecture multi-tiers :**
    - **Dans onlineplay/batch/pom.xml :**
      ```xml
      <!-- =============================================================== -->
      <!-- Dependency Management -->
      <!-- =============================================================== -->
      <dependencies>
        <!-- ===== Modules ===== -->
        <dependency>
          <groupId>online.caltuli</groupId>
          <artifactId>business</artifactId>
        </dependency>
        <dependency>
          <groupId>online.caltuli</groupId>
          <artifactId>model</artifactId>
        </dependency>
        <!-- ===== Third-party Libraries ===== -->
      </dependencies>
      ```
    - **Dans onlineplay/webapp/pom.xml :**
      ```xml
      <!-- =============================================================== -->
      <!-- Dependency Management -->
      <!-- =============================================================== -->
      <dependencies>
       <!-- ===== Modules ===== -->
        <dependency>
          <groupId>online.caltuli</groupId>
          <artifactId>business</artifactId>
        </dependency>
        <dependency>
          <groupId>online.caltuli</groupId>
          <artifactId>model</artifactId>
        </dependency>
        <!-- ===== Third-party Libraries ===== -->
      </dependencies>
      ```
    - **Dans onlineplay/business/pom.xml :**
      ```xml
      <!-- =============================================================== -->
      <!-- Dependency Management -->
      <!-- =============================================================== -->
      <dependencies>
        <!-- ===== Modules ===== -->
        <dependency>
          <groupId>online.caltuli</groupId>
          <artifactId>consumer</artifactId>
        </dependency>
        <dependency>
          <groupId>online.caltuli</groupId>
          <artifactId>model</artifactId>
        </dependency>
        <!-- ===== Third-party Libraries ===== -->
      </dependencies>
      ```
    - **Dans onlineplay/consumer/pom.xml :**
      ```xml
      <!-- =============================================================== -->
      <!-- Dependency Management -->
      <!-- =============================================================== -->
      <dependencies>
        <!-- ===== Modules ===== -->
        <dependency>
          <groupId>online.caltuli</groupId>
          <artifactId>model</artifactId>
        </dependency>
        <!-- ===== Third-party Libraries ===== -->
      </dependencies>
      ```

- **Création des sous-packages webapp.servlet, business.ai et batch.ai :**
    ```sh
    mkdir ../onlineplay/batch/src/main/java/online/caltuli/batch/ai
    mkdir -p ../onlineplay/webapp/src/main/java/online/caltuli/webapp/servlet
    mkdir ../onlineplay/business/src/main/java/online/caltuli/business/ai
    ```

- **Construction du projet Maven :**
    ```sh
    mvn package
    ```

- **Initialisation du répertoire Git :**
    ```sh
    cd $HOME/env/maven/maven-workspace/onlineplay
    git init
    git add .
    git commit -m "Initial commit"
    ```

- **Apprentissage de Git via quelques utilisations élémentaires :**
  - **Ajout et commit de répertoires, navigation entre les commits :**
    ```plaintext
    $ cd $HOME/env/maven/maven-workspace/onlineplay/
    $ git add docs/conception/schemas
    $ git commit -m "Ajout des schémas de conception dans docs/conception/schemas"
    [master b05b44f] Ajout des schémas de conception dans docs/conception/schemas
     6 files changed, 2294 insertions(+)
     create mode 100644 docs/conception/schemas/erd.mwb
     create mode 100644 docs/conception/schemas/erd.mwb.bak
     create mode 100644 docs/conception/schemas/erd.uxf
     create mode 100644 docs/conception/schemas/ucd.uxf
     create mode 100644 docs/conception/schemas/uml.pdf
     create mode 100644 docs/conception/schemas/uml.uxf
    $ git status
    Sur la branche master
    rien à valider, la copie de travail est propre
    $ git log
    commit b05b44f52e3d202286f633ff9e2c4edf3f9681f6 (HEAD -> master)
    Author: Caltuli <lucas.caltuli@gmail.com>
    Date:   Sat Jan 6 20:27:58 2024 +0100

        Ajout des schémas de conception dans docs/conception/schemas

    commit 531ef7a5b4376dc93c8295aff0cdbedf07aff510
    Author: Caltuli <lucas.caltuli@gmail.com>
    Date:   Sat Jan 6 20:06:56 2024 +0100

        Initial commit
    $ git checkout 531ef7a5b4376dc93c8295aff0cdbedf07aff510
    Note : basculement sur '531ef7a5b4376dc93c8295aff0cdbedf07aff510'.

    Vous êtes dans l'état « HEAD détachée ». Vous pouvez visiter, faire des modifications
    expérimentales et les valider. Il vous suffit de faire un autre basculement pour
    abandonner les commits que vous faites dans cet état sans impacter les autres branches

    Si vous voulez créer une nouvelle branche pour conserver les commits que vous créez,
    il vous suffit d'utiliser l'option -c de la commande switch comme ceci :

      git switch -c <nom-de-la-nouvelle-branche>

    Ou annuler cette opération avec :

      git switch -

    Désactivez ce conseil en renseignant la variable de configuration advice.detachedHead à false

    HEAD est maintenant sur 531ef7a Initial commit
    $ ls
    batch  business  consumer  model  pom.xml  webapp
    $ git log
    commit 531ef7a5b4376dc93c8295aff0cdbedf07aff510 (HEAD)
    Author: Caltuli <lucas.caltuli@gmail.com>
    Date:   Sat Jan 6 20:06:56 2024 +0100

        Initial commit
    $ git checkout master
    La position précédente de HEAD était sur 531ef7a Initial commit
    Basculement sur la branche 'master'
    $ ls
    batch  business  consumer  docs  model  pom.xml  webapp
    $
    ```

- **Apprentissage de Git via quelques utilisations élémentaires :**
  - **Ajout et commit de répertoires, navigation entre les commits :**
    ```plaintext
    $ cd $HOME/env/maven/maven-workspace/onlineplay/
    $ git add docs/conception/schemas
    $ git commit -m "Ajout des schémas de conception dans docs/conception/schemas"
    [master b05b44f] Ajout des schémas de conception dans docs/conception/schemas
     6 files changed, 2294 insertions(+)
     create mode 100644 docs/conception/schemas/erd.mwb
     create mode 100644 docs/conception/schemas/erd.mwb.bak
     create mode 100644 docs/conception/schemas/erd.uxf
     create mode 100644 docs/conception/schemas/ucd.uxf
     create mode 100644 docs/conception/schemas/uml.pdf
     create mode 100644 docs/conception/schemas/uml.uxf
    $ git status
    Sur la branche master
    rien à valider, la copie de travail est propre
    $ git log
    commit b05b44f52e3d202286f633ff9e2c4edf3f9681f6 (HEAD -> master)
    Author: Caltuli <lucas.caltuli@gmail.com>
    Date:   Sat Jan 6 20:27:58 2024 +0100

        Ajout des schémas de conception dans docs/conception/schemas

    commit 531ef7a5b4376dc93c8295aff0cdbedf07aff510
    Author: Caltuli <lucas.caltuli@gmail.com>
    Date:   Sat Jan 6 20:06:56 2024 +0100

        Initial commit
    $ git checkout 531ef7a5b4376dc93c8295aff0cdbedf07aff510
    Note : basculement sur '531ef7a5b4376dc93c8295aff0cdbedf07aff510'.

    Vous êtes dans l'état « HEAD détachée ». Vous pouvez visiter, faire des modifications
    expérimentales et les valider. Il vous suffit de faire un autre basculement pour
    abandonner les commits que vous faites dans cet état sans impacter les autres branches

    Si vous voulez créer une nouvelle branche pour conserver les commits que vous créez,
    il vous suffit d'utiliser l'option -c de la commande switch comme ceci :

      git switch -c <nom-de-la-nouvelle-branche>

    Ou annuler cette opération avec :

      git switch -

    Désactivez ce conseil en renseignant la variable de configuration advice.detachedHead à false

    HEAD est maintenant sur 531ef7a Initial commit
    $ ls
    batch  business  consumer  model  pom.xml  webapp
    $ git log
    commit 531ef7a5b4376dc93c8295aff0cdbedf07aff510 (HEAD)
    Author: Caltuli <lucas.caltuli@gmail.com>
    Date:   Sat Jan 6 20:06:56 2024 +0100

        Initial commit
    $ git checkout master
    La position précédente de HEAD était sur 531ef7a Initial commit
    Basculement sur la branche 'master'
    $ ls
    batch  business  consumer  docs  model  pom.xml  webapp
    $
    ```

- **Gestion de branches et contrôle de version avec Git : Suppression de la classe Player dans le diagramme de classes :**
    ```plaintext
    git branch player-class-removal 531ef7a5b4376dc93c8295aff0cdbedf07aff510
    git checkout player-class-removal
    <modification of docs/conception/schemas/class_diagramm.xpdf>
    xpdf docs/conception/schemas/class_diagramm.xpdf
    <see modifications>
    git add docs/conception/schemas/class_diagramm.xpdf # include area in next commit
    git commit -m "Player class removel in docs/conception/class_diagramm"
    git checkout master
    xpdf docs/conception/schemas/class_diagramm.xpdf
    <see previous version of docs/conception/schemas/class_diagramm.xpdf>
    ```

- **Mise à jour de la version du plugin JUnit dans la branche master puis fusion des branches master et player-class-removal :**
    ```sh
    git checkout master
    <JUnit version update in pom.xml>
    git add pom.xml
    git commit -m "JUnit version update"
    git log --all --graph --decorate --pretty=oneline --abbrev-commit # to visualize
    # output :
    # * d9c1d70 (HEAD -> master) JUnit version update
    # * b05b44f Ajout des schémas de conception dans docs/conception/schemas
    # | * 870631b (player-class-removal) Player class removel in docs/conception/class_diagramm
    # |/  
    # * 531ef7a Initial commit
    # 
    # merge the two branches master and player-class-removal
    git checkout master
    git merge player-class-removal
    git log --all --graph --decorate --pretty=oneline --abbrev-commit  # to visualize
    # *   9153104 (HEAD, master) Merge branch 'player-class-removal'
    # |\  
    # | * 870631b (player-class-removal) Player class removel in docs/conception/class_diagramm
    # * | d9c1d70 JUnit version update
    # * | b05b44f Ajout des schémas de conception dans docs/conception/schemas
    # |/  
    # * 531ef7a Initial commit
    ```
