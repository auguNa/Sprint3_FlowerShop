import Nivel1_txt_persistance.menu.MainMenu;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class MainMenuTest {
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    private MainMenu mainMenu;

    @BeforeEach
    public void setUp() {
        System.setOut(new PrintStream(outputStreamCaptor));
        mainMenu = new MainMenu();
    }

    @Test
    public void testCreateFlorist() {
        String input = "1\nFloristName\n0\n";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
        System.setIn(in);

        mainMenu.start();

        String output = outputStreamCaptor.toString(StandardCharsets.UTF_8);
        assertTrue(output.contains("Florist 'FloristName' created"));
    }

    @Test
    public void testStockManagementWithoutFlorist() {
        String input = "2\n0\n";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
        System.setIn(in);

        mainMenu.start();

        String output = outputStreamCaptor.toString(StandardCharsets.UTF_8);
        assertTrue(output.contains("Please create a florist first."));
    }

    @Test
    public void testSalesManagementWithoutFlorist() {
        String input = "3\n0\n";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
        System.setIn(in);

        mainMenu.start();

        String output = outputStreamCaptor.toString(StandardCharsets.UTF_8);
        assertTrue(output.contains("Please create a florist first."));
    }

    @Test
    public void testCreateFloristWhenAlreadyExists() {
        String input = "1\nFloristName\n1\nAnotherFloristName\n0\n";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
        System.setIn(in);

        mainMenu.start();

        String output = outputStreamCaptor.toString(StandardCharsets.UTF_8);
        assertTrue(output.contains("Florist already exists: FloristName"));
    }
}
