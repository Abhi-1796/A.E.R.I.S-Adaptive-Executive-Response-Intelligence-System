package com.aeris.service;

import com.aeris.ai.GeminiService;
import com.aeris.model.CommandHistory;
import com.aeris.repository.CommandHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.List;

@Service
public class AerisService {

    @Autowired
    private CommandHistoryRepository repository;

    @Autowired
    private GeminiService geminiService; // ✅ ADD THIS

    public String processCommand(String command) {

        // 1️⃣ Validate input
        if (command == null || command.trim().isEmpty()) {
            return "Please enter a command.";
        }

        command = command.trim();

        // 2️⃣ Call Gemini AI 👇 👇 👇
        String response = geminiService.askAI(command);

        // 3️⃣ Save command + response to DB
        saveHistory(command, response);

        // 4️⃣ Return response to controller → UI
        return response;
    }

    // 🔹 Helper method to save history
    private void saveHistory(String command, String response) {
        CommandHistory history = new CommandHistory();
        history.setCommand(command);
        history.setResponse(response);
        history.setTimestamp(Instant.now());

        repository.save(history);
    }

    // 🔹 Used by /api/aeris/history
    public List<CommandHistory> getAllHistory() {
        return repository.findAll();
    }

    public void deleteHistory(Long id) {
        repository.deleteById(id);
    }
}
