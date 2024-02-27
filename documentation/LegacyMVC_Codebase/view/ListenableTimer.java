
package view;

// importation d'une classe instanciant des déclencheurs
// qui est la classe mère de la classe ListenableTimer
// implémentée dans ce fichier
import javax.swing.Timer;

// importations pour gérer les évènements
import java.io.Serializable;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;
import java.awt.event.ActionListener;

// instancie des objets de surclasse TIMER qui émettent un évènement de
// classe PropertyChangeEvent lors de l'appel à la méthode stop() ;
// permet d'informer le contrôleur de la fin de l'animation représentant
// la chute d'un jeton pour qu'il gère le tour du prochain joueur si
// celui-ci est humain (et non IA)
public class ListenableTimer extends Timer implements Serializable {



  // ATTRIBUT



  // pour gérer l'écoutabilité de l'instance
  private PropertyChangeSupport observable;



  // CONSTRUCTEUR



  public ListenableTimer(int delay, ActionListener actionListener) {

    super(delay, actionListener);
    this.observable = new PropertyChangeSupport(this);

  }



  // RÉIMPLÉMENTATION DE LA MÉTHODE stop POUR QU'ELLE ÉMETTE UN
  // ÉVÈNEMENT DE CLASSE PropertyChangeEvent



  @Override
  public synchronized void stop() {
    super.stop();
    this.observable.firePropertyChange(
      "stopRunning",
      true,
      false
    );
  }



  // IMPLÉMETATION DE L'INTERFACE PropertyChangeListener
  // (méthodes pour rendre les instances écoutables par le contrôleur)



  // ajoute un écouteur de l'attribut observable
  public synchronized void addPropertyChangeListener(
      PropertyChangeListener listener) {
    this.observable.addPropertyChangeListener(listener);
  }

  // supprime un écouteur de l'attribut observable
  public synchronized void removePropertyChangeListener(
      PropertyChangeListener listener) {
    this.observable.removePropertyChangeListener(listener);
  }



  // ASSESSEUR



  public PropertyChangeSupport getObservable() {return this.observable;}



}
