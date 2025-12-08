// package com.dtcc.simulation.client;

// import java.io.BufferedReader;
// import java.io.InputStreamReader;
// import java.net.HttpURLConnection;
// import java.net.URL;

// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.stereotype.Service;

// import com.fasterxml.jackson.databind.JsonNode;
// import com.fasterxml.jackson.databind.ObjectMapper;

// @Service
// public class StreamingApiClient {

//     @Value("${fmp.api.key}")
//     private String apiKey;

//     private final ObjectMapper objectMapper = new ObjectMapper();

//     public void start() {
//         new Thread(() -> {
//             while (true) {
//                 try {
//                     String apiUrl =
//                             "https://financialmodelingprep.com/stable/aftermarket-trade"
//                                     + "?apikey=" + apiKey;

//                     URL url = new URL(apiUrl);
//                     HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//                     conn.setRequestMethod("GET");
//                     conn.setConnectTimeout(5000);
//                     conn.setReadTimeout(5000);

//                     int status = conn.getResponseCode();

//                     // Handle 429 rate limit correctly
//                     if (status == 429) {
//                         System.out.println("Rate limit hit. Waiting 15 seconds...");
//                         Thread.sleep(15000);
//                         continue; // retry
//                     }

//                     // If other errors
//                     if (status != 200) {
//                         System.out.println("Error response: " + status);
//                         Thread.sleep(5000);
//                         continue;
//                     }

//                     BufferedReader reader = new BufferedReader(
//                             new InputStreamReader(conn.getInputStream(), "UTF-8")
//                     );

//                     // Read entire JSON array
//                     StringBuilder json = new StringBuilder();
//                     String line;
//                     while ((line = reader.readLine()) != null) {
//                         json.append(line);
//                     }

//                     reader.close();
//                     conn.disconnect();

//                     // Parse JSON array
//                     JsonNode array = objectMapper.readTree(json.toString());

//                     if (array.isArray()) {
//                         for (JsonNode node : array) {
//                             System.out.println(node); // each stock trade
//                         }
//                     }

//                     // Poll only every 10 seconds (safe for free plan)
//                     Thread.sleep(10000);

//                 } catch (Exception e) {
//                     e.printStackTrace();
//                     try {
//                         Thread.sleep(5000);
//                     } catch (Exception ignored) {}
//                 }
//             }
//         }).start();
//     }
// }
