package com.gler.assignment.controller;


import com.gler.assignment.dto.TextRequestDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TextReplaceController {

    @GetMapping("/replace")
    public ResponseEntity<String> replaceText(@Valid TextRequestDTO request) {
        String text = request.getText();

        if (text.length() == 2) {
            return ResponseEntity.ok("");
        }

        String result = "*" + text.substring(1, text.length() - 1) + "$";

        return ResponseEntity.ok(result);
    }
}
