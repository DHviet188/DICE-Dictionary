package com.btl;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Random;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.concurrent.Task;
import javafx.geometry.Pos;
import javafx.scene.layout.VBox;
import javax.mail.MessagingException;
import javafx.scene.web.WebView;
import javafx.scene.web.WebEngine;
import javafx.util.Duration;
import javafx.animation.PauseTransition;


public class loginController implements Initializable{
    private Stage welcomeStage;
    private static boolean welcomeScreenShown = false;

    public void setWelcomeScreenShown(boolean welcomeScreenShown) {
        this.welcomeScreenShown = welcomeScreenShown;
    }

    @FXML
    private Button close;

    @FXML
    private Button closeButton;

    @FXML
    private Label confirmPasswordLabel;

    @FXML
    private PasswordField confirm_password;

    @FXML
    private Button createAccountBtn;

    @FXML
    private Button createButton;

    @FXML
    private TextField create_email;

    @FXML
    private AnchorPane create_form;

    @FXML
    private PasswordField create_password;

    @FXML
    private TextField create_username;

    @FXML
    private Button forgotPassBtn;

    @FXML
    private Button loginBtn;

    @FXML
    private AnchorPane loginRight_form;
    
    @FXML 
    private AnchorPane forgotPassword_form;

    @FXML
    private PasswordField password;

    @FXML
    private TextField username;
    
    @FXML
    private Button forgotPassword_close;

    @FXML
    private Button forgotPassword_confirm;
    
    @FXML
    private TextField emailForgot_text;
    
    @FXML
    private AnchorPane verifyCode_form;
    
    @FXML
    private TextField verifyCode_text;
    
    @FXML
    private Button verifyCode_Btn;
    
    @FXML
    private Button backToForgotPw;
    
    @FXML
    private AnchorPane resetPassword_form;
    
    @FXML
    private AnchorPane verifyCreateAccount_form;
    
    @FXML
    private TextField verifyCreateCode_text;
    
    @FXML
    private PasswordField newPassword_text;
    
    @FXML
    private PasswordField confirmNewPassword_text;
    
    @FXML
    private Button resetPassword_Btn;
    
    @FXML 
    private Button verifyCreateCode_Btn;
    
    @FXML 
    private Button backToCreateAccount;
    
    //Database tools
    private Connection connect;
    private PreparedStatement prepare;
    private ResultSet result;
    
    private String verificationCode;
    private String verifyCreateAccount;
    
    private double x = 0;
    private double y = 0;
    
    public Stage getWelcomeStage() {
        return welcomeStage;
    }
    
    /**
     * control login feature.
     */
    public void loginController() throws IOException {
        Stage loadingStage = new Stage();
        showLoadingStriped(loadingStage);
        
        disableAllButtons();
        
        Task<Boolean> loginTask = new Task<Boolean>() {
            protected Boolean call() throws Exception {
                String sql = "SELECT * FROM account_data WHERE username = ? and password = ?";

                connect = Database.connectDb();

                prepare = connect.prepareStatement(sql);
                prepare.setString(1, username.getText());
                prepare.setString(2, password.getText());

                result = prepare.executeQuery();

                if(username.getText().isEmpty() || password.getText().isEmpty()){
                    showAlert(AlertType.ERROR, "Error Message","Please fill all blank fields");
                }else{ 
                    return result.next();
                }
                return false;
            }
        };    
                
        loginTask.setOnSucceeded(e -> {
            loadingStage.close();
            if (loginTask.getValue()) {
                try {
                    getAccountData.username = username.getText();

                    loginBtn.getScene().getWindow().hide();
                    Parent root = FXMLLoader.load(getClass().getResource("menu.fxml"));
                    Stage stage = new Stage();
                    Scene scene = new Scene(root);

                    root.setOnMousePressed((MouseEvent event) ->{
                        x = event.getSceneX();
                        y = event.getSceneY();
                    });

                    root.setOnMouseDragged((MouseEvent event) ->{
                        stage.setX(event.getScreenX() - x);
                        stage.setY(event.getScreenY() - y);
                    });

                    stage.initStyle(StageStyle.TRANSPARENT);
                    stage.setScene(scene);
                    stage.show();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                
            } else {
                showAlert(AlertType.ERROR, "Error Message","Wrong Username/Password");
            }
            enableAllButtons();
        });
        
        loginTask.setOnFailed(e -> {
            loadingStage.close();
            Throwable throwable = loginTask.getException();
            if (throwable instanceof IllegalArgumentException) {
                showAlert(AlertType.ERROR, "Error Message",throwable.getMessage());
            } else {
                showAlert(AlertType.ERROR, "Error Message","Please filled in all blanks");
            }
            enableAllButtons();
        });
        
        new Thread(loginTask).start();
    }
    
    public void creatButtonAction(ActionEvent event) {
        if (create_username.getText().isEmpty() || create_password.getText().isEmpty() || confirm_password.getText().isEmpty() || create_email.getText().isEmpty()) {
            showAlert(AlertType.ERROR, "Error Message","Please filled in all blanks!");
        } else {
            if (!create_password.getText().equals(confirm_password.getText())) {
                confirmPasswordLabel.setText("Password does not match");
                confirmPasswordLabel.setStyle("-fx-text-fill: red;");
                confirmPasswordLabel.setVisible(true);
                return;
            } else if(!create_email.getText().endsWith("@gmail.com")) {
                showAlert(AlertType.ERROR, "Error Message","Your email is invalid");
                return;
            } else {
                confirmPasswordLabel.setText("Password Match");
                confirmPasswordLabel.setVisible(true);
                confirmPasswordLabel.setStyle("-fx-text-fill: green;");
                
                Stage loadingStage = new Stage();
                showLoadingStriped(loadingStage);
                
                Task<Boolean> checkUserTask = new Task<Boolean>() {
                    @Override
                    protected Boolean call() throws Exception {
                        connect = Database.connectDb();
                        String checkUser = "SELECT * FROM account_data WHERE username = ? OR email = ?";
                        prepare = connect.prepareStatement(checkUser);
                        prepare.setString(1, create_username.getText());
                        prepare.setString(2, create_email.getText());
                        result = prepare.executeQuery();
                        return result.next();
                    }
                };

                checkUserTask.setOnSucceeded(e -> {
                    if (checkUserTask.getValue()) {
                        showAlert(AlertType.ERROR, "Error Message", "Username or Email already exists!");
                    } else {
                        showAlert(AlertType.INFORMATION, "Email Sent", "Verify Code was sent to your email!");
                        create_form.setVisible(false);
                        verifyCreateAccount_form.setVisible(true);
                        loadingStage.hide();
                        try {
                            sendVerificationCreate(create_email.getText());
                        } catch (MessagingException f) {
                            f.printStackTrace();
                        }
                    }
                });

                checkUserTask.setOnFailed(e -> {
                    Throwable throwable = checkUserTask.getException();
                    showAlert(AlertType.ERROR, "Error Message", throwable.getMessage());
                });

                new Thread(checkUserTask).start();
            }
        }
    }
    
    public void sendVerificationCreate(String email) throws MessagingException {
        this.verifyCreateAccount = generateVerificationCode();
        String subject = "Your Verification Code";
        String text = "Your code is: " + this.verifyCreateAccount;
        EmailSender.sendEmail(email, subject, text);
    }
    
    public void addUserToDB() {
        Stage loadingStage = new Stage();
        showLoadingStriped(loadingStage);
        
        disableAllButtons();
        
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                if (!verifyCreateAccount.equals(verifyCreateCode_text.getText())) {
                    Platform.runLater(() -> showAlert(AlertType.ERROR, "Verification Code Error", "Your verification code does not match"));
                    loadingStage.hide();
                } else {
                    connect = Database.connectDb();
                    String insertUser = "INSERT INTO account_data (username, password, email) VALUES (?, ?, ?)";
                    prepare = connect.prepareStatement(insertUser);
                    prepare.setString(1, create_username.getText());
                    prepare.setString(2, create_password.getText());
                    prepare.setString(3, create_email.getText());
                    prepare.executeUpdate();
                    Platform.runLater(() -> {
                        showAlert(AlertType.INFORMATION, "Account Created", "Your account has been successfully created.");
                        resetCreateAccountForm();
                        loadingStage.hide();
                        enableAllButtons();
                    });
                }
                return null;
            }
        };
        task.setOnFailed(e -> {
            Throwable throwable = task.getException();
            throwable.printStackTrace();
            Platform.runLater(() -> showAlert(AlertType.ERROR, "Database Error", "Error while creating account."));
            loadingStage.hide();
            enableAllButtons();
        });
        new Thread(task).start();
    }

    public void resetCreateAccountForm() {
        create_username.clear();
        create_password.clear();
        confirm_password.clear();
        create_email.clear();
        confirmPasswordLabel.setVisible(false);
        loginRight_form.setVisible(true);
        create_form.setVisible(false);
        verifyCreateAccount_form.setVisible(false);
    }
    
    public void forgotPassword_Btn(ActionEvent event) {
        String email = emailForgot_text.getText();
        if (email.isEmpty()) {
            showAlert(AlertType.ERROR, "Error Message", "Please fill in the email field!");
        } else {
            isEmailExists(email);
        }
    }

    public void isEmailExists(String email) {
        Stage loadingStage = new Stage();
        showLoadingStriped(loadingStage);
        
        disableAllButtons();
        
        Task<Boolean> task = new Task<Boolean>() {
            @Override
            protected Boolean call() throws Exception {
                connect = Database.connectDb();
                String checkEmail = "SELECT * FROM account_data WHERE email = ?";
                prepare = connect.prepareStatement(checkEmail);
                prepare.setString(1, email);
                result = prepare.executeQuery();
                return result.next();
            }
        };
        task.setOnSucceeded(e -> {
            boolean exists = task.getValue();
            boolean _visible = true;
            if (exists) {
                try {
                    sendVerificationEmail(email);
                } catch (MessagingException me) {
                    Platform.runLater(() -> showAlert(AlertType.ERROR, "Email Sending Failed", "Failed to send verification email."));
                    _visible = false;
                    loadingStage.hide();
                }
            } else {
                Platform.runLater(() -> showAlert(AlertType.ERROR, "Error", "Email does not exist!"));
                loadingStage.hide();
            }
            if (_visible){
                Platform.runLater(() -> showAlert(AlertType.INFORMATION, "Email Sent", "Verification email has been sent."));
                forgotPassword_form.setVisible(false);
                verifyCode_form.setVisible(true);
                loadingStage.hide();
            }
            enableAllButtons();
        });
        task.setOnFailed(e -> {
            Throwable throwable = task.getException();
            throwable.printStackTrace();
            Platform.runLater(() -> showAlert(AlertType.ERROR, "Database Error", "Error checking email existence."));
            enableAllButtons();
        });
        new Thread(task).start();
    }

    
    public String generateVerificationCode() {
        Random random = new Random();
        return String.format("%06d", random.nextInt(999999));
    }

    
    public void sendVerificationEmail(String email) throws MessagingException {
        this.verificationCode = generateVerificationCode();
        String subject = "Your Verification Code";
        String text = "Your code is: " + this.verificationCode;
        EmailSender.sendEmail(email, subject, text);
    }
    
    public void verifyCode_Btn_Action(ActionEvent event) {
        String inputCode = this.verifyCode_text.getText();
        if (inputCode.equals(this.verificationCode)) {
            verifyCode_form.setVisible(false);
            resetPassword_form.setVisible(true);
        } else {
            showAlert(AlertType.ERROR, "Error Message","The verification code is not valid.");
        }
    }
    
    public void resetPasswordBtn_Action(ActionEvent event) {
        String newPw = newPassword_text.getText();
        String cfNewPw = confirmNewPassword_text.getText();
        
        if (newPw.isEmpty() || cfNewPw.isEmpty()) {
            showAlert(AlertType.ERROR, "Error Message","Please filled in all blanks");
        } else if (!newPw.equals(cfNewPw)) {
            showAlert(AlertType.ERROR, "Error Message","Password does not match");
        } else {
            try {
                resetPasswordDB(emailForgot_text.getText(), newPw);
            } catch (Exception e) {
                showAlert(AlertType.ERROR, "Error Message", "Failed to reset password!");
            }
        }
    }
    
    public void resetPasswordDB(String email, String newPw) {
        Stage loadingStage = new Stage();
        showLoadingStriped(loadingStage);
        
        disableAllButtons();
        
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                connect = Database.connectDb();
                String sql = "UPDATE account_data SET password = ? WHERE email = ?";
                prepare = connect.prepareStatement(sql);
                prepare.setString(1, newPw);
                prepare.setString(2, email);
                prepare.executeUpdate();
                return null;
            }
        };
        
        task.setOnSucceeded(e -> Platform.runLater(() -> {
            loadingStage.hide();
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Successfully");
            alert.setHeaderText(null);
            alert.setContentText("Password successfully reset");

            alert.setOnHidden(evt -> {
                resetPassword_form.setVisible(false);
                loginRight_form.setVisible(true);
            });

            alert.showAndWait();
            enableAllButtons();
        }));
        task.setOnFailed(e -> {
            Throwable throwable = task.getException();
            throwable.printStackTrace();
            Platform.runLater(() -> showAlert(AlertType.ERROR, "Error Message", "Failed to reset password!"));
            loadingStage.hide();
            enableAllButtons();
        });
        new Thread(task).start();
    }
    
    public void showLoadingStriped(Stage loadingStage) {
        WebView webView = new WebView();
        WebEngine webEngine = webView.getEngine();
        String htmlContent = "<html>" +
                             "<head>" +
                             "<style>" +
                             "html, body {" +
                             "  margin: 0;" +
                             "  padding: 0;" +
                             "  width: 100%;" +
                             "  height: 100%;" +
                             "  display: flex;" +
                             "  justify-content: center;" +
                             "  align-items: center;" +
                             "}" +
                             ".progress {" +
                             "  width: 300px;" +
                             "  height: 30px;" +
                             "  background-color: #ddd;" +
                             "}" +
                             ".progress-striped .progress-bar {" +
                             "  width: 0%;" +
                             "  height: 100%;" +
                             "  background-color: #3c6e71;" +
                             "  background-image: linear-gradient(45deg, #353535 25%, transparent 25%, transparent 50%, #353535 50%, #353535 75%, transparent 75%, transparent);" +
                             "  animation: progressAnimationStrike 6s infinite;" +
                             "}" +
                             "@keyframes progressAnimationStrike {" +
                             "  0% { width: 0; }" +
                             "  100% { width: 100%; }" +
                             "}" +
                             "</style>" +
                             "</head>" +
                             "<body>" +
                             "  <div class='progress progress-striped'>" +
                             "    <div class='progress-bar'></div>" +
                             "  </div>" +
                             "</body>" +
                             "</html>";
        webEngine.loadContent(htmlContent);

        Scene loadingScene = new Scene(webView, 300, 30);
        loadingStage.setScene(loadingScene);
        loadingStage.setTitle("Please wait...");
        loadingStage.show();
    }


    public void loginCreateAccount() {
        loginRight_form.setVisible(false);
        create_form.setVisible(true);
        forgotPassword_form.setVisible(false);
    }
    
    public void forgotPasswordAccount() {
        loginRight_form.setVisible(false);
        create_form.setVisible(false);
        forgotPassword_form.setVisible(true);
    }
    
    public void createClose() {
        loginRight_form.setVisible(true);
        create_form.setVisible(false);
    }
    
    public void forgotPasswordClose() {
        loginRight_form.setVisible(true);
        forgotPassword_form.setVisible(false);
    }
    
    public void backtoForgotPw_form() {
        verifyCode_form.setVisible(false);
        forgotPassword_form.setVisible(true);
    }
    
    private void showAlert(AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    
    public void closeVerifyCreate_form() {
        create_form.setVisible(true);
        verifyCreateAccount_form.setVisible(false);
    }
    
    private void disableAllButtons() {
        loginBtn.setDisable(true);
        createAccountBtn.setDisable(true);
        forgotPassBtn.setDisable(true);
        backToCreateAccount.setDisable(true);
        backToForgotPw.setDisable(true);
        closeButton.setDisable(true);
        createButton.setDisable(true);
        forgotPassword_close.setDisable(true);
        forgotPassword_confirm.setDisable(true);
        resetPassword_Btn.setDisable(true);
        verifyCode_Btn.setDisable(true);
        verifyCreateCode_Btn.setDisable(true);
    }
    
    private void enableAllButtons() {
        loginBtn.setDisable(false);
        createAccountBtn.setDisable(false);
        forgotPassBtn.setDisable(false);
        backToCreateAccount.setDisable(false);
        backToForgotPw.setDisable(false);
        closeButton.setDisable(false);
        createButton.setDisable(false);
        forgotPassword_close.setDisable(false);
        forgotPassword_confirm.setDisable(false);
        resetPassword_Btn.setDisable(false);
        verifyCode_Btn.setDisable(false);
        verifyCreateCode_Btn.setDisable(false);
    }
 
    /**
     *  close program.
     */
    public void close() {
        System.exit(0);
    }

    /**
     * .
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if (!welcomeScreenShown) {
            showWelcomeScreen();
            setWelcomeScreenShown(true);
        }
        
        PauseTransition delay = new PauseTransition(Duration.seconds(5));
        delay.setOnFinished(event -> welcomeStage.close());
        delay.play();
        
        loginRight_form.setVisible(true);
        create_form.setVisible(false);
        confirmPasswordLabel.setVisible(false);
        verifyCreateAccount_form.setVisible(false);
        
        confirm_password.textProperty().addListener((observable,oldValue,newValue) -> {
            if(create_password.getText().equals(newValue)) {
                confirmPasswordLabel.setText("Password Match");
                confirmPasswordLabel.setStyle("-fx-text-fill: green;");
            } else {
                confirmPasswordLabel.setText("Password does not match");
                confirmPasswordLabel.setStyle("-fx-text-fill: red;");
            }
        });
    }
    
    public void showWelcomeScreen() {
        if (!welcomeScreenShown) {
            welcomeStage = new Stage();
            WebView web_view = new WebView();
            WebEngine webEngine = web_view.getEngine();

            String css = "/*\n" +
                    "\n" +
                    "Tutorial:\n" +
                    "https://www.roboleary.net/animation/2022/10/31/how-to-make-a-slick-animation-schitts-creek-title-sequence.html\n" +
                    "\n" +
                    "Part of Title Sequences collection:\n" +
                    "https://codepen.io/collection/nNmwgP\n" +
                    "\n" +
                    "Source code:\n" +
                    "https://github.com/robole/title-sequences\n" +
                    "\n" +
                    "*/\n" +
                    "\n" +
                    "@import url(\"https://fonts.googleapis.com/css2?family=Playfair+Display:wght@600\");\n" +
                    "\n" +
                    ":root {\n" +
                    "  --bar-scale-y: 0;\n" +
                    "  --sparkle-color: rgb(253 244 215 / 40%);\n" +
                    "}\n" +
                    "\n" +
                    "@keyframes pop-word {\n" +
                    "  to {\n" +
                    "    transform: rotateX(0);\n" +
                    "  }\n" +
                    "}\n" +
                    "\n" +
                    "@keyframes show {\n" +
                    "  to {\n" +
                    "    opacity: 1;\n" +
                    "  }\n" +
                    "}\n" +
                    "\n" +
                    "@keyframes bar-scale {\n" +
                    "  to {\n" +
                    "    transform: scaleY(1);\n" +
                    "  }\n" +
                    "}\n" +
                    "\n" +
                    "@keyframes sparkle {\n" +
                    "  0% {\n" +
                    "    transform: scale(0);\n" +
                    "  }\n" +
                    "\n" +
                    "  60% {\n" +
                    "    transform: scale(1) translate(4px, 1px) rotate(8deg);\n" +
                    "  }\n" +
                    "\n" +
                    "  100% {\n" +
                    "    transform: scale(0) translate(4px, 1px) rotate(8deg);\n" +
                    "  }\n" +
                    "}\n" +
                    "\n" +
                    "@keyframes shimmer {\n" +
                    "  to {\n" +
                    "    text-shadow: 0 0 8px red;\n" +
                    "  }\n" +
                    "}\n" +
                    "\n" +
                    "body {\n" +
                    "  display: grid;\n" +
                    "  width: 600px;\n " +
                    "  height: 400px;\n" +
                    "  margin: 0;\n" +
                    "\n" +
                    "  background-color: black;\n" +
                    "  place-items: center;\n" +
                    "}\n" +
                    "\n" +
                    "h1 {\n" +
                    "  color: white;\n" +
                    "  font-family: \"Playfair Display\", Vidaloka, serif;\n" +
                    "  font-size: 4rem;\n" +
                    "\n" +
                    "  line-height: 0.85;\n" +
                    "  perspective: 500px;\n" +
                    "  text-align: center;\n" +
                    "}\n" +
                    "\n" +
                    ".word {\n" +
                    "  display: block;\n" +
                    "\n" +
                    "  animation: show 0.01s forwards, pop-word 1.5s forwards;\n" +
                    "  animation-timing-function: cubic-bezier(0.14, 1.23, 0.33, 1.16);\n" +
                    "  opacity: 0;\n" +
                    "\n" +
                    "  transform: rotateX(120deg);\n" +
                    "  transform-origin: 50% 100%;\n" +
                    "}\n" +
                    "\n" +
                    ".word:nth-of-type(2) {\n" +
                    "  padding: 0 2rem;\n" +
                    "\n" +
                    "  animation-delay: 1.5s;\n" +
                    "\n" +
                    "  color: gold;\n" +
                    "}\n" +
                    "\n" +
                    ".superscript {\n" +
                    "  position: relative;\n" +
                    "  animation-delay: 3.6s;\n" +
                    "\n" +
                    "  animation-duration: 0.25s;\n" +
                    "  animation-name: shimmer;\n" +
                    "\n" +
                    "  vertical-align: text-top;\n" +
                    "}\n" +
                    "\n" +
                    "/* bars */\n" +
                    ".superscript::before {\n" +
                    "  --bar-width: 25%;\n" +
                    "\n" +
                    "  position: absolute;\n" +
                    "\n" +
                    "  top: 37%;\n" +
                    "  left: 47%;\n" +
                    "  width: 14%;\n" +
                    "  height: 48%;\n" +
                    "\n" +
                    "  animation: bar-scale 0.25s linear 3s 1 forwards;\n" +
                    "\n" +
                    "  background: linear-gradient(\n" +
                    "    to right,\n" +
                    "    white var(--bar-width),\n" +
                    "    transparent var(--bar-width) calc(100% - var(--bar-width)),\n" +
                    "    white calc(100% - var(--bar-width))\n" +
                    "  );\n" +
                    "\n" +
                    "  content: \"\";\n" +
                    "\n" +
                    "  transform: scaleY(var(--bar-scale-y));\n" +
                    "}\n" +
                    "\n" +
                    "/* sparkle */\n" +
                    ".superscript::after {\n" +
                    "  --size: 10rem;\n" +
                    "\n" +
                    "  position: absolute;\n" +
                    "\n" +
                    "  top: -5%;\n" +
                    "  left: -85%;\n" +
                    "\n" +
                    "  width: var(--size);\n" +
                    "  height: var(--size);\n" +
                    "\n" +
                    "  animation: sparkle 0.4s linear 3.5s 1 forwards;\n" +
                    "\n" +
                    "  background: radial-gradient(\n" +
                    "      circle at center,\n" +
                    "      rgb(252 249 241 / 94%) 0% 7%,\n" +
                    "      transparent 7% 100%\n" +
                    "    ),\n" +
                    "    conic-gradient(\n" +
                    "      transparent 0deg 18deg,\n" +
                    "      var(--sparkle-color) 18deg,\n" +
                    "      transparent 20deg 40deg,\n" +
                    "      var(--sparkle-color) 40deg,\n" +
                    "      transparent 43deg 87deg,\n" +
                    "      var(--sparkle-color) 87deg,\n" +
                    "      transparent 95deg 175deg,\n" +
                    "      var(--sparkle-color) 175deg,\n" +
                    "      transparent 178deg 220deg,\n" +
                    "      var(--sparkle-color) 220deg,\n" +
                    "      transparent 222deg 270deg,\n" +
                    "      var(--sparkle-color) 270deg,\n" +
                    "      transparent 275deg 300deg,\n" +
                    "      var(--sparkle-color) 300deg,\n" +
                    "      transparent 303deg 360deg\n" +
                    "    );\n" +
                    "\n" +
                    "  border-radius: 50%;\n" +
                    "  clip-path: polygon(\n" +
                    "    50% 0,\n" +
                    "    59.13% 26.64%,\n" +
                    "    85.13% -2.35%,\n" +
                    "    100% 50%,\n" +
                    "    50% 100%,\n" +
                    "    0 50%,\n" +
                    "    31.39% 34.86%\n" +
                    "  );\n" +
                    "\n" +
                    "  content: \"\";\n" +
                    "\n" +
                    "  filter: blur(1px);\n" +
                    "\n" +
                    "  transform: scale(0);\n" +
                    "}\n" +
                    "\n" +
                    "@media screen and (max-width: 600px) {\n" +
                    "  h1 {\n" +
                    "    font-size: 3rem;\n" +
                    "  }\n" +
                    "\n" +
                    "  /* sparkle */\n" +
                    "  .superscript::after {\n" +
                    "    --size: 6rem;\n" +
                    "  }\n" +
                    "}";

            String html = "<html><head><style>" + css + "</style></head>"
                + "<body><h1><span class=\"word\">Dice<span class=\"superscript\">s</span> </span>"
                + "<span class=\"word\">Dictionary</span></h1></body></html>";

            webEngine.loadContent(html);

            Scene scene = new Scene(new Group(web_view), 600, 400);
            welcomeStage.initStyle(StageStyle.UNDECORATED);
            welcomeStage.setScene(scene);
            welcomeStage.show();
            welcomeScreenShown = true;
        }
    }
}