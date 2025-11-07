import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("------------ TODO MANAGEMENT SYSTEM ---------");
        System.out.println("Choose Database:");
        System.out.println("1) PostgreSQL (working)");
        System.out.println("2) MySQL (not implemented yet)");
        System.out.println("3) MongoDB (not implemented yet)");
        System.out.print("Enter your choice: ");
        String choice = sc.nextLine().trim();

        StorageFactory.Type type = StorageFactory.Type.POSTGRESQL;
        if (choice.equals("2")) type = StorageFactory.Type.MYSQL;
        else if (choice.equals("3")) type = StorageFactory.Type.MONGODB;

        TodoStorage storage = null;
        try {
            storage = StorageFactory.create(type);
        } catch (Exception e) {
            System.err.println("Failed to create storage: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }

        TodoService service = new TodoService(storage);

        while (true) {
            System.out.println("\n========= MENU =========");
            System.out.println("1) Create TODO");
            System.out.println("2) Get TODO by ID");
            System.out.println("3) Update TODO");
            System.out.println("4) Delete TODO");
            System.out.println("5) List all TODOs");
            System.out.println("6) List incomplete TODOs");
            System.out.println("7) Exit");
            System.out.print("Choose an option: ");
            String opt = sc.nextLine().trim();

            try {
                switch (opt) {
                    //CREATE
                    case "1":
                        System.out.print("Enter title: ");
                        String title = sc.nextLine();
                        System.out.print("Enter description: ");
                        String desc = sc.nextLine();
                        Todo todo = new Todo(title, desc, false);
                        service.createTodo(todo);
                        System.out.println("✅ TODO created successfully! ID: " + todo.getId());
                        break;

                    // RETRIEVE
                    case "2":
                        System.out.print("Enter TODO ID: ");
                        int rid = Integer.parseInt(sc.nextLine());
                        Todo found = service.getTodo(rid);
                        if (found == null) {
                            System.out.println("❌ TODO not found.");
                        } else {
                            System.out.println(found);
                        }
                        break;

                    // UPDATE
                    case "3":
                        System.out.print("Enter TODO ID to update: ");
                        int uid = Integer.parseInt(sc.nextLine());
                        Todo existing = service.getTodo(uid);
                        if (existing == null) {
                            System.out.println("❌ TODO not found.");
                            break;
                        }

                        System.out.print("New title (press Enter to keep current): ");
                        String nt = sc.nextLine();
                        if (!nt.isEmpty()) existing.setTitle(nt);

                        System.out.print("New description (press Enter to keep current): ");
                        String nd = sc.nextLine();
                        if (!nd.isEmpty()) existing.setDescription(nd);

                        System.out.print("Is completed? (true/false, press Enter to keep current): ");
                        String c = sc.nextLine();
                        if (!c.isEmpty()) existing.setCompleted(Boolean.parseBoolean(c));

                        service.updateTodo(existing);
                        System.out.println("✅ TODO updated successfully!");
                        break;

                    // DELETE
                    case "4":
                        System.out.print("Enter TODO ID to delete: ");
                        int did = Integer.parseInt(sc.nextLine());
                        service.deleteTodo(did);
                        System.out.println("🗑️ TODO deleted (if it existed).");
                        break;

                    // LIST ALL
                    case "5":
                        List<Todo> all = service.getAll();
                        if (all.isEmpty()) {
                            System.out.println("No TODOs found.");
                        } else {
                            System.out.println("==== All TODOs ====");
                            all.forEach(System.out::println);
                        }
                        break;

                    //  LIST INCOMPLETE
                    case "6":
                        List<Todo> list = service.getAll();
                        List<Todo> incomplete = list.stream()
                                .filter(t -> !t.isCompleted())
                                .toList();
                        if (incomplete.isEmpty()) {
                            System.out.println("🎉 All TODOs are complete!");
                        } else {
                            System.out.println("==== Incomplete TODOs ====");
                            incomplete.forEach(System.out::println);
                        }
                        break;

                    //  EXIT
                    case "7":
                        System.out.println("👋 Goodbye!");
                        System.exit(0);
                        break;

                    default:
                        System.out.println("⚠️ Invalid option. Try again.");
                }
            } catch (NumberFormatException nfe) {
                System.out.println("⚠️ Please enter a valid numeric ID.");
            } catch (Exception ex) {
                System.err.println("Operation failed: " + ex.getMessage());
                ex.printStackTrace();
            }
        }
    }
}
