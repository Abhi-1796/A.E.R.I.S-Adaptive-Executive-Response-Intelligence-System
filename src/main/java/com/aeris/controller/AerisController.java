package com.aeris.controller;

import com.aeris.model.CommandHistory;
import com.aeris.service.AerisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/aeris")
public class AerisController {

    @Autowired
    private AerisService aerisService;

    @GetMapping("/status")
    public String status(){
        return "Aeris is Online";
    }

    @PostMapping("/command")
    public String command(@RequestBody Map<String, String> body){
        return aerisService.processCommand(body.get("command"));
    }
        // GET history
    @GetMapping("/history")
    public List<CommandHistory> getHistory() {
        return aerisService.getAllHistory();
    }

    @DeleteMapping("/history/{id}")
    public void deleteHistory(@PathVariable Long id) {
        aerisService.deleteHistory(id);
    }

}
