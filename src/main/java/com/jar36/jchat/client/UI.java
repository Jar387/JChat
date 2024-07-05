package com.jar36.jchat.client;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import com.jar36.jchat.client.widgets.MessageBox;

import java.io.IOException;

public class UI {
    public static Terminal terminal;

    public static void error(String reason) throws IOException {
        MessageBox messageBox = new MessageBox(reason, "error", MessageBox.MB_OK);
        messageBox.showMessageBox(terminal);
        System.exit(-1);
    }

    public static void InitializeUI() throws IOException {
        terminal = new DefaultTerminalFactory().setInitialTerminalSize(new TerminalSize(80, 24))
                .setTerminalEmulatorTitle("JChat client")
                .createTerminal();
        terminal.enterPrivateMode();
        terminal.addResizeListener((terminalIn, newSize) -> {
            try {
                error("TODO: handle resize");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        AsyncUIWidget.initFrame(terminal);
    }
}
