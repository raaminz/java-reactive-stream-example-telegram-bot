package com.github.zare88.webseartch.subscriber;

import com.github.zare88.webseartch.db.Requests;
import com.pengrad.telegrambot.model.Update;

import java.time.LocalDateTime;
import java.util.concurrent.Flow;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DataPersistSubscriber implements Flow.Subscriber<Update> {
    private final Logger logger = Logger.getLogger(DataPersistSubscriber.class.getName());
    private Flow.Subscription subscription;

    public DataPersistSubscriber() {
        logger.setLevel(Level.FINER);
        logger.addHandler(new ConsoleHandler());

        Requests.DDL.createTable();
    }

    @Override
    public void onSubscribe(Flow.Subscription subscription) {
        this.subscription = subscription;
        subscription.request(1);
    }

    @Override
    public void onNext(Update update) {
        Long chatId = update.message().chat().id();
        String text = update.message().text();
        logger.log(Level.INFO, () -> String.format("&&& New Request logged. chatId: %d , text: %s", chatId, text));

        Requests.DAO.insert(LocalDateTime.now(), chatId, text);
        subscription.request(1);
    }

    @Override
    public void onError(Throwable throwable) {
        throwable.printStackTrace();
        logger.throwing(DataPersistSubscriber.class.getName(), "DataPersistSubscriber.onError", throwable);
    }

    @Override
    public void onComplete() {
        logger.info("DataPersistSubscriber is done!");
    }
}
