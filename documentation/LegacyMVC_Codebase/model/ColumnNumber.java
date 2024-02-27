
package model;

import java.util.HashSet;

// réprésente le numéro d'une colonne dans une grille de jeu
public enum ColumnNumber {



  ONE("1"),
  TWO("2"),
  THREE("3"),
  FOUR("4"),
  FIVE("5"),
  SIX("6"),
  SEVEN("7");



  // ATTRIBUTS



  // ensemble des instances de cette énumération
  public final static HashSet<ColumnNumber> SET_OF_COLUMN_NUMBERS
    = ColumnNumber.initialiseSetOfColumnNumbers();

  // chaine de caractères correspondant à l'instance
  private String token;



  // CONSTRUCTEUR PRIVÉ



  private ColumnNumber(String token) {this.token = token;}



  // MÉTHODES D'INITIALISATION



  // initialise l'attribut SET_OF_COLUMN_NUMBERS
  private static HashSet<ColumnNumber> initialiseSetOfColumnNumbers() {

    HashSet<ColumnNumber> SET_OF_COLUMN_NUMBERS;

    SET_OF_COLUMN_NUMBERS = new HashSet<ColumnNumber>();

    for (int i = 0 ; i < ColumnNumber.values().length ; i++)
      SET_OF_COLUMN_NUMBERS.add(
        ColumnNumber.values()[i]
      );

    return SET_OF_COLUMN_NUMBERS;

  }



  // MÉTHODES DE CONVERSION



  // renvoie l'entier correspondant au numéro de la colonne représentée
  // par l'instance
  public int toInt() {

    return this.ordinal() + 1;

  }

  // renvoie l'instance qui représente le numéro de la colonne correspondant
  // à l'entier passé en paramètre
  public static ColumnNumber intToColumnNumber(int i) {

    return ColumnNumber.values()[i-1];

  }



  // ASSESSEUR



  public String getToken() {return this.token;}



  // MÉTHODES DE DÉBOGAGE



  @Override
  public String toString() {
    return this.getToken();
  }



}
