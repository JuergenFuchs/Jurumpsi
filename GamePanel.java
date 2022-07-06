import java.awt.Dimension;

/**
 * Beschreiben Sie hier die Klasse GamePanel.
 * 
 * @author (Ihr Name) 
 * @version (eine Versionsnummer oder ein Datum)
 */
public class GamePanel
{
    private static final long serialVerrsionUID = 1L;
    public static final int WIDTH = 900;
    public static final int HEIGHT = 550;
    
    private Thread thread;
    private boolean isRunning = false;
    
    private int FPS = 60;
    private long targetTime = 1000 /FPS;
    public GamePanel(){
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        
        start();
    }
    private void start() {
        isRunning = true;
        thread = new Thread(this);
        thread.start();
}
public void run() {
    long start, elapsed, wait;
    while(isRunning){
        
    }
}
public void tick() {
    
}
public void paintComponent(){
    
}
}