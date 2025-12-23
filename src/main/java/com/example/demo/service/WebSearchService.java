package com.example.demo.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.client.RestClient;
import java.util.Map;
import java.util.function.Function;

public class WebSearchService implements Function<WebSearchService.Request, WebSearchService.Response> {

    private static final String TAVILY_API_KEY = "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";
    private final RestClient restClient = RestClient.create();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public record Request(String query) {
    }

    public record Response(String content) {
    }

    @Override
    public Response apply(Request request) {
        System.out.println("🔍 AI đang tìm kiếm: " + request.query);

        try {
            String responseBody = restClient.post()
                    .uri("https://api.tavily.com/search")
                    .header("Content-Type", "application/json")
                    .body(Map.of(
                            "api_key", TAVILY_API_KEY,
                            "query", request.query,
                            "search_depth", "basic",
                            "include_answer", true,
                            "max_results", 3))
                    .retrieve()
                    .body(String.class);

            // Parse JSON để lấy answer + results
            JsonNode root = objectMapper.readTree(responseBody);
            StringBuilder result = new StringBuilder();

            // Lấy câu trả lời tóm tắt (nếu có)
            if (root.has("answer")) {
                result.append("📌 Tóm tắt: ").append(root.get("answer").asText()).append("\n\n");
            }

            // Lấy các kết quả tìm kiếm
            if (root.has("results")) {
                result.append("🔗 Nguồn tham khảo:\n");
                JsonNode results = root.get("results");
                for (int i = 0; i < Math.min(results.size(), 3); i++) {
                    JsonNode item = results.get(i);
                    result.append(i + 1).append(". ")
                            .append(item.get("title").asText())
                            .append("\n   ")
                            .append(item.get("url").asText())
                            .append("\n");
                }
            }

            return new Response(result.toString());

        } catch (Exception e) {
            System.err.println("❌ Lỗi tìm kiếm: " + e.getMessage());
            e.printStackTrace();
            return new Response("Không thể tìm kiếm do lỗi kết nối.");
        }
    }
}