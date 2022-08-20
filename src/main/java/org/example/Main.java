package org.example;
import java.io.IOException;

public class Main
{
    public static void main(String[] args) throws IOException, InterruptedException
    {
        Screen screen = new Screen();
        Spooky spooky = new Spooky(screen);
        screen.screenWithDots();
        spooky.spookyMovement(screen.getObstaclePositions(), screen.getBombPositions());
    }
}