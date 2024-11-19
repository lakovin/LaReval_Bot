package Ethan.project.LaReval_Bot.service;

import Ethan.project.LaReval_Bot.config.BotConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot {

    final BotConfig config;

    public TelegramBot(BotConfig config) {
        this.config = config;
    }

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public String getBotToken() {
        return config.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {

        if(update.hasMessage() && update.getMessage().hasText()) {
            // Получает текст сообщения, приводит к нижнему регистру и удаляет лишние пробелы
            String messageText = update.getMessage().getText().trim().toLowerCase().replaceAll("[!?.]+$", "");
            long chatId = update.getMessage().getChatId();

            if (messageText.equals("для чего ты создан")) {
                sendMessage(chatId, "Я бот для бета тестирования.");
            } else if (messageText.equals("как тебя зовут")) {
                sendMessage(chatId, "Меня зовут LaReval_Bot.");
            } else if (messageText.equals("сколько тебе лет")) {
                sendMessage(chatId, "Я просто набор кода, так что мне нет возраста.");
            } else if (messageText.equals("/start")) {
                startCommandReceived(chatId, update.getMessage().getChat().getFirstName());
            } else {
                sendMessage(chatId, "Извини, пока я не могу тебе этим помочь /(ㄒoㄒ)/~~");
            }
        }
    }

    // Метод приветствующий на /start а так же сохраняет в базу никнейм юзера
    private void startCommandReceived(long chatId, String name) {


        String answer = "Привет, " + name +", рад тебя видеть.(￣┰￣*)";
        log.info("Replied to user " + name);


        sendMessage(chatId, answer);
    }

    // Метод для отправки сообщения пользователю
    private void sendMessage(long chatId, String textToSend) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);

        try{
            execute(message);
        }
        catch (TelegramApiException e) {
            log.error("Error occurred: " + e.getMessage());
        }
    }
}
