package com.github.zare88.websearch;

import com.pengrad.telegrambot.TelegramBot;

import java.util.Objects;

public class TelegramBotContext {
    public static final TelegramBotContext INSTANCE = new TelegramBotContext();
    private TelegramBot bot;

    private TelegramBotContext() {
    }

    public TelegramBot getTelegramBot() {
        return Objects.requireNonNull(bot);
    }

    public void setBotToken(String token) {
        bot = new TelegramBot(token);
    }
}