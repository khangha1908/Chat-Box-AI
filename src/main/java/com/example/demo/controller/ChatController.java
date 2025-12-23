package com.example.demo.controller;

import com.example.demo.model.ChatMessage;
import com.example.demo.model.Conversation;
import com.example.demo.model.User;
import com.example.demo.repository.ChatMessageRepository;
import com.example.demo.repository.ConversationRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ChatController {

    private final ChatClient chatClient;
    private final ChatMessageRepository chatRepo;
    private final UserRepository userRepo;
    private final ConversationRepository convRepo;

    public ChatController(ChatClient.Builder builder, ChatMessageRepository chatRepo, UserRepository userRepo,
            ConversationRepository convRepo) {
        String today = LocalDate.now().toString();

        this.chatClient = builder
                .defaultSystem("Ngày hiện tại: " + today + "\n\n" +
                        "Bạn là trợ lý AI trả lời bằng Tiếng Việt, định dạng Markdown.\n\n" +
                        "**SỬ DỤNG 'internetSearch' KHI:**\n" +
                        "- Người dùng hỏi về tin tức, thời sự, thời tiết, giá cả\n" +
                        "- Thông tin về người/công ty/sản phẩm cụ thể bạn không biết (như 3I/ATLAS)\n" +
                        "- Sự kiện có thể đã thay đổi sau tháng 1/2025\n\n" +
                        "Ví dụ CẦN search: \"Giá Bitcoin?\", \"Thời tiết Hà Nội?\", \"3I/ATLAS là gì?\"")
                .build();

        this.chatRepo = chatRepo;
        this.userRepo = userRepo;
        this.convRepo = convRepo;
    }

    @GetMapping("/chat")
    public String showChatPage(@RequestParam(required = false) Long id, Model model) {
        User currentUser = getCurrentUser();
        if (currentUser == null)
            return "redirect:/login";

        model.addAttribute("username", currentUser.getUsername());

        List<Conversation> conversations = convRepo.findByUserIdOrderByCreatedAtDesc(currentUser.getId());
        model.addAttribute("conversations", conversations != null ? conversations : new ArrayList<>());

        List<ChatMessage> history = new ArrayList<>();
        if (id != null) {
            Conversation conv = convRepo.findById(id).orElse(null);
            if (conv != null && conv.getUserId().equals(currentUser.getId())) {
                history = chatRepo.findByConversationIdOrderByTimestampAsc(id);
                model.addAttribute("currentConvId", id);
                model.addAttribute("currentTitle", conv.getTitle());
            } else {
                return "redirect:/chat";
            }
        } else {
            model.addAttribute("currentConvId", "");
            model.addAttribute("currentTitle", "New Chat");
        }

        model.addAttribute("history", history);
        return "chat";
    }

    @PostMapping("/chat")
    public String sendMessage(@RequestParam String message,
            @RequestParam(required = false) Long conversationId) {
        User currentUser = getCurrentUser();
        if (currentUser == null)
            return "redirect:/login";

        if (message == null || message.trim().isEmpty()) {
            return "redirect:/chat";
        }

        Long finalConvId = conversationId;
        if (finalConvId == null) {
            Conversation newConv = new Conversation(currentUser.getId(), "New Chat");
            String title = message.length() > 30 ? message.substring(0, 30) + "..." : message;
            newConv.setTitle(title);
            convRepo.save(newConv);
            finalConvId = newConv.getId();
        }

        chatRepo.save(new ChatMessage(currentUser.getId(), finalConvId, message, "user"));

        List<ChatMessage> history = chatRepo.findByConversationIdOrderByTimestampAsc(finalConvId);
        List<Message> promptMessages = new ArrayList<>();
        for (ChatMessage chatMsg : history) {
            if ("user".equals(chatMsg.getRole())) {
                promptMessages.add(new UserMessage(chatMsg.getMessageContent()));
            } else {
                promptMessages.add(new AssistantMessage(chatMsg.getMessageContent()));
            }
        }

        if (promptMessages.isEmpty()) {
            promptMessages.add(new UserMessage(message));
        }

        OpenAiChatOptions options = OpenAiChatOptions.builder()
                .withFunction("internetSearch")
                .build();

        String response = "";
        try {
            response = chatClient.prompt(new Prompt(promptMessages, options))
                    .call()
                    .content();
        } catch (Exception e) {
            e.printStackTrace();
            response = "⚠️ Xin lỗi, tôi gặp lỗi khi kết nối công cụ tìm kiếm (" + e.getMessage()
                    + "). Vui lòng thử lại câu hỏi khác.";
        }

        try {
            Thread.sleep(1000);
        } catch (Exception e) {
        }

        chatRepo.save(new ChatMessage(currentUser.getId(), finalConvId, response, "ai"));

        return "redirect:/chat?id=" + finalConvId;
    }

    @PostMapping("/delete-chat")
    public String deleteChat(@RequestParam Long id) {
        User currentUser = getCurrentUser();
        Conversation conv = convRepo.findById(id).orElse(null);
        if (conv != null && conv.getUserId().equals(currentUser.getId())) {
            List<ChatMessage> msgs = chatRepo.findByConversationIdOrderByTimestampAsc(id);
            chatRepo.deleteAll(msgs);
            convRepo.delete(conv);
        }
        return "redirect:/chat";
    }

    @GetMapping("/new-chat")
    public String newChat() {
        return "redirect:/chat";
    }

    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
            return null;
        }
        return userRepo.findByUsername(auth.getName());
    }
}
