package com.jar36.jchat.client.widgets;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.terminal.Terminal;
import com.sun.istack.internal.Nullable;

import java.io.IOException;

import static com.jar36.jchat.client.AsyncUIWidget.*;

public class Label {
    public int x;
    public int y;
    public String[] message;
    @Nullable
    public String title;

    private final int maxLen;

    public Label(int x, int y, String title, String[] message) {
        this.maxLen = getMaxLen(message);
        this.x = x;
        this.y = y;
        this.title = title;
        this.message = message;
    }

    public Label(int x, int y, String[] message) {
        this(x, y, null, message);
    }

    public Label(String[] message, String title) {
        this((width - Math.max(getMaxLen(message) + 4, 22)) / 2, (col - 5) / 2, title, message);
    }

    public Label(String[] message) {
        this((width - Math.max(getMaxLen(message) + 4, 22)) / 2, (col - 5) / 2, null, message);
    }

    private static int getMaxLen(String[] message) {
        int len = 0;
        for (String s : message) {
            if (s.length() > len) {
                len = s.length();
            }
        }
        return len;
    }

    public void showLabel(Terminal terminal) throws IOException {
        if (title == null) {
            drawVirtualWindow(terminal, x, y, maxLen + 4, message.length + 2);
        } else {
            drawVirtualWindow(terminal, x, y, maxLen + 4, message.length + 2, title);
        }
        for (int i = 0; i < message.length; i++) {
            terminal.setCursorPosition(x + 2, y + i + 1);
            terminal.putString(message[i]);
        }
        terminal.flush();
    }

    public void removeLabel(Terminal terminal) throws IOException {
        drawRectangle(TextColor.ANSI.BLUE, terminal, x, y, maxLen + 6, 5 + 1);
        terminal.flush();
    }
}
