package com.company;


import com.company.exeption.ProductExeption;
import com.company.exeption.UserException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import static com.company.Role.ADMIN;
import static com.company.Role.CLIENT;

public class Main {
    private static final String NEATPAZINTA_IVESTIS = "Neatpazinta ivestis";
    private static final Scanner SC = new Scanner(System.in);

    public static void main(String[] args) throws  UserException, ProductExeption {
        UserService userService = new UserService();


        try {
            while (true) {

                printMainMenu();
                String loginInput = SC.nextLine();

                switch (loginInput) {
                    case "1":
                        System.out.print("Iveskite prisijungimo varda: ");
                        String username = SC.nextLine();
                        System.out.print("Iveskite slaptazodi: ");
                        String password = SC.nextLine();
                        try {
                            User loggedInUser = userService.getUser(username, password);
                            userMenu(loggedInUser, userService);
                        } catch (UserException e) {
                            System.out.println(e.getMessage());
                        }
                        break;
                    case "2":
                        registerNewUser(userService);
                        break;
                    case "3":
                        return;
                    default:
                        System.out.println(NEATPAZINTA_IVESTIS);
                        break;
                }
            }
        } catch (IOException e) {
            System.out.println("ERROR");
        }
    }

    private static void userMenu(User user, UserService userService) throws IOException, ProductExeption, UserException {

        if (user.getRole().equals(CLIENT)) {

            clientUserMenu(user);
        } else if (user.getRole().equals(ADMIN)) {

            adminUserMenu(user, userService);
        }
    }

    private static void adminUserMenu(User adminUser, UserService userService) throws IOException, ProductExeption {
        ProductService productService = new ProductService();
        OrderService orderService = new OrderService();

        while (true) {
            printAdminMenu();
            String choice = SC.nextLine();

            switch (choice) {
                case "1":
                    List<Product> allProducts = productService.getAllProducts();
                    printAllProducts(allProducts);
                    break;
                case "2":
                    List<User> allUsers = userService.getAllUsers();
                    printAllUsers(allUsers);
                    break;
                case "3":
                    try {
                        List<Order> allOrders = orderService.getAllOrders();
                        printAllOrders(allOrders);
                    } catch (InputMismatchException e) {
                        System.out.println(e.getMessage());
                    }

                    break;
                case "4":
                    addNewProduct(productService);

                    break;
                case "5":
                    System.out.println("-------Prekės trynimas-------------");
                    System.out.print("Iveskite trinamos prekes modeli: ");
                    String modelToDelete = SC.nextLine();

                    try {
                        productService.deleteProductByModel(modelToDelete);
                        System.out.printf("Prekes modelis \'%s\' sekmingai istrintas \n", modelToDelete);

                    } catch (ProductExeption e) {
                        System.out.println((e.getMessage()));
                    }

                    break;
                case "6":
                    return;

                default:
                    System.out.println(NEATPAZINTA_IVESTIS);
                    break;
            }
        }
    }

    private static void clientUserMenu(User clientUser) throws IOException, UserException {
        OrderService orderService = new OrderService();
        ProductService productService = new ProductService();

        while (true) {
            printClientMenu();
            String choice = SC.nextLine();

            switch (choice) {
                case "1":
                    List<Product> allProducts = productService.getAllProducts();
                    printAllProducts(allProducts);
                    break;
                case "2":
                    addToShoppingCart(productService);
                    break;
                case "3":
                    System.out.print("Iveskite trinamos prekes modeli: ");
                    String modelToDelete = SC.nextLine();

                    try {
                        productService.deleteCartProductByModel(modelToDelete);

                        System.out.println("----------------------------------------------------------");
                        System.out.printf("Prekes modelis \'%s\' sekmingai istrintas \n", modelToDelete);
                        System.out.println("-----------------------------------------------------------");


                    } catch (ProductExeption e) {
                        System.out.println((e.getMessage()));
                    }

                    break;
                case "4":
                    List<Product> cart = productService.getAllCartProduct();
                    printShopingCart(cart);
                    break;
                case "5":
                    System.out.println("Norite pateikti uzsakyma (T/N)?");
                    String input = SC.nextLine();

                    if (input.equalsIgnoreCase("T")) {

                        Payment payment = getPayment();
                        Delivery delivery = getDelivery();
                        List<Product> products=productService.getAllCartProduct();
                        String quantity="";
                        Double amount=0.0;
                        User user=clientUser;
                        orderService.addOrder(new Order(payment,delivery,quantity,amount,products,user));

                    } else if (input.equalsIgnoreCase("N")) {
                        printClientMenu();
                    }

                    break;
                case "6":
                    getUserOrder(SC, orderService);

                    break;
                case "7":
                    return;
                default:
                    System.out.println(NEATPAZINTA_IVESTIS);
                    break;
            }
        }
    }


    private static Payment getPayment() {
        System.out.println("Pasirinkite atsiskaitymo buda:");
        Payment[] payments = Payment.values();
        while (true) {
            for (int i = 0; i < payments.length; i++) {
                System.out.printf("[%d] %s\n", i + 1, payments[i]);
            }
            int choicePayment = SC.nextInt();
            SC.nextLine();
            if (choicePayment >= 1 && choicePayment <= payments.length) {
                return payments[choicePayment - 1];
            }
        }
    }

    private static Delivery getDelivery() {
        System.out.println("Pasirinkite pristatymo buda:");
        Delivery[] deliveries = Delivery.values();
        while (true) {
            for (int i = 0; i < deliveries.length; i++) {
                System.out.printf("[%d] %s\n", i + 1, deliveries[i]);
            }
            int choicePayment = SC.nextInt();
            SC.nextLine();
            if (choicePayment >= 1 && choicePayment <= deliveries.length) {
                return deliveries[choicePayment - 1];
            }
        }
    }

    private static void addNewProduct(ProductService productService) throws IOException {

        System.out.println("------------Naujos prekes ivedimas------------");
        System.out.print("Iveskite prekes pavadinima: ");
        String title = SC.nextLine();
        System.out.print("Iveskite prekes gamintoja: ");
        String producer = SC.nextLine();
        System.out.print("Iveskite prekes modeli: ");
        String model = SC.nextLine();
        System.out.print("Iveskite prekes kaina: ");
        double price = SC.nextDouble();
        SC.nextLine();

        productService.addProduct(new Product(title, producer, model, price));
        System.out.println("--------------Preke sekmingai itraukta i asortimenta------------------");
        System.out.println();
    }

    private static void addToShoppingCart(ProductService productService) throws IOException {
        System.out.println("-------Prekiu krepselis-------------");
        String keepShopping;

        do {
            System.out.print("Iveskite prekes pavadinima: ");
            String titleToAdd = SC.nextLine();
            System.out.print("Iveskite prekes gamintoją: ");
            String producerToAdd = SC.nextLine();
            System.out.print("Iveskite prekes modeli: ");
            String modelToAdd = SC.nextLine();

            Product productToAdd = productService.getCartProduct(titleToAdd, producerToAdd, modelToAdd);
            if (productToAdd != null) {
                productService.addToCart(productToAdd);
                System.out.println("----------------------------------------");
                System.out.printf("Preke \'%s %s %s\' sekmingai ideta \n", titleToAdd, producerToAdd, modelToAdd);
                System.out.println("----------------------------------------");

            } else {
                System.out.println("Tokia preke neegzistuoja");
            }

            System.out.println("Testi apsipirkima(T/N)?");
            keepShopping = SC.nextLine();
        }
        while (keepShopping.equalsIgnoreCase("T"));

    }

    private static void registerNewUser(UserService userService) throws IOException, UserException {

        System.out.println("------------Naujo vartotojo registracija:------------");
        System.out.println();
        String username = getValidUsername(userService);

        System.out.print("Iveskite slaptazodi: ");
        String password = SC.nextLine();
        System.out.print("Iveskite varda: ");
        String name = SC.nextLine();
        System.out.print("Iveskite pavarde: ");
        String surname = SC.nextLine();
        System.out.print("Iveskite adresa: ");
        String address = SC.nextLine();

        User user = new User(username, password, name, surname, address);
        userService.addUser(user);
        System.out.println("Vartotojas sekmingai uzregistruotas");
        System.out.println();

        clientUserMenu(user);
    }

    private static void getUserOrder(Scanner SC, OrderService orderService) throws FileNotFoundException {

        System.out.print("Iveskite prisijungimo varda: ");
        String username = SC.nextLine();

        User ordersForUser = orderService.getALLOrderForUser(username);
        for (Order order : ordersForUser.getOrders()) {
            if (!order.equals(null)) {
                System.out.println(order);
            }
            System.out.println("Jus neturite suformuotu uzsakymu");


        }


    }


    private static String getValidUsername(UserService userService) throws FileNotFoundException {

        String username = "";
        while (true) {
            System.out.print("Iveskite prisijungimo varda: ");
            username = SC.nextLine();
            if (!userService.isUserExists(username)) {
                return username;
            }
            System.out.printf("Vartotojo vardas \'%s\' uzimtas\n", username);
            System.out.println();

        }
    }

    private static void printShopingCart(List<Product> cart) {
        System.out.println("-------PREKES:-----");
        System.out.println();
        for (Product product : cart) {
            System.out.println("Pavadinimas: " + product.getTitle());
            System.out.println("Gamintojas: " + product.getProducer());
            System.out.println("Modelis: " + product.getModel());
            System.out.println("Kaina: " + product.getPrice() + "Eur");
            System.out.println("");
        }
    }

    private static void printAllUsers(List<User> users) {

        for (User user : users) {
            printUserForAdmin(user);
        }
    }

    private static void printAllProducts(List<Product> products) {

        for (Product product : products) {
            System.out.println("--------------------------------------");
            System.out.println("Prekės pavadinimas: " + product.getTitle());
            System.out.println("Gamintojas: " + product.getProducer());
            System.out.println("Modelis: " + product.getModel());
            System.out.println("Kaina: " + product.getPrice() + " Eur");
            System.out.println();
        }
    }

    private static void printAllOrders(List<Order> orders) throws InputMismatchException {


        for (Order order : orders) {
            System.out.println("-------------UZSAKYMAS--------------------");
            System.out.println("Klientas: " + order.getUser().getUsername());
//            System.out.println("Prekes: "+ order.getProducts());
            System.out.println("Prekiu kiekis: " + order.getQuantity());
            System.out.println("Suma be PVM: " + order.getAmount());
            System.out.println("Suma su PVM: " + order.getAmount() * 1.21);
            System.out.println("Apmokejimo budas: " + order.getPayment());
            System.out.println("Pristatymo budas: " + order.getDelivery());
            System.out.println();


        }
    }

    private static void printUserForAdmin(User adminUser) {
        System.out.println("--------------------------------------");
        System.out.println("Prisijungimo vardas: " + adminUser.getUsername());
        System.out.println("Vardas: " + adminUser.getName());
        System.out.println("Pavarde: " + adminUser.getSurname());
        System.out.println("Adresas: " + adminUser.getAddress());
        System.out.println("Role: " + adminUser.getRole());
        System.out.println("---------------------------------------");
    }

    private static void printAdminMenu() {
        System.out.println();
        System.out.println("--------------MENIU----------------");
        System.out.println("[1] Perziureti visa prekiu sarasa");
        System.out.println("[2] Perziureti registruotu vartotoju informacija");
        System.out.println("[3] Perziureti visus užsakymus");
        System.out.println("[4] Pridėti naują preke");
        System.out.println("[5] Istrinti egzistuojancia preke");
        System.out.println("[6] Atsijungti");
        System.out.println("------------------------------------");
    }

    private static void printClientMenu() {

        System.out.println();
        System.out.println("--------------MENIU----------------");
        System.out.println("[1] Perziureti visa prekiu sarasa");
        System.out.println("[2] Prideti prekę i pirkiniu krepseli");
        System.out.println("[3] Istrinti preke is pirkiniu krepselio");
        System.out.println("[4] Perziureti savo pirkiniu krepseli");
        System.out.println("[5] Pateikti nauja uzsakyma");
        System.out.println("[6] Perziureti savo pateiktus užsakymus");
        System.out.println("[7] Atsijungti");
        System.out.println();
    }

    private static void printMainMenu() {

        System.out.println();
        System.out.println("----PAGRINDINIS MENIU----");
        System.out.println();
        System.out.println("[1] Prisijungti");
        System.out.println("[2] Registruotis");
        System.out.println("[3] Atsijungti");
        System.out.println();
        System.out.println("--------------------------");
        System.out.println();
    }
}



