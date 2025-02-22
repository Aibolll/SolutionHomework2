import java.util.*;

/**
 * MUDController:
 * A simple controller that reads player input and orchestrates
 * basic commands like look around, move, pick up items,
 * check inventory, show help, etc.
 */
public class MUDController {

    private final Player player;
    private boolean running;
    private final Scanner scanner;

    public MUDController() {
        this.player = new Player();
        this.running = true;
        this.scanner = new Scanner(System.in);
        initializeGame();
    }

    private void initializeGame() {
        Room startRoom = new Room("A small stone chamber", "A dimly lit room with stone walls.");
        startRoom.addItem(new Item("sword"));
        startRoom.addItem(new Item("shield"));
        player.setCurrentRoom(startRoom);
    }

    public void runGameLoop() {
        System.out.println("Welcome to the MUD game! Type 'help' for a list of commands.");
        while (running) {
            System.out.print("> ");
            String input = scanner.nextLine().trim();
            handleInput(input);
        }
        System.out.println("Thanks for playing!");
    }

    public void handleInput(String input) {
        if (input.isEmpty()) return;

        String[] parts = input.split(" ", 2);
        String command = parts[0].toLowerCase();
        String argument = (parts.length > 1) ? parts[1] : "";

        switch (command) {
            case "look":
                lookAround();
                break;
            case "move":
                move(argument);
                break;
            case "pick":
                if (argument.startsWith("up ")) pickUp(argument.substring(3));
                else System.out.println("Invalid command format! Use: pick up <itemName>");
                break;
            case "inventory":
                checkInventory();
                break;
            case "help":
                showHelp();
                break;
            case "quit":
            case "exit":
                running = false;
                break;
            default:
                System.out.println("Unknown command. Type 'help' for a list of commands.");
        }
    }

    private void lookAround() {
        System.out.println(player.getCurrentRoom().describe());
    }

    private void move(String direction) {
        System.out.println("You can't move in this version yet!");
    }

    private void pickUp(String itemName) {
        Room currentRoom = player.getCurrentRoom();
        Item item = currentRoom.getItem(itemName);

        if (item == null) {
            System.out.println("No item named '" + itemName + "' here!");
        } else {
            player.addItem(item);
            currentRoom.removeItem(item);
            System.out.println("You picked up " + itemName + ".");
        }
    }

    private void checkInventory() {
        if (player.getInventory().isEmpty()) {
            System.out.println("Your inventory is empty.");
        } else {
            System.out.println("You are carrying:");
            for (Item item : player.getInventory()) {
                System.out.println("- " + item.getName());
            }
        }
    }

    private void showHelp() {
        System.out.println("Available commands:");
        System.out.println("look - Describe the current room.");
        System.out.println("move <forward|back|left|right> - Move in a direction.");
        System.out.println("pick up <itemName> - Pick up an item.");
        System.out.println("inventory - List items in your inventory.");
        System.out.println("help - Show this menu.");
        System.out.println("quit/exit - End the game.");
    }

    public static void main(String[] args) {
        new MUDController().runGameLoop();
    }
}

class Player {
    private Room currentRoom;
    private final List<Item> inventory = new ArrayList<>();

    public Room getCurrentRoom() {
        return currentRoom;
    }

    public void setCurrentRoom(Room room) {
        this.currentRoom = room;
    }

    public void addItem(Item item) {
        inventory.add(item);
    }

    public List<Item> getInventory() {
        return inventory;
    }
}

class Room {
    private final String name;
    private final String description;
    private final List<Item> items = new ArrayList<>();

    public Room(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public void addItem(Item item) {
        items.add(item);
    }

    public void removeItem(Item item) {
        items.remove(item);
    }

    public Item getItem(String itemName) {
        return items.stream().filter(i -> i.getName().equalsIgnoreCase(itemName)).findFirst().orElse(null);
    }

    public String describe() {
        StringBuilder desc = new StringBuilder(name + ": " + description + "\nItems here: ");
        if (items.isEmpty()) desc.append("none");
        else items.forEach(i -> desc.append(i.getName()).append(", "));
        return desc.toString();
    }
}

class Item {
    private final String name;

    public Item(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
