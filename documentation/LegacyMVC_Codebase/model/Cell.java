
package model;

// importations pour gérer les évènements ;
// pour déclencher un évènement de classe PropertyChangeEvent
// à chaque changement de couleur (CellState) d'une cellule
// (Cell) de la grille de jeu (Grid)
import java.io.Serializable;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;

// instancie des objets qui représentent les cellules des grilles
// de jeu (grilles de jeu des parties en cours et grilles de jeu
// des arbres de morceaux de parties possibles des joueurs IA qui leur
// permettent d'évaluer les positions courantes via un algorithme
// min-max)
public final class Cell implements Serializable {



  // ATTRIBUTS



  // pour déclencher un évènement de classe PropertyChangeEvent
  // à chaque changement de valeur de l'attribut CellState (représentant
  // la couleur de la cellule représentée par l'instance) afin
  // d'informer la vue (view) pour qu'il reflète le modèle (model)
  // à l'utilisateur via l'interface graphique
  PropertyChangeSupport observable;

  // représente les coordonnées de la cellule représentée par l'instance
  // sur la grille de jeu
  private Coordinates coordinates;

  // représente la couleur de la cellule représentée par l'instance
  private CellState cellState;



  // CONSTRUCTEUR



  public Cell(Coordinates coordinates, CellState... cellState) {

    this.observable = new PropertyChangeSupport(this);
    this.coordinates = coordinates;

    if (cellState.length == 0)
      this.cellState = CellState.NULL;
    else
      this.cellState = cellState[0];

  }



  // MÉTHODES POUR SE RENDRE OBSERVABLE PAR LES ÉCOUTEURS



  public synchronized void addPropertyChangeListener(
      PropertyChangeListener listener) {
    this.observable.addPropertyChangeListener(listener);
  }

  public synchronized void removePropertyChangeListener(
      PropertyChangeListener listener) {
    this.observable.removePropertyChangeListener(listener);
  }



  // ASSESSEURS



  public PropertyChangeSupport getObservable() {return this.observable;}
  public Coordinates getCoordinates() {return this.coordinates;}
  public CellState getCellState() {return this.cellState;}



  // MUTATEURS



  public void setObservable(PropertyChangeSupport observable) {
    this.observable = observable;
  }
  public void setCoordinates(Coordinates coordinates) {
    this.coordinates = coordinates;
  }
  public synchronized void setCellState(CellState cellState) {
    CellState oldCellState = this.cellState;
    this.cellState = cellState;
    if (this.observable != null)
      this.observable.firePropertyChange(
        "cellModification",
        oldCellState,
        cellState
    );
  }



  // MÉTHODES DE DÉBOGAGE



  @Override
  public String toString() {
    return
      "("
      + this.coordinates
      + ","
      + this.cellState
      + " at "
      + super.toString()
      + ")";
  }

  public void unobservableSetCellState(CellState color) {
    this.cellState = color;
  }


}
