public class MAP
{
    //Knotenanzahl
    private int anzahlKnoten;
    
    // Feld der Knoten des Graphen
    private KNOTEN[] knoten;   

    // 2-dim Feld der Adjazenzmatrix
    private int[][] matrix;
    public MAP()
    {
        int maximaleKnoten = 3;
        anzahlKnoten = 0;
        knoten = new KNOTEN[maximaleKnoten];
        matrix = new int[maximaleKnoten][maximaleKnoten];
    }
    
    
}