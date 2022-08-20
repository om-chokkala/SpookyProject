package org.example;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Screen
{
    public static final TextColor YELLOW = new TextColor.RGB(255, 255, 0);

    private DefaultTerminalFactory terminalFactory = new DefaultTerminalFactory();
    private Terminal terminal;
    private char[][] array = new char[80][24];
    private List<Position> obstaclePositions = new ArrayList<>();
    private List<Position> bombPositions = new ArrayList<>();
    private int score;
    public Screen() throws IOException
    {
        terminal = terminalFactory.createTerminal();
        terminal.setCursorVisible(false);
    }

    public int getScore()
    {
        return score;
    }

    public void setScore(int score)
    {
        this.score = this.score + score;
    }

    public char getChar(int col, int row)
    {
        return array[col - 1][row - 1];
    }

    public List<Position> getObstaclePositions()
    {
        return obstaclePositions;
    }

    public List<Position> getBombPositions()
    {
        return bombPositions;
    }

    public void putChar(int column, int row, char c) throws IOException
    {
        terminal.setCursorPosition(column, row);
        terminal.putCharacter(c);
        terminal.setForegroundColor(YELLOW);
        array[column - 1][row - 1] = c;
        terminal.flush();
    }

    public void screenWithDots() throws IOException
    {
        char dot = '.';
        for (int col = 2; col < 79; col += 2)
        {
            for (int row = 1; row < 23; row++)
            {
                putChar(col, row, dot);
            }
        }
        obstacles();
    }

    public void obstacles() throws IOException
    {
        char block = '\u2588';
        char bomb = '\u2601';

        int[] obstacleArrayX = new int[]{16, 32, 48, 64};
        int[] obstacleArrayY = new int[]{5, 9, 13, 17};

        int[] bombArrayX = new int[]{8, 24, 40, 56, 72};
        int[] bombArrayY = new int[]{4, 8, 12, 16, 20};

        for (int col = 2; col < 79; col++)
        {
            for (int row = 1; row < 23; row++)
            {
                // Adding border
                if (col == 2 || col == 78 || row == 1 || row == 22)
                {
                    obstaclePositions.add(new Position(col, row));
                    putChar(col,row, block);
                }
                // Adding obstacles
                if((contains(obstacleArrayX,col)) && (contains(obstacleArrayY, row)))
                {
                    obstaclePositions.add(new Position(col, row));
                    putChar(col,row, block);
                }
                // Adding bombs
                if((contains(bombArrayX,col)) && (contains(bombArrayY, row)))
                {
                    bombPositions.add(new Position(col,row));
                    putChar(col, row, bomb);
                }
            }
        }
    }
    public static boolean contains(int[] arr, int item)
    {
        for (int n : arr)
        {
            if (item == n)
            {
                return true;
            }
        }
        return false;
    }

    public KeyStroke getInput() throws IOException
    {
        return terminal.pollInput();
    }

    public void close() throws IOException, InterruptedException
    {
        terminal.clearScreen();
        String lost = "You have lost the game! Try again!";
        String score = "Your score is " + getScore();
        for (int i = 0; i < lost.length(); i++)
        {
            putChar(i + 22, 8, lost.charAt(i));
        }
        for (int i = 0; i < score.length(); i++)
        {
            putChar(i + 29, 14, score.charAt(i));
        }
        terminal.flush();
        Thread.sleep(4000);
        terminal.close();
    }
}
