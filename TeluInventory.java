import java.util.ArrayList;
import java.util.Scanner;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TeluInventory {
    public ArrayList<User> users = new ArrayList<User>();
    public ArrayList<Good> goods = new ArrayList<Good>();
    public ArrayList<History> histories = new ArrayList<History>(); // Riwayat pengambilan
    public Scanner input = new Scanner(System.in);
    public SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public TeluInventory() {
        this.users.add(new User(1, "Default user", "user", "password"));
    }

    public static void main(String[] args) {
        // constructor untuk melakukan inisialisasi applikasi dengan default user.
        // Menggunakan konsep new object agar penggunaan attr dan method tidak static
        TeluInventory App = new TeluInventory();

        System.out.println("WELCOME TO TELKOM INVENTORY");
        System.out.println("In here you can entrust your belongings to us!");
        int choice = 0;
        int result = 0;

        //iteratif 1
        while (true) {
            System.out.println("==============================================");
            User currentUser;
            System.out.println("1. Login\n2. Register\n3. Exit");
            System.out.print("Enter your choice: ");
            choice = App.input.nextInt();
            App.input.nextLine(); // membersihkan buffer \n yang dihasilkan oleh nextInt
            if (choice != 1 && choice != 2 && choice != 3) {
                System.out.println("Your choice is invalid");
                System.out.println("==============================================\n\n\n");
                continue;
            }

            if (choice == 1) {
                System.out.print("Enter username: ");

                String username = App.input.nextLine();
                System.out.print("Enter password: ");
                String password = App.input.nextLine();
                result = App.findUser(username, password);
                if (result < 0) {
                    System.out.println("Your username or password is invalid!");
                    System.out.println("==============================================\n\n\n");
                    continue;
                }
                currentUser = App.users.get(result);
                System.out.println("Welcome " + currentUser.name);
                App.userDashboard(currentUser);
            } else if (choice == 2) {
                System.out.print("Enter your name: ");
                String name = App.input.nextLine();
                System.out.print("Enter your username: ");
                String username = App.input.nextLine();
                System.out.print("Enter your password: ");
                String password = App.input.nextLine();
                App.addUser(name, username, password);
                System.out.println("\nSuccessfully added your account! You can sign in now!");
            } else if (choice == 3) {
                System.out.println("Thank you for using our service. See you next time :)");
                break;
            }
            //end

            System.out.println("==============================================\n\n\n");
        }
    }

    public void userDashboard(User currentUser) {
        int choice;

        //iteratif 2
        while (true) {
            System.out.println("----------------------------------------------");
            System.out.println(
                    "1. Check your belongings\n2. Store your belongings\n3. Take your belongings\n4. Show History\n5. Logout");
            System.out.print("Enter your choice: ");
            choice = this.input.nextInt();
            this.input.nextLine(); // membersihkan buffer \n yang dihasilkan oleh nextInt

            if (choice == 5) {
                break;
            } else if (choice == 1) {
                this.listGoods(currentUser.id);
            } else if (choice == 2) {
                System.out.print("Enter your belonging: ");
                String name = this.input.nextLine();

                this.addGood(name, currentUser.id);
                System.out.println("Success add belonging");
            } else if (choice == 3) {
                this.takeGood(currentUser);
            } else if (choice == 4) {
                this.showHistory(currentUser.id);
            }
            //end

            System.out.println("----------------------------------------------\n\n\n");
        }
    }

    public void takeGood(User currentUser) {
        System.out.println("\n=================== YOUR RECEIPT ===================");

        int index = 0;
        //iteratif 3
        for (Good good : this.goods) {
            if (good.user_id == currentUser.id && !good.taken) {
                index++;
                this.histories.add(new History(currentUser.id, "Took " + good.name));
                System.out.println(index + " | " + good.name);
                good.take();
            }
        }

        if (index > 0) {
            System.out.println("\n" + index + " Item taken successfully!");
            String timestamp = dateFormat.format(new Date());
            System.out.println("Took at: " + timestamp + " By: " + currentUser.name);
        } else {
            System.out.println("\nYou don't have any belongings to take.");
        }
        //end

        System.out.println("======================================================");
    }

    public void listGoods(int userId) {
        System.out.println("\nYour belongings: ");
        int index = 0;
        boolean found = false;

        //iterarif 4
        for (Good good : this.goods) {
            if (good.user_id == userId && !good.taken) {
                index++;
                System.out.println(index + " | " + good.name + " | " + good.created_at);
                found = true;
            }
        }
        //end
        if (index <= 0) {
            System.out.println("You don't have any belongings yet!");
        }
    }

    public void showHistory(int userId) {
        System.out.println("\n=================== HISTORY ===================");
        boolean found = false;

        //iteratif 5
        for (History history : this.histories) {
            if (history.userId == userId) {
                System.out.println("Action: " + history.action + " | Time: " + history.created_at);
                found = true;
            }
        }
        //end


        if (!found) {
            System.out.println("No history found.");
        }
        System.out.println("===============================================");
    }

    public int findUser(String username, String password, int index) {
        // Base Case 1: Jika index sudah melampaui ukuran list (tidak ketemu)

        //rekursif 1
        if (index >= users.size())
            return -1;

        // Base Case 2: Jika data ditemukan
        User user = users.get(index);
        if (user.username.equals(username) && user.password.equals(password)) {
            return index;
        }

        // Rekursi: Panggil diri sendiri dengan index berikutnya
        return findUserRecursive(username, password, index + 1);
    }

    // fungsi find yg lama
    // public int findUser(String username, String password) {
    // int index = 0;
    // for (User user : this.users) {
    // if (user.username.equals(username) && user.password.equals(password)) {
    // return index;
    // }
    // index++;
    // }
    // return -1;
    // }

    public void addUser(String name, String username, String password) {
        User lastUser = this.users.get(this.users.size() - 1);
        this.users.add(new User(lastUser.id + 1, name, username, password));
    }

    public void addGood(String name, int user_id) {
        int id = 1;

        //iteratif 6
        if (this.goods.size() > 0) {
            Good lastGood = this.goods.get(this.goods.size() - 1);
            id = lastGood.id + 1;
        }
        //end

        this.goods.add(new Good(id, name, user_id, false));
        this.histories.add(new History(user_id, "Stored " + name));
    }
}

class User {
    public int id;
    public String name;
    public String username;
    public String password;

    public User(int id, String name, String username, String password) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.password = password;
    }
}

class Good {
    public int id;
    public String name;
    public int user_id;
    public boolean taken;
    public String created_at;
    public String updated_at;
    public SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public Good(int id, String name, int user_id, boolean taken) {
        String now = dateFormat.format(new Date());

        this.id = id;
        this.name = name;
        this.user_id = user_id;
        this.taken = taken;
        this.created_at = now;
        this.updated_at = now;
    }

    public void take() {
        this.taken = true;
        this.updated_at = dateFormat.format(new Date());
    }
}

class History {
    public int userId;
    public String action;
    public String created_at;
    public SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public History(int userId, String action) {
        this.userId = userId;
        this.action = action;
        this.created_at = dateFormat.format(new Date());
    }
}