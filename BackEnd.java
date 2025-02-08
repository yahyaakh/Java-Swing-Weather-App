
// retreive weather data from API fetch the latest weather info

// this data will be displaed 

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class BackEnd {
   
    // fetch weather data for a given city 

    public static JSONObject getWeatherData(String locationName)
    {
        JSONArray locationData = getLocationData(locationName);

        JSONObject location = (JSONObject) locationData.get(0);

        double latitude = (double) location.get("latitude");
        double longitude = (double) location.get("longitude");

        String urlString = "https://api.open-meteo.com/v1/forecast?" +
        "latitude=" +latitude +"&longitude=" + longitude+
        "&hourly=temperature_2m,relative_humidity_2m,weather_code,wind_speed_10m";

        try {
            HttpURLConnection conn = fetchApiResponse(urlString) ; 
          // check for response status if it is 200 then all good 

          if (conn.getResponseCode() != 200 )
            {
                System.out.println("there is an error , could not connect to api");
                return null ;
            }

            StringBuilder resultJson = new StringBuilder();
            Scanner scanner = new Scanner(conn.getInputStream());
            while (scanner.hasNext() )
            {
                // read and store into the string builder
                resultJson.append(scanner.nextLine());
            }

            scanner.close();

            // close url connection 

            conn.disconnect();

            JSONParser parser = new JSONParser();

            JSONObject resulJsonObject = (JSONObject) parser.parse(String.valueOf(resultJson));
            // retreve hourly data :

            JSONObject hourly = (JSONObject) resulJsonObject.get("hourly");

            JSONArray time = (JSONArray) hourly.get("time");

            int index = findIndexOfCurrentTime(time);

            JSONArray temperatureData = (JSONArray) hourly.get("temperature_2m");

            double temperature = (double) temperatureData.get(index);

            JSONArray weathercode = (JSONArray) hourly.get("weather_code");

            String weatherCondition = convertWeatherCode( (long) weathercode.get(index));
                 
           // get Humidity data 

           JSONArray Humidity = (JSONArray) hourly.get("relative_humidity_2m");

            long humidity = (long ) Humidity.get(index);

            // get wind speed 


            JSONArray WindSpeed  = (JSONArray) hourly.get("wind_speed_10m");
            
            double windspeed = (double) WindSpeed.get(index);

            // build the weather json data object that gonna be used in the app frontend

            JSONObject weatherData = new JSONObject();

            weatherData.put("temperature" , temperature);

            weatherData.put("weather_condition" , weatherCondition);

            weatherData.put("humidity" , humidity);

            weatherData.put("windspeed" , windspeed);


            return weatherData ;


        }
        catch (Exception e)
        {
            e.printStackTrace();

        }


       return null ;
    }


    public static JSONArray getLocationData(String locationName)
    {
        locationName = locationName.replaceAll(" " , "+");

        // build API URL with location parameter 

        String urlString = "https://geocoding-api.open-meteo.com/v1/search?name=" +
        locationName + "&count=10&language=en&format=json" ;

        try 
        {
            HttpURLConnection conn = fetchApiResponse(urlString) ; 

            // check response status 
            // 200 means goood 
            // 400 means something is wrong 

            if (conn.getResponseCode() != 200 )
            {
                System.out.println("there is an error , could not connect to api");
                return null ;
            }
            else 
            {
             StringBuilder resultJson = new StringBuilder() ;
             Scanner scanner = new Scanner (conn.getInputStream()) ;

             // read and store the resulting Json data into our string builder 
             while(scanner.hasNext())
             {
                resultJson.append(scanner.nextLine());
             }

             scanner.close();

             // close url connection 

             conn.disconnect();

             // parse the JSON string into a JSON object 

             JSONParser parser = new JSONParser();
             JSONObject resultJsonObj = (JSONObject)parser.parse(String.valueOf(resultJson));

             // get the list of location data the api generated from the location name

             JSONArray locationData = (JSONArray) resultJsonObj.get("results");

             return locationData;

            }

        } catch (Exception e)
        {
            e.printStackTrace();
        }
                return null;

        
        

    }

    private static HttpURLConnection  fetchApiResponse(String urlString)
    {

     try 
     {
        URL url = new URL (urlString);

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("GET");

        // CONNECT TO OUR API
        conn.connect();

        return conn ; 

     }catch (IOException e)
     {
        e.printStackTrace();
     }

     return null; 

    }


    private static int findIndexOfCurrentTime(JSONArray timeList)
    {
        String currentTime = getCurrentTime();

        // iterate Through the time list and see which one matches our current time.

        for (int i=0 ; i < timeList.size() ; i++)
        {
            String time = (String) timeList.get(i);
            if (time.equalsIgnoreCase(currentTime))
            {
                //return the index 
                return i;
            }
        }

        return 0 ; 
    }

    public static String getCurrentTime()
    {
      LocalDateTime  currentDateTime = LocalDateTime.now();

      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-mm-dd'T'HH':00'");

      String formatteDateTime = currentDateTime.format(formatter);

      return formatteDateTime;
    }

    // convert the weather code to something that can be read easily 

    // 0 clear sky 
    // 1 2 3 mainly clear , partly cloudy and overcast 

    // 45 48 fog and depositing rime fog 

    // 77 rain

    // and so on 

    private static String convertWeatherCode(long weathercode )
    {
         String weatherCondition ="" ;

         if (weathercode == 0L)
         {
            weatherCondition = "Clear" ; 
         }
         else if (weathercode >0l && weathercode <= 3L)
         {
            weatherCondition = "Cloudy" ;
         }

         else if( (weathercode >= 51L && weathercode <= 67L ) ||   (weathercode >= 80L && weathercode <= 99L ) )
         {
            weatherCondition = "Rain" ;

         }
           
         else if (weathercode >= 71L && weathercode <= 77L )
         {
            weatherCondition = "Snow" ;
         }


       return weatherCondition ; 


    }
}
