package com.jar36.jchat.client;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;

import java.io.IOException;

public class UI{
    public static Terminal terminal;
    public static void InitializeUI() throws IOException {
        terminal = new DefaultTerminalFactory().setInitialTerminalSize(new TerminalSize(80, 24))
                .setTerminalEmulatorTitle("JChat client")
                .createTerminal();
        terminal.enterPrivateMode();
        terminal.addResizeListener((terminalIn, newSize) -> {
            AsyncUIWidget.width = newSize.getRows();
            AsyncUIWidget.col = newSize.getColumns();
            try {
                AsyncUIWidget.preFrame(terminalIn);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        AsyncUIWidget.preFrame(terminal);
    }
}
