package uk.ac.ed.inf.aqmaps;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;


class WebServerConnection {
	private static final HttpClient CLIENT = HttpClient.newHttpClient();
	private static final String PROTOCOL = "http";
	private static final String SERVER_DOMAIN = "localhost"; /// INCLUDE TOP LEVEL DOMAIN IF AVAILABLE (E.G 'example.com')
	private static int port;
	
	public static void setPort(int port) {
		WebServerConnection.port = port;
	}
	
	// communication with the server here
	private static HttpResponse<String> makeRequest(String subDirectory) throws IOException, InterruptedException {
		try {
			var urlString = String.format("%s://%s:%s/%s", PROTOCOL, SERVER_DOMAIN, port, subDirectory);
			var request = HttpRequest.newBuilder()
					.uri(URI.create(urlString))
					.build();
	
			var response = CLIENT.send(request, BodyHandlers.ofString());
			if (response.statusCode() == 200) {
				return response;
			}
			return null;
		}
		catch (IOException | InterruptedException e) {
			System.out.println("Fatal error: Unable to connect to " + SERVER_DOMAIN + " at port " + port + ".");
			System.exit(1);
			return null;
		}
	}
	
	public static String getBuildings() {
		try {
			return makeRequest("buildings/no-fly-zones.geojson").body();
		} catch (IOException | InterruptedException e) {
			System.out.println("Fatal error: Unable to connect to " + SERVER_DOMAIN + " at port " + port + ".");
			System.exit(1);
			return null;
		}
	}

	public static String getAirQualityData(int year, int month, int day) {
		try {
			var dayStr = day < 10 ? "0" + day : "" + day;
			var monthStr = month < 10 ? "0" + month : "" + month;
			var date = String.format("%s/%s/%s", year, monthStr, dayStr);
			return makeRequest("maps/" + date + "/air-quality-data.json").body();
		} catch (IOException | InterruptedException e) {
			System.out.println("could not get buildings file");
			System.exit(1);
			return null;
		}
	}

	public static String getWords(String[] words) {
		try {
			String joinedWords = String.join("/", words);
			return makeRequest("words/" + joinedWords + "/details.json").body();
		} catch (IOException | InterruptedException e) {
			System.out.println("Fatal error: Unable to connect to " + SERVER_DOMAIN + " at port " + port + ".");
			System.exit(1);
			return null;
		}
	}
	
	
}
