package com.example.demo02;


import com.google.ai.client.generativeai.GenerativeModel;

public class GeminiManager
{
    private static GeminiManager instance;
    private GenerativeModel gemini;

    private GeminiManager() {
        gemini = new GenerativeModel(
                "gemini-2.5-flash",
                BuildC
        );
    }
}
