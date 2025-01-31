package data8319.service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.json.JSONArray;
import org.json.JSONObject;
public class ChatGptService {

	public String generateSummary(String prompt) throws Exception  {
		
		    String apiKey = "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";  
		    // Replace with  actual OpenAI API key
		    String apiUrl = "https://api.openai.com/v1/chat/completions";

		    // JSON payload for the OpenAI API request
		    JSONObject json = new JSONObject();
		    json.put("model", "gpt-4o");  // Specify the model
		    JSONArray messages = new JSONArray();
		    JSONObject message = new JSONObject();
		    message.put("role", "user");
		    message.put("content", prompt);
		    messages.put(message);
		    json.put("messages", messages);
		    json.put("max_tokens", 16384);  // Adjust tokens based on summary length needed

		    HttpRequest request = HttpRequest.newBuilder()
		        .uri(new URI(apiUrl))
		        .header("Content-Type", "application/json")
		        .header("Authorization", "Bearer " + apiKey)
		        .POST(HttpRequest.BodyPublishers.ofString(json.toString()))
		        .build();

		    HttpClient client = HttpClient.newHttpClient();
		    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

		    // Parse the response as JSON
		    JSONObject responseObject = new JSONObject(response.body());

		 // Check for errors in response
		    if (responseObject.has("error")) {
		        System.err.println("API Error Response: " + responseObject.getJSONObject("error").toString());
		        throw new Exception("OpenAI API Error: " + responseObject.getJSONObject("error").getString("message"));
		    }

		    // Retrieve the generated content
		    if (responseObject.has("choices")) {
		        return responseObject.getJSONArray("choices")
		                             .getJSONObject(0)
		                             .getJSONObject("message")
		                             .getString("content")
		                             .trim();
		    } else {
		        System.err.println("Unexpected API response: " + response.body());
		        throw new Exception("Unexpected response from API: 'choices' not found.");
		    }
		}
}	
	