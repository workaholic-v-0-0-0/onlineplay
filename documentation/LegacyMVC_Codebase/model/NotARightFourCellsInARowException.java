
package model;

// exception levée lorsqu'un ensemble de cellules devant être un ensemble
// de quatre cellules alignées n'a pas quatre éléments
public class NotARightFourCellsInARowException extends Exception {

  public NotARightFourCellsInARowException() {
    super("Un groupe de cellules gagnantes n'est pas de taille 4.");
  }

  public NotARightFourCellsInARowException(String comment) {
    super(comment);
  }

}
