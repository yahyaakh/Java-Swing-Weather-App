import javax.swing.SwingUtilities;

public class AppLauncher {


    public static void main (String [] args) 
    {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run ()
            {
                new Main() ;

               //System.out.println(BackEnd.getLocationData ("Tokyo"));

            //System.out.println(BackEnd.getCurrentTime());



            }
        });
    }
    
}
