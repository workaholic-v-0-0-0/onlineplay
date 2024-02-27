

package model;

import java.util.HashSet;

// réprésente le numéro d'une ligne dans une grille de jeu
public enum LineNumber {



  OUT("0"), // ligne au-dessus de la grille ; indique une colonne pleine
  ONE("1"),
  TWO("2"),
  THREE("3"),
  FOUR("4"),
  FIVE("5"),
  SIX("6");



  // ATTRIBUTS



  // chaine de caractères correspondant à l'instance
  private String token;



  // CONSTRUCTEUR PRIVÉ



  private LineNumber(String token) {this.token = token;}



  // MÉTHODES ARITHMÉTIQUES



  // renvoie l'instance correspondant au numéro de la ligne immédiatement
  // supérieure à celle dont le numéro est représenté par l'instance
  // sur la grille de jeu ;
  public LineNumber decrement() {

    return LineNumber.intToLineNumber(this.toInt() - 1);

  }



  // MÉTHODES DE CONVERSION



  // renvoie l'entier correspondant au numéro de la ligne représentée
  // par l'instance
  public int toInt() {

    return this.ordinal();

  }

  // renvoie l'instance qui représente le numéro de la ligne correspondant
  // à l'entier passé en paramètre
  public static LineNumber intToLineNumber(int i) {

    return LineNumber.values()[i];

  }



  // ASSESSEURS



  public String getToken() {return this.token;}



  // MÉTHODES DE DÉBOGAGE



  @Override
  public String toString() {
    return this.getToken();
  }



}
