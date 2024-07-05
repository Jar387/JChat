package com.jar36.jchat.client.widgets;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.terminal.Terminal;

import java.io.IOException;

import static com.jar36.jchat.client.AsyncUIWidget.*;

public class MessageBox {
    public static final char MB_OK = 0x1;
    public static final char MB_CANCEL = 0x2; // combine usage
    public static final char MB_CONFIRM = 0x3; // combine usage

    public int focus;
    public int x;
    public int y;
    public String message;
    public String title;
    public char style;

    public MessageBox(String message, char style) {
        this((width - Math.max(message.length() + 4, 22)) / 2, (col - 5) / 2, message, "Message Box", style);
    }

    public MessageBox(String message, String title, char style) {
        this((width - Math.max(message.length() + 4, 22)) / 2, (col - 5) / 2, message, title, style);
    }

    public MessageBox(int x, int y, String message, char style) {
        this(x, y, message, "Message Box", style);
    }

    public MessageBox(int x, int y, String message, String title, char style) {
        this.x = x;
        this.y = y;
        this.message = message;
        this.title = title;
        this.style = style;
        if (style == MB_OK) {
            focus = FOCUS_RIGHT;
        } else if (style == MB_CANCEL || style == MB_CONFIRM) {
            focus = FOCUS_LEFT;
        }
    }

    public int showMessageBox(Terminal terminal) throws IOException {
        drawVirtualWindow(terminal, x, y, Math.max(message.length() + 4, 22), 5, title);
        terminal.setCursorPosition(x + 2, y + 1);
        terminal.putString(message);
        if (style == MessageBox.MB_CANCEL || style == MessageBox.MB_CONFIRM) {
            drawButton(terminal, x + 2, y + 3, MessageBox.MB_CANCEL, true);
            drawButton(terminal, x + Math.max(message.length() + 4, 22) - 2 - 8, y + 3, MessageBox.MB_CONFIRM, false);
            focus = FOCUS_LEFT;
        } else {
            drawButton(terminal, x + Math.max(message.length() + 4, 22) - 2 - 8, y + 3, style, true);
            focus = FOCUS_RIGHT;
        }
        terminal.flush();
        while (true) {
            KeyStroke keyStroke = terminal.readInput();
            if (keyStroke.getKeyType() == KeyType.ArrowLeft || keyStroke.getKeyType() == KeyType.ArrowRight) {
                // switch button status
                if (focus == FOCUS_LEFT) {
                    focus = FOCUS_RIGHT;
                    drawButton(terminal, x + 2, y + 3, MessageBox.MB_CANCEL, false);
                    drawButton(terminal, x + Math.max(message.length() + 4, 22) - 2 - 8, y + 3, MessageBox.MB_CONFIRM, true);
                    terminal.flush();
                    continue;
                }
                if (focus == FOCUS_RIGHT) {
                    focus = FOCUS_LEFT;
                    drawButton(terminal, x + 2, y + 3, MessageBox.MB_CANCEL, true);
                    drawButton(terminal, x + Math.max(message.length() + 4, 22) - 2 - 8, y + 3, MessageBox.MB_CONFIRM, false);
                    terminal.flush();
                    continue;
                }
            }
            if (keyStroke.getKeyType() == KeyType.Enter) {
                // erase messageBox first
                drawRectangle(TextColor.ANSI.BLUE, terminal, x, y, Math.max(message.length() + 4, 22) + 2, 5 + 1);
                return focus;
            }
            terminal.flush();
        }
    }
}
