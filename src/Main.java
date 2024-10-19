import command.CommandHandler;

public class Main {
    private static final String INTRODUCTION = "Welcome to Campus Chaos 2024. Enter 'help' for more details on the commands.";

    public static void main(String[] args) {
        System.out.println(INTRODUCTION);

        CommandHandler commandHandler = new CommandHandler();
        commandHandler.handleUserInput();
    }
}
