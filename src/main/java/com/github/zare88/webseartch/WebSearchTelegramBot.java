package com.github.zare88.webseartch;

import com.github.zare88.webseartch.subscriber.BotUpdateSubscriber;
import com.github.zare88.webseartch.subscriber.DataPersistSubscriber;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;

import java.util.concurrent.SubmissionPublisher;


public class WebSearchTelegramBot {

    public static void main(String[] args) {
        if (args.length == 0) {
            throw new IllegalArgumentException("Telegram bot token required as argument.");
        }
        TelegramBotContext.INSTANCE.setBotToken(args[0]);
        new WebSearchTelegramBot().startListening();
    }

    public void startListening() {
        SubmissionPublisher<Update> publisher = new SubmissionPublisher<>();

        publisher.subscribe(new BotUpdateSubscriber());
        publisher.subscribe(new DataPersistSubscriber());

        TelegramBotContext.INSTANCE.getTelegramBot().setUpdatesListener(updates -> {
            updates.forEach(publisher::submit);
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });
    }

}
