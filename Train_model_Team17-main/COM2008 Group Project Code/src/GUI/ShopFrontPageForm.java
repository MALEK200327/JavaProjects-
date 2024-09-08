package GUI;

import javax.swing.*;
import java.net.URL;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.HashMap;


public class ShopFrontPageForm extends JFrame {

    private Map<Product, Integer> selectedItems = new HashMap<>();
    private JLabel basketCountLabel; 
    private BasketForm basketForm;
    private List<Product> productList;
    private User currentUserFromLogin;
    

    public ShopFrontPageForm(User currentUser) {

       
        super("Trains Of Sheffield");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 500);

        // Title label
        JLabel titleLabel = new JLabel("Trains Of Sheffield");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);

        this.currentUserFromLogin = currentUser;

        JButton accountsButton = new JButton("Account");
        accountsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                
                openAccountsForm(currentUser);
                
            }
        });


        //Staff and manager buttons
        JButton staffButton = new JButton("Staff");
        staffButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                
                openStaffForm();
                
            }
        });

        JButton managerButton = new JButton("Manager");
        managerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               
                openManagerForm();
                
            }
        });

        // Adding staff button
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        if(currentUser.getRole() == 1 || currentUser.getRole() == 2) {  //If user has role of staff or manager
            headerPanel.add(staffButton);
        }
        if(currentUser.getRole() == 2) { //If user has role of just manager
            headerPanel.add(managerButton);
        }

        if(currentUser.getRole() != 4) {
            headerPanel.add(accountsButton);
        }
        

        // Initialise the basketCountLabel with '0' indicating the basket is empty
        basketCountLabel = new JLabel("0");
        basketCountLabel.setFont(new Font("Arial", Font.BOLD, 16));
        basketCountLabel.setForeground(Color.RED);

        // Header panel with the basket image as a button
        //JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        ImageIcon originalBasketIcon = new ImageIcon(getClass().getResource("/GUI/images/basket.jpg")); // Ensure this path is correct
        Image basketImage = originalBasketIcon.getImage();
        
        Image newBasketImg = basketImage.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        ImageIcon basketIcon = new ImageIcon(newBasketImg);
        JButton goToBasketButton = new JButton(basketIcon);
        goToBasketButton.setBorderPainted(false);
        goToBasketButton.setContentAreaFilled(false);
        goToBasketButton.setFocusPainted(false);
        // Modify goToBasketButton's ActionListener
        goToBasketButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (basketForm == null || !basketForm.isVisible()) {
                    basketForm = new BasketForm(selectedItems, currentUser);
                    basketForm.setBasketUpdateCallback(new BasketForm.BasketUpdateCallback() {
                        @Override
                        public void onBasketUpdated(Map<Product, Integer> updatedBasket) {
                            int totalQuantity = updatedBasket.values().stream().mapToInt(Integer::intValue).sum();
                            basketCountLabel.setText(String.valueOf(totalQuantity));
                        }
                    });
                    basketForm.setVisible(true);
                } else {
                    basketForm.requestFocus();
                }
            }
        });

        JPanel combinedHeaderPanel = new JPanel(new BorderLayout());
        combinedHeaderPanel.add(titleLabel, BorderLayout.CENTER);
        combinedHeaderPanel.add(headerPanel, BorderLayout.EAST);


        // Create a layered pane to overlap the basket count on the basket button
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(basketIcon.getIconWidth(), basketIcon.getIconHeight()));
        goToBasketButton.setBounds(0, 0, basketIcon.getIconWidth(), basketIcon.getIconHeight());
        basketCountLabel.setBounds(30, -5, 20, 20); // Position the label on the top-right corner of the button

        layeredPane.add(goToBasketButton, Integer.valueOf(1));
        layeredPane.add(basketCountLabel, Integer.valueOf(2));
        
        if(currentUser.getRole() != 4) {
            headerPanel.add(layeredPane);
        }
        JPanel mainContainer = new JPanel(new BorderLayout());

        mainContainer.add(combinedHeaderPanel, BorderLayout.NORTH);

        // Panel to contain the items
        JPanel itemsPanel = new JPanel(new GridLayout(0, 3, 10, 10)); // 3 items per row


        populateShop(itemsPanel);

        // Add the itemsPanel to the mainContainer panel
        mainContainer.add(itemsPanel, BorderLayout.CENTER);


        // Add the mainContainer panel to the frame's content pane
        getContentPane().add(mainContainer);

        // JScrollPane to enable scrolling
        JScrollPane scrollPane = new JScrollPane(mainContainer);
        getContentPane().add(scrollPane);

        setVisible(true);

        headerPanel.setBackground(new Color(230, 230, 250)); // Light Lavender background
        goToBasketButton.setOpaque(true);
        goToBasketButton.setBackground(new Color(255, 255, 255)); // White background for button

        headerPanel.setBackground(new Color(230, 230, 250)); // Light lavender background

        goToBasketButton.setOpaque(true);
        goToBasketButton.setBackground(new Color(255, 255, 255)); // White background

        mainContainer.setBackground(new Color(245, 245, 245)); // Soft gray background

        itemsPanel.setBackground(new Color(245, 245, 245)); // Soft gray background

        mainContainer.setBackground(new Color(245, 245, 245)); // Soft Gray background
    }

    private void populateShop(JPanel panel) {
         
        DatabaseConnectionHandler databaseConnectionHandler = new DatabaseConnectionHandler();
        DatabaseOperations databaseOperations = new DatabaseOperations();

        try {
            databaseConnectionHandler.openConnection();
            productList = databaseOperations.getAllProducts(databaseConnectionHandler.getConnection());
        }
        catch(Throwable e) {
            e.printStackTrace();
        }

        for(Product product: productList) {
            addItem(panel, product);
        }
    } 

    private static void openStaffForm() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                StaffForm staffForm = new StaffForm();
                staffForm.setVisible(true); 
            }
        });
    }

    private static void openManagerForm() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                ManagerForm managerForm = new ManagerForm();
                managerForm.setVisible(true); 
            }
        });
    }


    private static void openAccountsForm(User currentUser) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    AccountsForm example = new AccountsForm(currentUser);
                    example.setVisible(true); 
                }
            });
    }


    private void addItem(JPanel panel, Product product) {
        JPanel itemPanel = new JPanel(new BorderLayout(5, 5));
    itemPanel.setBorder(BorderFactory.createLineBorder(new Color(192, 192, 192))); // Light Gray border
    itemPanel.setBackground(Color.WHITE); // White background for each item
                
    // Load the image and place it at the top of the itemPanel
    ImageIcon productImageIcon = getProductImageIcon(product.getProductCode());        
    Image productImage = productImageIcon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
    JLabel imageLabel = new JLabel(new ImageIcon(productImage));
    itemPanel.add(imageLabel, BorderLayout.NORTH);       
    
    // Create a sub-panel for the product name, price, and stock with vertical layout
    JPanel textPanel = new JPanel();
    textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
    textPanel.setBackground(Color.WHITE); 
    
    // Create and add a label for the product name
    JLabel itemNameLabel = new JLabel(product.getProductName());
    itemNameLabel.setAlignmentX(Component.CENTER_ALIGNMENT); 
    textPanel.add(itemNameLabel);
    
    // Create and add a label for the product price
    JLabel priceLabel = new JLabel(String.format("£%.2f", product.getPrice()));
    priceLabel.setAlignmentX(Component.CENTER_ALIGNMENT); 
    textPanel.add(priceLabel);

    DatabaseOperations databaseOperations = new DatabaseOperations();
    DatabaseConnectionHandler databaseConnectionHandler = new DatabaseConnectionHandler();

    JLabel stockLabel = null;
    try {
        databaseConnectionHandler.openConnection();
        stockLabel = new JLabel("Stock: " + databaseOperations.getStockByProductCode(databaseConnectionHandler.getConnection(), product.getProductCode()));
        stockLabel.setAlignmentX(Component.CENTER_ALIGNMENT); 
        textPanel.add(stockLabel);
    } catch (SQLException e) {
        e.printStackTrace();
    } finally {
        databaseConnectionHandler.closeConnection();
    }
    
    itemPanel.add(textPanel, BorderLayout.CENTER);
        
        
        // Create and add the "Add to Basket" button at the bottom of the itemPanel
        JButton addButton = new JButton("Add to Basket");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(currentUserFromLogin.getRole() != 4) {
                    int currentQuantity = selectedItems.getOrDefault(product, 0);
                    selectedItems.put(product, currentQuantity + 1);
                    updateBasketCountLabel();
                    if (basketForm != null && basketForm.isVisible()) {
                        basketForm.updateBasketContents(selectedItems);
                        basketForm.updateTotalPriceLabel(); // Update total price in the open basket form
                    }
                }
                else {
                    SwingUtilities.invokeLater(() ->
                                JOptionPane.showMessageDialog(null, "Please login to make a purchase!")
                    );
                }
                
            }
        });

        itemPanel.add(addButton, BorderLayout.SOUTH);
            
        itemPanel.setBackground(Color.WHITE); // White background for each item panel
        itemPanel.setBorder(BorderFactory.createLineBorder(new Color(210, 210, 210))); // Soft border

        // Styling the Add to Basket button
        addButton.setBackground(new Color(173, 216, 230)); // Light blue background
        addButton.setForeground(Color.WHITE); // White text
        addButton.setFocusPainted(false);
        addButton.setFont(new Font("Arial", Font.BOLD, 12));
        addButton.setOpaque(true);
        // Style the "Add to Basket" button
        addButton.setBackground(new Color(173, 216, 230)); // Light Blue background
        addButton.setOpaque(true);
        addButton.setBorderPainted(false);
        // Wrap the itemPanel in another panel that uses FlowLayout to avoid vertical stretching
        JPanel flowPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        flowPanel.setBackground(Color.WHITE); // Set the background to white
        flowPanel.add(itemPanel);
    
        panel.add(flowPanel);
    }
    


        
    private void updateBasketCountLabel() {
        // Calculate the total quantity of all products
        int totalQuantity = selectedItems.values().stream().mapToInt(Integer::intValue).sum();
        basketCountLabel.setText(String.valueOf(totalQuantity));
    }


    private ImageIcon getProductImageIcon(String productCode) {
        String imagePath;
    
        switch (productCode.charAt(0)) {
            case 'L': 
                imagePath = "/GUI/images/locomotive.jpg";
                break;
            case 'S': 
                imagePath = "/GUI/images/rollingstock.jpeg";
                break;
            case 'R': 
                imagePath = "/GUI/images/trackpiece.jpg";
                break;
            case 'M': 
                imagePath = "/GUI/images/trainset.jpg";
                break;
            case 'P': 
                imagePath = "/GUI/images/trackpack.jpg";
                break;
            case 'C': 
                imagePath = "/GUI/images/controller.jpg";
                break;
            default:
                imagePath = "/GUI/images/default.jpg"; // Default image if no match
                break;
        }

    URL imageUrl = getClass().getResource(imagePath);
    if (imageUrl != null) {
        return new ImageIcon(imageUrl);
    } else {
        // Handle missing resource
        System.err.println("Resource not found: " + imagePath);
        return null;
    }
        
    }
}
