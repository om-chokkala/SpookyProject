package org.example;

import com.googlecode.lanterna.input.KeyStroke;
import java.io.IOException;
import java.util.List;

public class Spooky
{
    private final char spooky;
    private final char space;
    private final Screen screen;

    public Spooky(Screen screen)
    {
        this.spooky = '\u2620';
        this.screen = screen;
        this.space = ' ';
    }

    public void spookyMovement(List<Position> obstacles, List<Position> bombPositions) throws IOException, InterruptedException
    {
        boolean continueReadingInput = true;
        int column = 40;
        int row = 11;
        int oldColumn;
        int oldRow;
        boolean scoreIncrement;
        screen.putChar(column, row, spooky);
        while (continueReadingInput)
        {
            scoreIncrement = true;
            KeyStroke keyStroke;
            do
            {
                Thread.sleep(5);
                keyStroke = screen.getInput();
            } while (keyStroke == null);

            oldColumn = column;
            oldRow = row;

            switch (keyStroke.getKeyType())
            {
                case ArrowDown -> row++;
                case ArrowUp -> row--;
                case ArrowRight -> column += 2;
                case ArrowLeft -> column -= 2;
            }
            char c1;
            char c2;
            for (Position p : obstacles)
            {
                if ((p.getColumn() == column && p.getRow() == row) || (column == 0) || (column == 78) || (row == 0) || (row == 23))
                {
                    c1 = screen.getChar(p.getColumn(), p.getRow());
                    c2 = screen.getChar(column, row);

                    if (c1 == c2)
                    {
                        scoreIncrement = false;
                        screen.setScore(-2);
                        System.err.println(":-( Obstacle: -2\tScore: " + screen.getScore());
                        column = oldColumn;
                        row = oldRow;
                    }
                }
            }
            for (Position p : bombPositions)
            {
                if ((p.getColumn() == column && p.getRow() == row) || (column == 0) || (column == 78) || (row == 0) || (row == 23))
                {
                    c1 = screen.getChar(p.getColumn(), p.getRow());
                    c2 = screen.getChar(column, row);
                    if (c1 == c2)
                    {
                        scoreIncrement = false;
                        screen.setScore(-2);
                        System.err.println(":-( Bomb: -2\tScore: " + screen.getScore());
                        screen.close();
                        System.exit(0);
                    }
                }
            }
            screen.putChar(oldColumn, oldRow, space);
            screen.putChar(column, row, spooky);
            if(scoreIncrement)
            {
                screen.setScore(2);
                System.out.println(":-) Treasure: +2\tScore: " + screen.getScore());
            }

            Character quit = keyStroke.getCharacter();
            if (quit == Character.valueOf('q'))
            {
                continueReadingInput = false;
                System.out.println("quit");
            }
        }
    }
}
