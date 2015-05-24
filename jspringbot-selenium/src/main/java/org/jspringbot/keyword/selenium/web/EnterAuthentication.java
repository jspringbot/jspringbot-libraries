package org.jspringbot.keyword.selenium.web;

import org.jspringbot.KeywordInfo;
import org.jspringbot.keyword.selenium.AbstractSeleniumKeyword;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.awt.event.KeyEvent;


@Component
@KeywordInfo(
        name = "Enter Authentication",
        parameters = {"username", "password"},
        description = "classpath:desc/EnterAuthentication.txt"
)
public class EnterAuthentication extends AbstractSeleniumKeyword {

    @Override
    public Object execute(Object[] params) throws Exception {
        String username = String.valueOf(params[0]);
        String password = String.valueOf(params[1]);

        Robot robot = new Robot();

        Keyboard keyboard = new Keyboard(robot);

        keyboard.type(username);

        robot.keyPress(KeyEvent.VK_TAB);
        robot.keyRelease(KeyEvent.VK_TAB);
        Thread.sleep(500);

        keyboard.type(password);

        robot.keyPress(KeyEvent.VK_ENTER);
        robot.keyRelease(KeyEvent.VK_ENTER);

        return null;
    }
}
