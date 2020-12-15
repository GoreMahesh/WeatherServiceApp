package com.systools;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Weather {
	public static void main(String[] args) throws IOException {
		String input;
		try {
			System.out.println("Tomorrow Predicted Temperature");
			System.out.println("Enter the 5 digit Zip code of the city in USA");
			
			BufferedReader bufferReader = new BufferedReader(new InputStreamReader(System.in));
			while ((input = bufferReader.readLine()) != null && !input.equals("exit") && !input.equals("quit")) {
				try {
					int zipcode = 32256;
					zipcode = Integer.parseInt(input);
					getDetails(zipcode);
				} catch (NumberFormatException e) {
					System.out.println("Please enter a valid zip code\n");
				}

				System.out.println(
						"Want to check temperature at another location,\nthen please Enter the 5 digit Zip code of the city in USA or enter exit to quit");

			}
			System.out.println(".....Program Exited.....");

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void getDetails(int zipcode) {
		URLConnection con=null;
		InputStream is=null;
		BufferedReader bufferedReader=null;
		String line = null;
		JSONObject obj = null;
		JSONArray arr = null;
		int index = 0;
		try {
			// Make a URL to the web page
			// You can generate the APP_ID and APP_CODE after signing up on
			// "https://developer.here.com"
			// metric = false, Temperature would be in fahrenheit (For Celcius type metric =
			// true)
			// reports back hourly forecast for the next 7 days
			URL url = new URL(
					"https://weather.api.here.com/weather/1.0/report.json?app_id={Your_APP_ID}&app_code={Your_APP_CODE}&product=forecast_hourly&metric=false&zipcode="
							+ zipcode);

			url.openStream().close();
			// Get the input stream through URL Connection
			 con= url.openConnection();

			 is = con.getInputStream();

			 bufferedReader = new BufferedReader(new InputStreamReader(is));

			line = bufferedReader.readLine();

			obj = new JSONObject(line);

			arr = obj.getJSONObject("hourlyForecasts").getJSONObject("forecastLocation").getJSONArray("forecast");
			String cityName = obj.getJSONObject("hourlyForecasts").getJSONObject("forecastLocation").getString("city");

			ArrayList<Double> temp = new ArrayList<Double>();
			ArrayList<String> dateTime = new ArrayList<String>();
			
			String today = arr.getJSONObject(index).getString("weekday");
			for (index = 1; today.equals(arr.getJSONObject(index).getString("weekday")); index++) {
			}
			for (int indexj = 0; indexj < 24; index++, indexj++) {
				temp.add(Double.parseDouble((arr.getJSONObject(index).getString("temperature"))));
				dateTime.add(arr.getJSONObject(index).getString("localTime"));
			}
			
			int min_index = temp.indexOf(Collections.min(temp));
			String time = dateTime.get(min_index).substring(0, 2);
			String date = dateTime.get(min_index).substring(2, dateTime.get(min_index).length());
			date = date.substring(0, 2) + "-" + date.substring(2, 4) + "-" + date.substring(4, 8);
			
			String meridiem = "am";
			// Display time in meridiem
			if (Integer.parseInt(time) > 11) {
				if (time != "12")
					time = Integer.toString((Integer.parseInt(time) - 12));
				meridiem = "pm";
			} else {
				if (time == "00")
					time = "12";
			}
			// Finds the coolest hour of tomorrow for the city of user feeded zipcode
			System.out.println(
					"coolest hour in " + cityName + " tomorrow on " + date + " would be at " + time + " " + meridiem);
			System.out.println("and the temperature would be " + temp.get(min_index) + " F\n");

		} catch (IOException e) {
			System.out.println("City not found, please enter a valid Zip Code\n");
			return;
		} catch (JSONException e) {
			System.out.println(" Invalid API format ");
		}
	}
}
