package com.aeris.ai;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Service
public class GeminiService {

    private final WebClient webClient;
    private final String apiKey;

    public GeminiService(@Value("${gemini.api.key}") String apiKey) {
        this.apiKey = apiKey;
        this.webClient = WebClient.builder()
                .baseUrl("https://generativelanguage.googleapis.com/v1beta") // Use v1beta for newest features
                .defaultHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    public String askAI(String prompt) {
        // 1. Get the real current date/time to send to the AI
        String currentDateTime = ZonedDateTime.now()
                .format(DateTimeFormatter.ofPattern("EEEE, MMMM dd, yyyy HH:mm:ss VV"));

        System.out.println("Calling Gemini 3 Flash with Date Context: " + currentDateTime);

        // 2. Updated Request Body with "system_instruction"
        // This ensures the AI knows it is 2026, not 2024.
        Map<String, Object> requestBody = Map.of(
                "system_instruction", Map.of(
                        "parts", List.of(
                                Map.of("text", "You are AERIS AI. Today's date and time is: " + currentDateTime)
                        )
                ),
                "contents", List.of(
                        Map.of(
                                "role", "user",
                                "parts", List.of(
                                        Map.of("text", prompt)
                                )
                        )
                )
        );

        try {
            Map response = webClient.post()
                    .uri(uriBuilder -> uriBuilder
                            .path("/models/gemini-3-flash-preview:generateContent")
                            .queryParam("key", apiKey)
                            .build())
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            return extractText(response);

        } catch (WebClientResponseException.TooManyRequests e) {
            return "AERIS is thinking... Please wait a few seconds before asking again.";
        } catch (Exception e) {
            e.printStackTrace();
            return "AI service unavailable right now: " + e.getMessage();
        }
    }

    private String extractText(Map response) {
        try {
            List candidates = (List) response.get("candidates");
            Map first = (Map) candidates.get(0);
            Map content = (Map) first.get("content");
            List parts = (List) content.get("parts");
            Map textPart = (Map) parts.get(0);
            return textPart.get("text").toString();
        } catch (Exception e) {
            return "Error parsing AI response.";
        }
    }
}






















//package com.aeris.ai;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.MediaType;
//import org.springframework.stereotype.Service;
//import org.springframework.web.reactive.function.client.WebClient;
//import org.springframework.web.reactive.function.client.WebClientResponseException;
//
//import java.util.List;
//import java.util.Map;
//
//@Service
//public class GeminiService {
//
//    private final WebClient webClient;
//    private final String apiKey;
//
//    public GeminiService(@Value("${gemini.api.key}") String apiKey) {
//        this.apiKey = apiKey;
//        this.webClient = WebClient.builder()
//                .baseUrl("https://generativelanguage.googleapis.com/v1") // MUST be v1
//                .defaultHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
//                .build();
//    }
//
//    public String askAI(String prompt) {
//
//        System.out.println("Calling Gemini 2.5 Flash");
//        System.out.println("Prompt = " + prompt);
//
//        // Correct request format for Gemini 2.5
//        Map<String, Object> requestBody = Map.of(
//                "contents", List.of(
//                        Map.of(
//                                "role", "user",
//                                "parts", List.of(
//                                        Map.of("text", prompt)
//                                )
//                        )
//                )
//        );
//
//        try {
//            Map response = webClient.post()
//                    .uri(uriBuilder -> uriBuilder
//                            .path("/models/gemini-3-flash:generateContent") // ✅ MODEL
//                            .queryParam("key", apiKey)
//                            .build())
//                    .bodyValue(requestBody)
//                    .retrieve()
//                    .bodyToMono(Map.class)
//                    .block();
//
//            return extractText(response);
//
//        }
//        catch (WebClientResponseException.TooManyRequests e) {
//            return "AERIS is thinking... Please wait a few seconds before asking again.";
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//            return "AI service unavailable right now.";
//        }
//    }
//
//    private String extractText(Map response) {
//        List candidates = (List) response.get("candidates");
//        Map first = (Map) candidates.get(0);
//        Map content = (Map) first.get("content");
//        List parts = (List) content.get("parts");
//        Map textPart = (Map) parts.get(0);
//        return textPart.get("text").toString();
//    }
//}
