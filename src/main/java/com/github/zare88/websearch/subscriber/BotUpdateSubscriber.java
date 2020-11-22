package com.github.zare88.websearch.subscriber;


import com.github.zare88.websearch.TelegramBotContext;
import com.github.zare88.websearch.api.ddg.DuckDuckGo;
import com.github.zare88.websearch.api.ddg.DuckDuckGoResponse;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.AbstractSendRequest;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SendPhoto;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Flow;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BotUpdateSubscriber implements Flow.Subscriber<Update> {

    private final Logger logger = Logger.getLogger(BotUpdateSubscriber.class.getName());
    private Flow.Subscription subscription;

    public BotUpdateSubscriber() {
        logger.setLevel(Level.FINER);
        logger.addHandler(new ConsoleHandler());
    }

    @Override
    public void onSubscribe(Flow.Subscription subscription) {
        this.subscription = subscription;
        subscription.request(1);
    }

    @Override
    public void onNext(Update update) {
        String keyword = update.message().text();
        logger.log(Level.INFO, () -> String.format("####Received a new search keyword : %s", keyword));
        DuckDuckGo duckDuckGo = new DuckDuckGo();
        try {
            DuckDuckGoResponse response;
            AbstractSendRequest<?> request;
            if (keyword != null) {
                if (keyword.equals("/start")) {
                    request = new SendMessage(update.message().chat().id(), "well come!");
                } else {
                    response = duckDuckGo.query(keyword).get();
                    if (response.getAbstractText() == null || response.getAbstractText().isBlank()) {
                        request = new SendMessage(update.message().chat().id(), "No result has found!!");
                    } else {
                        String text = duckDuckGo.getFormattedText(keyword, response.getAbstractText());
                        if (response.getImageURL() == null || response.getImageURL().isBlank()) {
                            request = new SendMessage(update.message().chat().id(), text);
                        } else {
                            request = new SendPhoto(update.message().chat().id(),
                                    response.getImageData().get()).caption(text);
                        }
                    }
                }
            }
            else request = new SendMessage(update.message().chat().id() , "please send word or text!");
            TelegramBotContext.INSTANCE.getTelegramBot().execute(request);
        } catch (InterruptedException | ExecutionException e) {
            throw new IllegalStateException(e);
        } finally {
            subscription.request(1);
        }

    }

    @Override
    public void onError(Throwable throwable) {
        logger.log(Level.SEVERE, "Error occurred while processing request.");
        logger.throwing(BotUpdateSubscriber.class.getName(), "BotUpdateSubscriber.onError", throwable);
    }

    @Override
    public void onComplete() {
        logger.info("BotUpdateSubscriber is done!");
    }
}