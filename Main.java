import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import org.json.simple.JSONObject;

public class Main extends JFrame
{

    private JFrame frame = new JFrame(); 
    private JPanel panel = new JPanel();
    private JTextField SearchField;
    private JButton SearchButton;

    private JLabel weatherConditionImage = new JLabel();


    private JSONObject weatherData ; 



public Main()
{
    setupGUI();
}


// constructor



private void setupGUI()
{

        frame.setSize(450,700);  
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Weather Application");
        ImageIcon image = new ImageIcon("app icon.png");
        frame.setIconImage(image.getImage());
        frame.getContentPane().setBackground(Color.WHITE);

        panel.setLayout(null);


       addGuiCompenents();


        frame.add(panel);
        frame.setVisible(true);


}
private void addGuiCompenents()
{

    // search field 

    // search Label and Field
    JLabel userlabel = new JLabel("Search a City ");
    userlabel.setBounds(10,20,80,25);
    panel.add(userlabel);
    SearchField = new JTextField(20);
    SearchField.setBounds(130,20,165,25);
    panel.add(SearchField); 



    // weather image 

    JLabel weatherConditionImage = new JLabel(loadImage("C:\\Users\\yahya\\OneDrive\\Desktop\\javaWeatherProject\\images\\clear.png"));

    weatherConditionImage.setBounds(0,200,450,217);

    panel.add(weatherConditionImage);

    // temperature text

    JLabel temperatureText = new JLabel("10 C");

    temperatureText.setBounds(0,500,450,55);
   
    temperatureText.setFont(new Font("Dialog" , Font.BOLD , 48));

    temperatureText.setHorizontalAlignment(SwingConstants.CENTER);

    panel.add(temperatureText);

    // weather condition description 

    JLabel weatherConditionDesc = new JLabel("Cloudy");

    weatherConditionDesc.setBounds(0,600,450,36);

    weatherConditionDesc.setFont(new Font("Dialog" , Font.PLAIN , 32));

    weatherConditionDesc.setHorizontalAlignment(SwingConstants.CENTER);

    panel.add(weatherConditionDesc);


   // HUMIDITY IMAGE 

   JLabel HumidityImage = new JLabel(loadImage("images/humidity.png"));

   HumidityImage.setBounds(15,550,60,70);
   
   panel.add(HumidityImage);


   // HUMIDITY TEXT
   
   JLabel HumidityText = new JLabel("<html><b>Humidity</b> 100%</html>")  ; 

   HumidityText.setBounds(90,550,85,55);

   HumidityText.setFont(new Font("Dialog" , Font.PLAIN , 16));

   panel.add(HumidityText);


   // windspeed image 

   JLabel WindSpeedImage = new JLabel(loadImage("images/windspeed.png"));

   WindSpeedImage.setBounds(350,550,74,70);

   panel.add(WindSpeedImage);


   // windspeedtext 

   JLabel windspeedtext = new JLabel("<html><b>Windspeed</b> 15Km/h</html>")  ; 

   windspeedtext.setBounds(350,610,85,55);

   windspeedtext.setFont(new Font("Dialog" , Font.PLAIN , 16));

   panel.add(windspeedtext);


    // search button 

    SearchButton = new JButton("Search");
    //change the cursor to a hand cursor when hovering over this button
    SearchButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    SearchButton.setBounds(130,55,165,25);
    SearchButton.addActionListener(new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {

            // get location from user 

            String userInput = SearchField.getText();

            // validate input string 

            if (userInput.isEmpty())
              {
                return ; 
              }

              // retrieve weather data 

              weatherData = BackEnd.getWeatherData(userInput);

              // update GUI 

              // update weather image 

              String weatherCondition = (String) weatherData.get("weather_condition");

              switch(weatherCondition)

              {
                case "Clear":
                weatherConditionImage.setIcon(loadImage("images\\clear.png"));
                break;

                case "Cloudy":
                weatherConditionImage.setIcon(loadImage("images\\cloudy.png"));
                break;

                case "Rain":
                weatherConditionImage.setIcon(loadImage("images\\rain.png"));
                break;

                case "Snow":
                weatherConditionImage.setIcon(loadImage("images\\snow.png"));
                break;

              }

                // update template text 


                double temperature = (double) weatherData.get("temperature");
                temperatureText.setText(temperature + " C" );

              // update weather condition text 

               weatherConditionDesc.setText(weatherCondition);

               // update humidity text 

               long humidity = (long) weatherData.get("humidity") ;
               HumidityText.setText("<html><b>Humidity<\b> " + humidity + "%<html>");

               // update windspeed text 

               double windspeed = (double) weatherData.get("windspeed") ;
               windspeedtext.setText("<html><b>Humidity<\b> " + windspeed + "Km/h<html>");

           

                   

            }
            

        });
        
    


    panel.add(SearchButton);



 
}

private ImageIcon  loadImage(String resourcePath)
{
    try 
    {
        BufferedImage image = ImageIO.read(new File(resourcePath));
        return new ImageIcon(image);
    }catch (IOException e )
    {
        e.printStackTrace();
    }
    
    System.out.println("could not find resourse");
    return null;

}



}