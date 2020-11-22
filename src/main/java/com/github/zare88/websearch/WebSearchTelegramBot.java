package com.github.zare88.websearch;

import com.github.zare88.websearch.subscriber.BotUpdateSubscriber;
import com.github.zare88.websearch.subscriber.DataPersistSubscriber;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;

import java.util.concurrent.SubmissionPublisher;


public class WebSearchTelegramBot {

    public static void main(String[] args) {
        if (args.length == 0) {
            throw new IllegalArgumentException("Telegram bot token required as argument.");
        }
        TelegramBotContext.INSTANCE.initializeBot(args[0]);
        new WebSearchTelegramBot().startListening();
    }

    public void startListening() {
        var publisher = new SubmissionPublisher<Update>();

        publisher.subscribe(new BotUpdateSubscriber());
        publisher.subscribe(new DataPersistSubscriber());

        TelegramBotContext.INSTANCE.getTelegramBot().setUpdatesListener((var updates) -> {
            updates.forEach(publisher::submit);
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });
    }

}
