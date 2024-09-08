package GUI;

import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.mindrot.jbcrypt.BCrypt;

import GUI.Order.STATUS;

public class DatabaseOperations {

    private static final String KEY = "16ByteSecretKey!"; // Change to a securely generated key



    public void insertUser(User newUser, Address newAddress, Connection connection) throws SQLException {
        try {
            //Insert into user, address and customer


            String insertAddressQuery = "INSERT INTO address (houseNumber, street, city, postcode) VALUES (?, ?, ?, ?)";
            PreparedStatement addressStatement = connection.prepareStatement(insertAddressQuery, PreparedStatement.RETURN_GENERATED_KEYS);
            addressStatement.setInt(1, newAddress.getHouseNumber());
            addressStatement.setString(2, newAddress.getStreet());
            addressStatement.setString(3, newAddress.getCity());
            addressStatement.setString(4, newAddress.getPostcode());
            int rowsAffected = addressStatement.executeUpdate();

            if (rowsAffected == 0) {
                throw new SQLException("Creating address failed, no rows affected.");
            }


            //Retrieves the auto-incremented addressID
            int addressID;
            try (var generatedKeys = addressStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    addressID = generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Creating address failed, no ID obtained.");
                }
            }

            //Now insert into user table using the retrieved addressID
            String insertUserQuery = "INSERT INTO user (email, password, role, forename, surname, addressID) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement userStatement = connection.prepareStatement(insertUserQuery);
            userStatement.setString(1, newUser.getEmail());
            userStatement.setString(2, newUser.getPassword());
            userStatement.setInt(3, newUser.getRole());
            userStatement.setString(4, newUser.getForename());
            userStatement.setString(5, newUser.getSurname());
            userStatement.setInt(6, addressID);
            rowsAffected += userStatement.executeUpdate();
            




            addressStatement.close();
            userStatement.close();
            connection.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
            throw e; // Re-throw the exception to signal an error.
        }
    }

    public String encrypt(String data) {
        try {
            SecretKeySpec secretKey = new SecretKeySpec(KEY.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            byte[] iv = new byte[16];
            new SecureRandom().nextBytes(iv);
            IvParameterSpec ivSpec = new IvParameterSpec(iv);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);

            byte[] encrypted = cipher.doFinal(data.getBytes());
            byte[] encryptedIVAndText = new byte[iv.length + encrypted.length];
            System.arraycopy(iv, 0, encryptedIVAndText, 0, iv.length);
            System.arraycopy(encrypted, 0, encryptedIVAndText, iv.length, encrypted.length);

            return Base64.getEncoder().encodeToString(encryptedIVAndText);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String decrypt(String encryptedData) {
        try {
            SecretKeySpec secretKey = new SecretKeySpec(KEY.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    
            byte[] encryptedIVAndText = Base64.getDecoder().decode(encryptedData);
            byte[] iv = new byte[16];
            System.arraycopy(encryptedIVAndText, 0, iv, 0, iv.length);
            IvParameterSpec ivSpec = new IvParameterSpec(iv);
    
            int encryptedSize = encryptedIVAndText.length - iv.length;
            byte[] encryptedBytes = new byte[encryptedSize];
            System.arraycopy(encryptedIVAndText, iv.length, encryptedBytes, 0, encryptedSize);
    
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec);
            byte[] original = cipher.doFinal(encryptedBytes);
    
            return new String(original);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    


    
    public void insertStaff(Staff newStaff, Connection connection) throws SQLException {
        try {
            String insertSQL = "INSERT INTO user (email, password, role) VALUES (?, ?, ?)";

            PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);

            
            preparedStatement.setString(1, newStaff.getEmail());
            preparedStatement.setString(2, newStaff.getPassword());
            preparedStatement.setInt(3, 1);
            

            int rowsAffected = preparedStatement.executeUpdate();

        }
        catch (SQLException e) {
            e.printStackTrace();
            throw e; // Re-throw the exception to signal an error.
        }
    }

    public void insertOrder(Connection connection, Order order, List<OrderLine> orderLines) throws SQLException {
        String insertOrderQuery = "INSERT INTO `order` (userID, order_date, price_total, order_status) VALUES (?, ?, ?, ?)";
        String insertOrderLineQuery = "INSERT INTO orderLine (orderID, product_code, quantity) VALUES (?, ?, ?)";
        String selectStockQuery = "SELECT stock FROM stock WHERE product_code = ?";
        String updateStockQuery = "UPDATE stock SET stock = stock - ? WHERE product_code = ?";
        boolean allProductsInStock = true;
    
        try {
            // Disable autocommit
            connection.setAutoCommit(false);
    
            try (PreparedStatement orderStatement = connection.prepareStatement(insertOrderQuery, PreparedStatement.RETURN_GENERATED_KEYS)) {
                orderStatement.setInt(1, order.getUserID());
                orderStatement.setString(2, order.getOrderDate());
                orderStatement.setDouble(3, order.getPriceTotal());
                orderStatement.setString(4, "confirmed");
    
                int rowsAffected = orderStatement.executeUpdate();
                if (rowsAffected == 0) {
                    throw new SQLException("Creating order failed, no rows affected.");
                }
    
                int orderID;
                try (ResultSet generatedKeys = orderStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        orderID = generatedKeys.getInt(1);
                    } else {
                        throw new SQLException("Creating order failed, no ID obtained.");
                    }
                }
    
                // Check stock availability for all order lines
                for (OrderLine orderLine : orderLines) {
                    try (PreparedStatement stockStatement = connection.prepareStatement(selectStockQuery)) {
                        stockStatement.setString(1, orderLine.getProduct_code());
                        ResultSet resultSet = stockStatement.executeQuery();
    
                        if (resultSet.next()) {
                            int availableStock = resultSet.getInt("stock");
                            int requestedQuantity = orderLine.getQuantity();
    
                            // If there is no stock, set to false and break the loop
                            if (availableStock < requestedQuantity) {
                                allProductsInStock = false;
                                break;
                            }
                        } else {
                            // Product not in stock - set flag to false and break the loop
                            allProductsInStock = false;
                            break;
                        }
                    }
                }
    
                if (allProductsInStock) {
                    // All products are in stock. insert order lines
                    try (PreparedStatement orderLineStatement = connection.prepareStatement(insertOrderLineQuery)) {
                        for (OrderLine orderLine : orderLines) {
                            orderLineStatement.setInt(1, orderID);
                            orderLineStatement.setString(2, orderLine.getProduct_code());
                            orderLineStatement.setInt(3, orderLine.getQuantity());
    
                            orderLineStatement.addBatch();
                        }
                        // Execute batch update for all order lines
                        orderLineStatement.executeBatch();
                    }
    
                    // Commit the transaction as all order lines have been successfully added
                    connection.commit();

                    for (OrderLine orderLine : orderLines) {
                        try (PreparedStatement updateStockStatement = connection.prepareStatement(updateStockQuery)) {
                            updateStockStatement.setInt(1, orderLine.getQuantity());
                            updateStockStatement.setString(2, orderLine.getProduct_code());
        
                            updateStockStatement.executeUpdate();
                        }
                    }
                } else {
                    // Rollback the transaction as not all products are in stock
                    connection.rollback();
                    throw new SQLException("Not all products are in stock. Order not placed.");
                }
            }
        } catch (SQLException e) {
            // Rollback the transaction if there is an exception
            connection.rollback();
            throw e;
        } finally {
            connection.setAutoCommit(true);
        }
    }

    public List<OrderLine> getOrderLinesByOrderID(Connection connection, int orderID) throws SQLException {
        List<OrderLine> orderLines = new ArrayList<>();
        String query = "SELECT * FROM orderLine WHERE OrderID = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, orderID);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int orderLineID = resultSet.getInt("orderLineID");
                    String productID = resultSet.getString("product_code");
                    int quantity = resultSet.getInt("quantity");

                    // Create OrderLine object and add it to the list
                    OrderLine orderLine = new OrderLine(orderLineID, orderID, productID, quantity);
                    orderLines.add(orderLine);
                }
            }
        }

        return orderLines;
    }

    

    public void insertBankDetails(Connection connection, BankDetails bankDetails, User user) throws SQLException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            // Check if the user has existing bank details
            String checkExistingSQL = "SELECT * FROM bank_details WHERE userID = ?";
            preparedStatement = connection.prepareStatement(checkExistingSQL);
            preparedStatement.setInt(1, user.getUserID());
            resultSet = preparedStatement.executeQuery();
            
            // If the user has existing bank details, delete them
            if (resultSet.next()) {
                String deleteSQL = "DELETE FROM bank_details WHERE userID = ?";
                preparedStatement = connection.prepareStatement(deleteSQL);
                preparedStatement.setInt(1, user.getUserID());
                preparedStatement.executeUpdate();
            }
            
            // Insert the new bank details
            String insertSQL = "INSERT INTO bank_details (card_holder_name, card_number, expiry_date, security_code, userID, bank_card_name) VALUES (?, ?, ?, ?, ?, ?)";

            preparedStatement = connection.prepareStatement(insertSQL);
            preparedStatement.setString(1, bankDetails.getCardName());
            preparedStatement.setString(2, bankDetails.getCardNumber()); // Already encrypted
            preparedStatement.setString(3, bankDetails.getExpiryDate());
            preparedStatement.setString(4, bankDetails.getSecurityCode()); // Already encrypted
            preparedStatement.setInt(5, user.getUserID());
            preparedStatement.setString(6, bankDetails.getNickname());

            preparedStatement.executeUpdate();
            
            
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (resultSet != null) {
                resultSet.close();
            }
            if (preparedStatement != null) {
                preparedStatement.close();
            }
        }
    }

    //BCrypt.checkpw(entered_password, hashedPassword)

    public User checkUserLogin(Connection connection, String email, String entered_password) throws SQLException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT userID, email, password, role, forename, surname, addressID FROM user WHERE email=?";
            stmt = connection.prepareStatement(sql);
            stmt.setString(1, email);
            rs = stmt.executeQuery();

            if (rs.next()) {
                // If there is a result, validate the hashed password using BCrypt
                String hashedPassword = rs.getString("password");
                if (BCrypt.checkpw(entered_password, hashedPassword)) {
                    // Password matches, create and return a User object
                    int userID = rs.getInt("userID");
                    String retrievedEmail = rs.getString("email");
                    String retrievedForename = rs.getString("forename");
                    String retrievedSurname = rs.getString("surname");
                    int retrievedRole = rs.getInt("role");

                    return new User(userID, retrievedEmail, hashedPassword, retrievedForename, retrievedSurname, retrievedRole);
                }
            }
            return null; // If no result is found or password doesnt match, return null
        } catch (SQLException e) {
            e.printStackTrace();
            throw e; // Rethrow the exception to signal an error.
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
        }
    }

        

        public void updateUserData(Connection connection, int userID, String email,String forename, String surname, int role) throws SQLException {
            PreparedStatement preparedStatement = null;
        
            try {
                String sql = "UPDATE user SET email=?, forename=?, surname=?, role=? WHERE userID=?";
                preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, email);
                preparedStatement.setString(2, forename);
                preparedStatement.setString(3, surname);
                preparedStatement.setInt(4, role);
                preparedStatement.setInt(5, userID);
        
                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {

                } else {

                }
            } catch (SQLException e) {
                e.printStackTrace();
                throw e;
            } finally {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            }
        }
        

        public List<User> getAllUsers(Connection connection) throws SQLException {
            PreparedStatement stmt = null;
            ResultSet rs = null;
            List<User> userList = new ArrayList<>();
    
            try {
                String sql = "SELECT userID, email, password, role, forename, surname, addressID FROM user";
                stmt = connection.prepareStatement(sql);
                rs = stmt.executeQuery();
    
                while (rs.next()) {
                    int userID = rs.getInt("userID");
                    String retrievedEmail = rs.getString("email");
                    String retrievedPassword = rs.getString("password");
                    String retrievedForename = rs.getString("forename");
                    String retrievedSurname = rs.getString("surname");
                    int retrievedRole = rs.getInt("role");
    
                    User user = new User(userID, retrievedEmail, retrievedPassword, retrievedForename, retrievedSurname, retrievedRole);
                    userList.add(user);
                }
            } catch (SQLException e) {
                e.printStackTrace();
                throw e;
            } finally {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
            }
            return userList;
        }


    public HashMap<String, Integer> getAllStock(Connection connection) throws SQLException {
            PreparedStatement stmt = null;
            ResultSet rs = null;
            
            HashMap<String, Integer> stockMap = new HashMap<String, Integer>();
    
            try {
                String sql = "SELECT product_code, stock FROM stock";
                stmt = connection.prepareStatement(sql);
                rs = stmt.executeQuery();
    
                while (rs.next()) {
                    String productCode = rs.getString("product_code");
                    int stockQuantity = rs.getInt("stock");

                   
                    stockMap.put(productCode, stockQuantity);
                }
            } catch (SQLException e) {
                e.printStackTrace();
                throw e;
            } finally {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
            }
            return stockMap;
        }

    public List<Product> getAllProducts(Connection connection) throws Exception {
        List<Product> products = new ArrayList<>();
        
        try {
            String selectProduct = "SELECT * FROM product";
            PreparedStatement preparedStatement = connection.prepareStatement(selectProduct);
            ResultSet productResultSet = preparedStatement.executeQuery();

            while (productResultSet.next()) {
                String productCode = productResultSet.getString("product_code");
                String brandName = productResultSet.getString("brand_name");
                String productName = productResultSet.getString("product_name");
                double price = productResultSet.getDouble("price");
                GaugeType gauge = GaugeType.valueOf(productResultSet.getString("gauge"));

                if ("L".equalsIgnoreCase(String.valueOf(productCode.charAt(0)))) {
                    String selectLocomotive = "SELECT * FROM locomotive WHERE product_code = ?";
                    PreparedStatement preparedStatement2 = connection.prepareStatement(selectLocomotive);
                    preparedStatement2.setString(1, productCode);
                    ResultSet locomotiveResultSet = preparedStatement2.executeQuery();
                    if (locomotiveResultSet.next()) {
                        DecoderType decoderType = DecoderType.valueOf(locomotiveResultSet.getString("dcc").toUpperCase());
                        Era era = Era.valueOf(locomotiveResultSet.getString("era").toUpperCase());
                        Product locomotive = new Locomotive(productCode, productName, brandName, price, gauge, decoderType, era);
                        products.add(locomotive);

                    }
                    locomotiveResultSet.close();
                    preparedStatement2.close();
                } else if ("S".equalsIgnoreCase(String.valueOf(productCode.charAt(0)))) {
                    String selectRollingStock = "SELECT * FROM rolling_stock WHERE product_code = ?";
                    PreparedStatement preparedStatement3 = connection.prepareStatement(selectRollingStock);
                    preparedStatement3.setString(1, productCode);
                    ResultSet rollingStockResultSet = preparedStatement3.executeQuery();
                    if (rollingStockResultSet.next()) {
                        Era era = Era.valueOf(rollingStockResultSet.getString("era").toUpperCase());
                        Product rollingStock = new RollingStock(productCode, productName, brandName, price, gauge, era);
                        products.add(rollingStock);
                    }
                    rollingStockResultSet.close();
                    preparedStatement3.close();
                } else if ("R".equalsIgnoreCase(String.valueOf(productCode.charAt(0)))) {
                    String selecttrackPiece = "SELECT * FROM track_piece WHERE product_code = ?";
                    PreparedStatement preparedStatement4 = connection.prepareStatement(selecttrackPiece);
                    preparedStatement4.setString(1, productCode);
                    ResultSet trackPieceSet = preparedStatement4.executeQuery();
                    if (trackPieceSet.next()) {
                        TrackType trackType = TrackType.valueOf(trackPieceSet.getString("piece_name").toUpperCase()); 
                        Product trackPiece = new TrackPiece(productCode, productName, brandName, price, gauge,trackType);
                        products.add(trackPiece);
                    }
                    trackPieceSet.close();
                    preparedStatement4.close();
                } else if ("C".equalsIgnoreCase(String.valueOf(productCode.charAt(0)))) {
                    String selectController = "SELECT * FROM controller WHERE product_code = ?";
                    PreparedStatement preparedStatement5 = connection.prepareStatement(selectController);
                    preparedStatement5.setString(1, productCode);
                    ResultSet controllerResultSet = preparedStatement5.executeQuery();
                    if (controllerResultSet.next()) {
                        DecoderType decoderType = DecoderType.valueOf(controllerResultSet.getString("controller_name").toUpperCase()); 
                        Product controller = new Controller(productCode, productName, brandName, price, gauge,decoderType);
                        products.add(controller);
                    }
                    controllerResultSet.close();
                    preparedStatement5.close();
                } else if ("M".equalsIgnoreCase(String.valueOf(productCode.charAt(0)))) {
                    List<Locomotive> locomotivesArray = new ArrayList<>();
                    List<RollingStock> rollingStockArray = new ArrayList<>();
                    List<TrackPiece> trackPiecesArray = new ArrayList<>();
                    List<Controller> controllersArray = new ArrayList<>();
                    //Retrieve era using the product_code
                    String selectera = "SELECT era FROM train_set WHERE product_code = ?";
                    PreparedStatement preparedStatement0 = connection.prepareStatement(selectera);
                    preparedStatement0.setString(1, productCode);
                    ResultSet trainSetEraResultSet = preparedStatement0.executeQuery();
                    
                    String trainSetEra="OO"; // Default value if not found
                    if (trainSetEraResultSet.next()) {
                        trainSetEra = trainSetEraResultSet.getString("era");
                    }
                    
                    // Retrieve trainSetLineID using the product_code
                    String selectTrainSetID = "SELECT trainSetID FROM train_set WHERE product_code = ?";
                    PreparedStatement preparedStatement1 = connection.prepareStatement(selectTrainSetID);
                    preparedStatement1.setString(1, productCode);
                    ResultSet trainSetIDResultSet = preparedStatement1.executeQuery();
                    
                    int trainSetLineID = -1; // Default value if not found
                    if (trainSetIDResultSet.next()) {
                        trainSetLineID = trainSetIDResultSet.getInt("trainSetID");
                    }
                    trainSetIDResultSet.close();
                    preparedStatement1.close();
                    
                    // Continue with the rest of the code if trainSetLineID is found
                    if (trainSetLineID != -1) {
                        // Retrieve other details using trainSetLineID
                        String selectTrainSet = "SELECT * FROM train_set_line WHERE trainSetLineID = ?";
                        PreparedStatement preparedStatement6 = connection.prepareStatement(selectTrainSet);
                        preparedStatement6.setInt(1, trainSetLineID);
                        ResultSet trainSetResultSet = preparedStatement6.executeQuery();
                        
                        if (trainSetResultSet.next()) {
                            int locomotiveId = trainSetResultSet.getInt("locomotiveID");
                            int rollingStockId = trainSetResultSet.getInt("rolling_stockID");
                            int trackPieceId = trainSetResultSet.getInt("track_pieceID");
                            int controllerId= trainSetResultSet.getInt("controllerID");    
                            // Now, get the corresponding locomotive details
                            String selectLocomotiveForTrainSet = "SELECT * FROM locomotive WHERE locomotiveID = ?";
                            PreparedStatement preparedStatement7 = connection.prepareStatement(selectLocomotiveForTrainSet);
                            preparedStatement7.setInt(1, locomotiveId);
                            ResultSet locomotiveForTrainSetResultSet = preparedStatement7.executeQuery();
                    
                            if (locomotiveForTrainSetResultSet.next()) {
                                DecoderType decoderType = DecoderType.valueOf(locomotiveForTrainSetResultSet.getString("dcc").toUpperCase());
                                Era era = Era.valueOf(locomotiveForTrainSetResultSet.getString("era").toUpperCase());
                                String productCodeTS = locomotiveForTrainSetResultSet.getString("product_code");
                                String selectproductinfo = "SELECT * FROM product WHERE product_code = ?";
                                PreparedStatement preparedStatement06 = connection.prepareStatement(selectproductinfo);
                                preparedStatement06.setString(1, productCodeTS);
                                ResultSet trainSetLCOResultSet = preparedStatement06.executeQuery();
                                if (trainSetLCOResultSet.next()) {
                                    String brandNameTS = trainSetLCOResultSet.getString("brand_name");
                                    String productNameTS = trainSetLCOResultSet.getString("product_name");
                                    double priceTS = trainSetLCOResultSet.getDouble("price");
                                    GaugeType gaugeTS = GaugeType.valueOf(trainSetLCOResultSet.getString("gauge"));
                                    Locomotive locomotiveForTrainSet = new Locomotive(productCodeTS, productNameTS, brandNameTS, priceTS, gaugeTS, decoderType, era);
                                    locomotivesArray.add(locomotiveForTrainSet);
                                }
                            }

                            locomotiveForTrainSetResultSet.close();
                            preparedStatement7.close();
                            // Now, get the corresponding rolling stock details
                            String selectRollingStockForTrainSet = "SELECT * FROM rolling_stock WHERE rollingStockID = ?";
                            PreparedStatement preparedStatement8 = connection.prepareStatement(selectRollingStockForTrainSet);
                            preparedStatement8.setInt(1, rollingStockId);
                            ResultSet rollingStockForTrainSetResultSet = preparedStatement8.executeQuery();
                            if (rollingStockForTrainSetResultSet.next()) {
                                Era era = Era.valueOf(rollingStockForTrainSetResultSet.getString("era").toUpperCase());
                                String productCodeTS = rollingStockForTrainSetResultSet.getString("product_code");
                                String selectproductinfo = "SELECT * FROM product WHERE product_code = ?";
                                PreparedStatement preparedStatement07 = connection.prepareStatement(selectproductinfo);
                                preparedStatement07.setString(1, productCodeTS);
                                ResultSet trainSetRSOResultSet = preparedStatement07.executeQuery();
                                if (trainSetRSOResultSet.next()) {
                                    String brandNameTS = trainSetRSOResultSet.getString("brand_name");
                                    String productNameTS = trainSetRSOResultSet.getString("product_name");
                                    double priceTS = trainSetRSOResultSet.getDouble("price");
                                    GaugeType gaugeTS = GaugeType.valueOf(trainSetRSOResultSet.getString("gauge"));
                                    RollingStock rollingStockForTrainSet = new RollingStock(productCodeTS, productNameTS, brandNameTS, priceTS, gaugeTS, era);
                                    rollingStockArray.add(rollingStockForTrainSet);
                                }
                            }
                            rollingStockForTrainSetResultSet.close();
                            preparedStatement8.close();
                            // Now, get the corresponding track piece details
                            String selectTrackPieceForTrainSet = "SELECT * FROM track_piece WHERE trackPieceID = ?";
                            PreparedStatement preparedStatement9 = connection.prepareStatement(selectTrackPieceForTrainSet);
                            preparedStatement9.setInt(1, trackPieceId);
                            ResultSet trackPieceForTrainSetResultSet = preparedStatement9.executeQuery();
                            if (trackPieceForTrainSetResultSet.next()) {
                                TrackType trackType = TrackType.valueOf(trackPieceForTrainSetResultSet.getString("piece_name").toUpperCase()); 
                                String productCodeTS = trackPieceForTrainSetResultSet.getString("product_code");
                                String selectproductinfo = "SELECT * FROM product WHERE product_code = ?";
                                PreparedStatement preparedStatement08 = connection.prepareStatement(selectproductinfo);
                                preparedStatement08.setString(1, productCodeTS);
                                ResultSet trainSetTPOResultSet = preparedStatement08.executeQuery();
                                if (trainSetTPOResultSet.next()) {
                                    String brandNameTS = trainSetTPOResultSet.getString("brand_name");
                                    String productNameTS = trainSetTPOResultSet.getString("product_name");
                                    double priceTS = trainSetTPOResultSet.getDouble("price");
                                    GaugeType gaugeTS = GaugeType.valueOf(trainSetTPOResultSet.getString("gauge"));
                                    TrackPiece trackPieceForTrainSet = new TrackPiece(productCodeTS, productNameTS, brandNameTS, priceTS, gaugeTS, trackType);
                                    trackPiecesArray.add(trackPieceForTrainSet);
                                }
                            }
                            trackPieceForTrainSetResultSet.close();
                            preparedStatement9.close();
                            // Now, get the corresponding controller details
                            String selectControllerForTrainSet = "SELECT * FROM controller WHERE controllerID = ?";
                            PreparedStatement preparedStatement10 = connection.prepareStatement(selectControllerForTrainSet);
                            preparedStatement10.setInt(1, controllerId);
                            ResultSet controllerForTrainSetResultSet = preparedStatement10.executeQuery();
                            if (controllerForTrainSetResultSet.next()) {
                                DecoderType decoderType = DecoderType.valueOf(controllerForTrainSetResultSet.getString("controller_name").toUpperCase()); 
                                String productCodeTS = controllerForTrainSetResultSet.getString("product_code");
                                String selectproductinfo = "SELECT * FROM product WHERE product_code = ?";
                                PreparedStatement preparedStatement09 = connection.prepareStatement(selectproductinfo);
                                preparedStatement09.setString(1, productCodeTS);
                                ResultSet trainSetCROResultSet = preparedStatement09.executeQuery();
                                if (trainSetCROResultSet.next()) {
                                    String brandNameTS = trainSetCROResultSet.getString("brand_name");
                                    String productNameTS = trainSetCROResultSet.getString("product_name");
                                    double priceTS = trainSetCROResultSet.getDouble("price");
                                    GaugeType gaugeTS = GaugeType.valueOf(trainSetCROResultSet.getString("gauge"));
                                    Controller controllerForTrainSet = new Controller(productCodeTS, productNameTS, brandNameTS, priceTS, gaugeTS, decoderType);
                                    controllersArray.add(controllerForTrainSet);
                                }
                            }
                            controllerForTrainSetResultSet.close();
                            preparedStatement10.close();

                        }

                        trainSetResultSet.close();
                        preparedStatement6.close();}
                        TrainSet trainSet = new TrainSet(
                            productCode,
                            productResultSet.getString("product_name"),
                            productResultSet.getString("brand_name"),
                            productResultSet.getDouble("price"),
                            GaugeType.valueOf(productResultSet.getString("gauge")),
                            locomotivesArray,
                            rollingStockArray,
                            trackPiecesArray,
                            controllersArray,
                            Era.valueOf(trainSetEra)
                        );
                        products.add(trainSet);
                    } else if ("p".equalsIgnoreCase(String.valueOf(productCode.charAt(0)))) {
                    List<TrackPiece> TrackPieceArray = new ArrayList<>();
                    String selectTrackPackID = "SELECT trackPackID FROM track_pack WHERE product_code = ?";
                    PreparedStatement preparedStatement1 = connection.prepareStatement(selectTrackPackID);
                    preparedStatement1.setString(1, productCode);
                    ResultSet trackPackIDResultSet = preparedStatement1.executeQuery();
                    
                    int trainSetLineID = -1; // Default value if not found
                    if (trackPackIDResultSet.next()) {
                        trainSetLineID = trackPackIDResultSet.getInt("trackPackID");
                    }
                    trackPackIDResultSet.close();
                    preparedStatement1.close();
                    if (trainSetLineID != -1) {
                        // Retrieve other details using trainSetLineID
                        String selectTrackPackSet = "SELECT * FROM track_pack_line WHERE trackPackLineID = ?";
                        PreparedStatement preparedStatement6 = connection.prepareStatement(selectTrackPackSet);
                        preparedStatement6.setInt(1, trainSetLineID);
                        ResultSet trainSetResultSet = preparedStatement6.executeQuery();
                        
                        if (trainSetResultSet.next()) {
                            int trackPieceId = trainSetResultSet.getInt("track_pieceID"); 
                            // Now, get the corresponding track piece details
                            String selectTrackPieceForTrainSet = "SELECT * FROM track_piece WHERE trackPieceID = ?";
                            PreparedStatement preparedStatement9 = connection.prepareStatement(selectTrackPieceForTrainSet);
                            preparedStatement9.setInt(1, trackPieceId);
                            ResultSet trackPieceForTrainSetResultSet = preparedStatement9.executeQuery();
                            if (trackPieceForTrainSetResultSet.next()) {
                                TrackType trackType = TrackType.valueOf(trackPieceForTrainSetResultSet.getString("piece_name").toUpperCase()); 
                                String productCodeTS = trackPieceForTrainSetResultSet.getString("product_code");
                                String selectproductinfo = "SELECT * FROM product WHERE product_code = ?";
                                PreparedStatement preparedStatement08 = connection.prepareStatement(selectproductinfo);
                                preparedStatement08.setString(1, productCodeTS);
                                ResultSet trainSetTPOResultSet = preparedStatement08.executeQuery();
                                if (trainSetTPOResultSet.next()) {
                                    String brandNameTS = trainSetTPOResultSet.getString("brand_name");
                                    String productNameTS = trainSetTPOResultSet.getString("product_name");
                                    double priceTS = trainSetTPOResultSet.getDouble("price");
                                    GaugeType gaugeTS = GaugeType.valueOf(trainSetTPOResultSet.getString("gauge"));
                                    TrackPiece trackPieceForTrainSet = new TrackPiece(productCodeTS, productNameTS, brandNameTS, priceTS, gaugeTS, trackType);
                                    TrackPieceArray.add(trackPieceForTrainSet);
                                }
                                trainSetTPOResultSet.close();
                                preparedStatement08.close();

                            }
                            trackPieceForTrainSetResultSet.close();
                            preparedStatement9.close();

                        }

                        trainSetResultSet.close();
                        preparedStatement6.close();}
                        TrackPack trackPack = new TrackPack(
                            productCode,
                            productResultSet.getString("product_name"),
                            productResultSet.getString("brand_name"),
                            productResultSet.getDouble("price"),
                            GaugeType.valueOf(productResultSet.getString("gauge")),
                            TrackPieceArray
                        );
                        products.add(trackPack);
                }
            }
            productResultSet.close();
            preparedStatement.close();
        }
            catch (SQLException e) {
            e.printStackTrace();
            throw e; // Rethrow the exception to signal an error.
        } 
        return products;
    }

    

    public void getUserByID(int ID, Connection connection) throws SQLException {
        try {
            String selectSQL = "SELECT * FROM user WHERE userID=?";
            PreparedStatement preparedStatement = connection.prepareStatement(selectSQL);

            preparedStatement.setInt(1, ID);
        }
        catch (SQLException e) {
            e.printStackTrace();
            throw e;// Rethrow the exception to signal an error.
        }
    }

    public void updateEmail(String newEmail, User newUser, Connection connection) throws SQLException {
        try {
            String updateSQL = "UPDATE user SET email=? WHERE userID=?";
            PreparedStatement preparedStatement = connection.prepareStatement(updateSQL);

            preparedStatement.setString(1, newEmail);
            preparedStatement.setInt(2, newUser.getUserID());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;// Rethrow the exception to signal an error.
        }
    }

    public int getAddressIDFromUserID(Connection connection, int userID) throws SQLException {
        ResultSet rs = null;
        int addressID = -1; // start with an invalid value
    
        try {
            String sql = "SELECT addressID FROM user WHERE userID=?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, userID);
    
            rs = preparedStatement.executeQuery();
    
            // check if the result set has any data
            if (rs.next()) {
                addressID = rs.getInt("addressID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (rs != null) {
                rs.close();
            }
        }
    
        return addressID;
    }

    public void updateAddress(Connection connection, int userID, Address address) throws SQLException {
        int addressID = getAddressIDFromUserID(connection, userID);
    
        try {
            if (addressID != -1) {
                String sql = "UPDATE address SET houseNumber=?, street=?, city=?, postcode=? WHERE addressID=?";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
    
                preparedStatement.setInt(1, address.getHouseNumber()); 
                preparedStatement.setString(2, address.getStreet()); 
                preparedStatement.setString(3, address.getCity()); 
                preparedStatement.setString(4, address.getPostcode()); 
                preparedStatement.setInt(5, addressID);
    
                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                } else {
                }
            } else {
                // Handle the case where the addressID is invalid or not found

            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public void updateStockLevels(Connection connection, String productID, int quantity) throws SQLException {
        try {
            String updateSQL = "UPDATE stock SET stock=? WHERE product_code=?";
            PreparedStatement preparedStatement = connection.prepareStatement(updateSQL);

            preparedStatement.setInt(1, quantity);
            preparedStatement.setString(2, productID);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;// Re-throw the exception to signal an error.
        }
    }

    public boolean isEmailAlreadyRegistered(Connection connection, String email) throws SQLException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        boolean emailExists = false;
    
        try {
            // Prepare SQL statement to check for existing email
            String query = "SELECT * FROM user WHERE email = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, email);
            resultSet = preparedStatement.executeQuery();
    
            // If the result set has a row, the email is already registered
            if (resultSet.next()) {
                emailExists = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            // Close the ResultSet and PreparedStatement
            if (resultSet != null) {
                resultSet.close();
            }
            if (preparedStatement != null) {
                preparedStatement.close();
            }
        }
    
        return emailExists;
    }

    public List<GUI.Order> getAllOrders(Connection connection) throws SQLException {
            PreparedStatement stmt = null;
            ResultSet rs = null;
            List<GUI.Order> orderList = new ArrayList<>();
    
            try {
                String sql = "SELECT orderID, order_date, price_total, order_status, userID FROM `order`";
                stmt = connection.prepareStatement(sql);
                rs = stmt.executeQuery();
    
                while (rs.next()) {
                    int orderID = rs.getInt("orderID");
                    String order_date = rs.getString("order_date");
                    double price_total = rs.getDouble("price_total");
                    STATUS order_status = STATUS.valueOf(rs.getString("order_status"));
                    int userID = rs.getInt("userID");
    
                    
                    GUI.Order order = new GUI.Order(orderID, order_date, price_total, order_status, userID);
                    orderList.add(order);
                }
            } catch (SQLException e) {
                e.printStackTrace();
                throw e;
            } finally {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
            }
            return orderList;
    }

    public List<GUI.Order> getAllOrdersByUserID(Connection connection, int userID) throws SQLException {
            PreparedStatement stmt = null;
            ResultSet rs = null;
            List<GUI.Order> orderList = new ArrayList<>();
    
            try {
                String sql = "SELECT orderID, order_date, price_total, order_status, userID FROM `order` WHERE userID=?";
                stmt = connection.prepareStatement(sql);
                stmt.setInt(1, userID);
                rs = stmt.executeQuery();
    
                while (rs.next()) {
                    int orderID = rs.getInt("orderID");
                    String order_date = rs.getString("order_date");
                    double price_total = rs.getDouble("price_total");
                    STATUS order_status = STATUS.valueOf(rs.getString("order_status"));
    
                    
                    GUI.Order order = new GUI.Order(orderID, order_date, price_total, order_status, userID);
                    orderList.add(order);
                }
            } catch (SQLException e) {
                e.printStackTrace();
                throw e;
            } finally {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
            }
            return orderList;
    }

    public void deleteOrder(Connection connection, int orderID) throws SQLException {
        PreparedStatement deleteOrderLinesStatement = null;
        PreparedStatement deleteOrderStatement = null;
        PreparedStatement updateStockStatement = null;
        ResultSet resultSet = null;
    
        try {
            connection.setAutoCommit(false); // Start transaction
    
            // Retrieve the products and quantities from order lines to increment stock
            String retrieveOrderLinesSQL = "SELECT product_code, quantity FROM orderLine WHERE orderID=?";
            deleteOrderLinesStatement = connection.prepareStatement(retrieveOrderLinesSQL);
            deleteOrderLinesStatement.setInt(1, orderID);
            resultSet = deleteOrderLinesStatement.executeQuery();
    
            // Delete order lines
            String deleteOrderLinesSQL = "DELETE FROM orderLine WHERE orderID=?";
            deleteOrderLinesStatement = connection.prepareStatement(deleteOrderLinesSQL);
            deleteOrderLinesStatement.setInt(1, orderID);
            deleteOrderLinesStatement.executeUpdate();
    
            //delete the order
            String deleteOrderSQL = "DELETE FROM `order` WHERE orderID=?";
            deleteOrderStatement = connection.prepareStatement(deleteOrderSQL);
            deleteOrderStatement.setInt(1, orderID);
            int rowsAffected = deleteOrderStatement.executeUpdate();
    
            if (rowsAffected > 0) {        
                String updateStockSQL = "UPDATE stock SET stock = stock + ? WHERE product_code = ?";
                updateStockStatement = connection.prepareStatement(updateStockSQL);
                while (resultSet.next()) {
                    String productCode = resultSet.getString("product_code");
                    int quantity = resultSet.getInt("quantity");
    
                    updateStockStatement.setInt(1, quantity);
                    updateStockStatement.setString(2, productCode);
                    updateStockStatement.executeUpdate();
                }
    
                connection.commit(); // Commit transaction
            } else {

                connection.rollback(); // Rollback if no rows were affected
            }
        } catch (SQLException e) {
            connection.rollback(); // Rollback in case of exception
            e.printStackTrace();
            throw e;
        } finally {
            // Close resources and restore autocommit
            if (resultSet != null) resultSet.close();
            if (deleteOrderLinesStatement != null) deleteOrderLinesStatement.close();
            if (deleteOrderStatement != null) deleteOrderStatement.close();
            if (updateStockStatement != null) updateStockStatement.close();
            connection.setAutoCommit(true);
        }
    }
    
    
    
    

    public void updateOrderStatus(Connection connection, STATUS order_status, int orderID) throws SQLException {
            PreparedStatement preparedStatement = null;
        
            try {
                String sql = "UPDATE `order` SET order_status=? WHERE orderID=?";

                preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, String.valueOf(order_status));
                preparedStatement.setInt(2, orderID);
        
                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {

                } else {

                }
            } catch (SQLException e) {
                e.printStackTrace();
                throw e;
            } finally {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            }
        }


    public boolean hasBankDetails(Connection connection, int userID) throws SQLException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        boolean hasDetails = false;
        
        try {
            String sql = "SELECT * FROM bank_details WHERE userID = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, userID);
            resultSet = preparedStatement.executeQuery();
        
            // Check if theres at least one record for the given userID
            hasDetails = resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (resultSet != null) {
                resultSet.close();
            }
            if (preparedStatement != null) {
                preparedStatement.close();
            }
        }
        
        return hasDetails;
    }


    public String generateUniqueProductCode(Connection connection, String productType) throws SQLException {
        String productCode = null;
        int serialNumber = 1; // Initialise serial number to 1 by default


        // Query to retrieve the maximum number for a specific product type
        String maxSerialNumberQuery = "SELECT MAX(SUBSTRING(product_code, 2)) AS max_serial FROM product WHERE product_code LIKE ?";
        
        // Set the correct product code prefix based on the product type
        String productCodePrefix;
        switch (productType.toLowerCase()) {
            case "track":
                productCodePrefix = "R";
                break;
            case "controller":
                productCodePrefix = "C";
                break;
            case "locomotive":
                productCodePrefix = "L";
                break;
            case "rolling stock":
                productCodePrefix = "S";
                break;
            case "train set":
                productCodePrefix = "M";
                break;
            case "track pack":
                productCodePrefix = "P";
                break;
            default:
                return null; // Return null for unrecognised product types
        }

        try (PreparedStatement preparedStatement = connection.prepareStatement(maxSerialNumberQuery)) {
            preparedStatement.setString(1, productCodePrefix + "%");
            ResultSet resultSet = preparedStatement.executeQuery();
    
            if (resultSet.next()) {
                // Increment the serial number for the new product code
                serialNumber = resultSet.getInt("max_serial") + 1;
            }
        }
    
        // Generate the product code
        productCode = String.format("%s%05d", productCodePrefix, serialNumber);
    
        return productCode;
    }

    public void addNewProduct(Connection connection, String brandName, String productName, double price, String gauge, int quantity, String productType, String productEra, String productDCC) throws SQLException {
        try {
            // Generate a unique product code based on the provided parameters
            String productCode = generateUniqueProductCode(connection, productType);

            String productCodePrefix;
            switch (productType.toLowerCase()) {
                case "track":
                    productCodePrefix = "R"; 
                    break;
                case "controller":
                    productCodePrefix = "C";
                    break;
                case "locomotive":
                    productCodePrefix = "L"; 
                    break;
                case "rolling stock":
                    productCodePrefix = "S"; 
                    break;
                case "train set":
                    productCodePrefix = "M"; 
                    break;
                case "track pack":
                    productCodePrefix = "P"; 
                    break;
                default:
                    return; 
        }

            // Insert data into the product table
            String insertProductQuery = "INSERT INTO product (product_code, brand_name, product_name, price, gauge) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement productStatement = connection.prepareStatement(insertProductQuery);
            productStatement.setString(1, productCode);
            productStatement.setString(2, brandName);
            productStatement.setString(3, productName);
            productStatement.setDouble(4, price);
            productStatement.setString(5, gauge);
            productStatement.executeUpdate();

            // Insert data into the stock table
            String insertStockQuery = "INSERT INTO stock (product_code, stock) VALUES (?, ?)";
            PreparedStatement stockStatement = connection.prepareStatement(insertStockQuery);
            stockStatement.setString(1, productCode);
            stockStatement.setInt(2, quantity);
            stockStatement.executeUpdate();

            switch (productType.toLowerCase()) {
                case "track":
                    try {
                        String insertProductQueryTrack = "INSERT INTO track_piece (piece_name, product_code) VALUES (?, ?)";
                        PreparedStatement trackStatement = connection.prepareStatement(insertProductQueryTrack);
                        trackStatement.setString(1, productName);
                        trackStatement.setString(2, productCode);
                        trackStatement.executeUpdate();
                    }
                    catch (SQLException e) {
                        e.printStackTrace();
                        throw e;
                    }
                    break;
                case "controller":
                    try {
                        String insertProductQueryController = "INSERT INTO controller (controller_name, product_code) VALUES (?, ?)";
                        PreparedStatement controllerStatement = connection.prepareStatement(insertProductQueryController);
                        controllerStatement.setString(1, productDCC);
                        controllerStatement.setString(2, productCode);
                        controllerStatement.executeUpdate();
                    }
                    catch(SQLException e) {
                        e.printStackTrace();
                        throw e;
                    }
                    break;
                case "locomotive":
                    try {
                        String insertProductQueryLocomotive = "INSERT INTO locomotive (era, dcc, product_code) VALUES (?, ?, ?)";
                        PreparedStatement locomotiveStatement = connection.prepareStatement(insertProductQueryLocomotive);
                        locomotiveStatement.setString(1, productEra);
                        locomotiveStatement.setString(2, productDCC);
                        locomotiveStatement.setString(3, productCode);
                        locomotiveStatement.executeUpdate();

                    }
                    catch(SQLException e) {
                        e.printStackTrace();
                        throw e;
                    }
                    break;
                case "rolling stock":
                    try {
                        String insertProductQueryRollingStock = "INSERT INTO rolling_stock (era, product_code) VALUES (?, ?)";
                        PreparedStatement rollingStockStatement = connection.prepareStatement(insertProductQueryRollingStock);
                        rollingStockStatement.setString(1, productEra);
                        rollingStockStatement.setString(2, productCode);
                        rollingStockStatement.executeUpdate();
                    }
                    catch(SQLException e) {
                        e.printStackTrace();
                        throw e;
                    }
                    break;
                case "train set":
                    break;
                case "track pack":
                    break;
                default:
                    return; // Return null for unrecognised product types
        }

        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }


    public int getStockByProductCode(Connection connection, String productCode) throws SQLException {
        int stock = 0;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            // SQL query to retrieve stock based on product_code
            String query = "SELECT stock FROM stock WHERE product_code = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, productCode);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                // Retrieve stock value from the result set
                stock = resultSet.getInt("stock");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            // Close resources in the finally block
            if (resultSet != null) {
                resultSet.close();
            }
            if (preparedStatement != null) {
                preparedStatement.close();
            }
        }

        return stock;
    }

    public Address getAddressDetails(Connection connection, int addressID) throws SQLException {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Address address = null;
    
        try {
            String query = "SELECT * FROM address WHERE addressID=?";
            statement = connection.prepareStatement(query);
            statement.setInt(1, addressID);
            resultSet = statement.executeQuery();
    
            if (resultSet.next()) {
                // Retrieve address details from the result set
                int houseNumber = resultSet.getInt("houseNumber");
                String street = resultSet.getString("street");
                String city = resultSet.getString("city");
                String postcode = resultSet.getString("postcode");
    
                // Create an Address object
                address = new Address(addressID, houseNumber, street, city, postcode);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            // Close resources
            if (resultSet != null) {
                resultSet.close();
            }
            if (statement != null) {
                statement.close();
            }
        }
    
        return address;
    }


    
}
