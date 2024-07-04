package com.jar36.jchat.client;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.terminal.Terminal;

import java.io.IOException;

public class AsyncUIWidget {
    public static String title = "JChat client gui v0.1";
    public static int width = 80;
    public static int col = 24;
    public static void drawStringMidAlign(Terminal terminal, String string, int column) throws IOException {
        if(string.length()>width||column>col){
            return;
        }
        int offset = (width-string.length())/2;
        terminal.setCursorPosition(offset, column);
        terminal.putString(string);
    }
    public static void clearScreen(Terminal terminal) throws IOException {
        for(int i=0;i<width*col;i++){
            terminal.putCharacter(' ');
        }
    }
    public static void drawRectangle(TextColor textColor, Terminal terminal, int xUL, int yUL, int width, int height) throws IOException {
        terminal.setBackgroundColor(textColor);
        for(int i=xUL;i<xUL+width;i++){
            for(int j=yUL;j<yUL+height;j++){
                terminal.setCursorPosition(i, j);
                terminal.putCharacter(' ');
            }
        }
    }
    public static void drawLandscapeLine(Terminal terminal, int x, int y, int length, char c) throws IOException {
        for(int i=x;i<x+length;i++){
            terminal.setCursorPosition(i, y);
            terminal.putCharacter(c);
        }
    }
    public static void drawHorizonLine(Terminal terminal, int x, int y, int length, char c) throws IOException{
        for(int i=y;i<y+length;i++){
            terminal.setCursorPosition(x, i);
            terminal.putCharacter(c);
        }
    }
    public static void drawDottedRectangle(TextColor textColor, Terminal terminal, int xUL, int yUL, int width, int height) throws IOException {
        // draw upper side with ascii 0xc4 '─'
        // draw left side with ascii '|'
        // draw corners with ascii ⌐¬┐└
        terminal.setForegroundColor(textColor);
        terminal.setCursorPosition(xUL, yUL);
        terminal.putCharacter('┌');
        drawLandscapeLine(terminal, xUL+1, yUL, width-2, '─');
        terminal.putCharacter('┐');
        drawHorizonLine(terminal, xUL, yUL+1, height-2, '│');
        drawHorizonLine(terminal, xUL+width-1, yUL+1, height-2, '│');
        terminal.setCursorPosition(xUL, yUL+height-1);
        terminal.putCharacter('└');
        drawLandscapeLine(terminal, xUL+1, yUL+height-1, width-2, '─');
        terminal.putCharacter('┘');
    }
    public static void drawDottedRectangleForVirtualWindow(TextColor textColor1, TextColor textColor2, Terminal terminal, int xUL, int yUL, int width, int height) throws IOException {
        // draw upper side with ascii 0xc4 '─'
        // draw left side with ascii '|'
        // draw corners with ascii ⌐¬┐└
        terminal.setForegroundColor(textColor1);
        terminal.setCursorPosition(xUL, yUL);
        terminal.putCharacter('┌');
        drawLandscapeLine(terminal, xUL+1, yUL, width-2, '─');
        terminal.setForegroundColor(textColor2);
        terminal.putCharacter('┐');
        terminal.setForegroundColor(textColor1);
        drawHorizonLine(terminal, xUL, yUL+1, height-2, '│');
        terminal.setForegroundColor(textColor2);
        drawHorizonLine(terminal, xUL+width-1, yUL+1, height-2, '│');
        terminal.setCursorPosition(xUL, yUL+height-1);
        terminal.setForegroundColor(textColor1);
        terminal.putCharacter('└');
        terminal.setForegroundColor(textColor2);
        drawLandscapeLine(terminal, xUL+1, yUL+height-1, width-2, '─');
        terminal.putCharacter('┘');
    }
    public static void drawVirtualWindow(Terminal terminal, int x, int y, int w, int h) throws IOException {
        drawRectangle(TextColor.ANSI.BLACK, terminal, x+1, y+1, w+1, h);
        drawRectangle(TextColor.ANSI.WHITE, terminal, x, y, w, h);
        drawDottedRectangleForVirtualWindow(TextColor.ANSI.WHITE_BRIGHT, TextColor.ANSI.BLACK_BRIGHT, terminal, x, y, w, h);
    }
    public static void preFrame(Terminal terminal) throws IOException {
        terminal.setBackgroundColor(TextColor.ANSI.BLUE);
        terminal.setForegroundColor(TextColor.ANSI.WHITE_BRIGHT);
        terminal.setCursorVisible(false);
        clearScreen(terminal);
        drawStringMidAlign(terminal, title, 0);
        drawVirtualWindow(terminal, 3, 4, 12, 5);
        terminal.flush();
    }
}
