
package com.mkyong.http;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class Main{
    public static void main(String[] args) {
        JFrame frame = new JFrame("Login");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 1) );

        JTextField usernameField = new JTextField();
        JTextField passwordField = new JTextField();

        panel.add(new JLabel("Username"));
        panel.add(usernameField);
        panel.add(new JLabel("password"));
        panel.add(passwordField);


        JButton registerButton = new JButton("login");
        panel.add(registerButton);

        frame.add(panel);
        frame.setVisible(true);

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = passwordField.getText();

                try {
                    callApi(username, password);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    private static void callApi(String username,String password) throws Exception{
        Map<Object, Object> data = new HashMap<>();
        data.put("username",username);
        data.put("password",password);
        String bodyForm = data.entrySet().stream()
                .map(entry -> URLEncoder.encode((String) entry.getKey(), StandardCharsets.UTF_8) + "="
                        + URLEncoder.encode((String) entry.getValue(), StandardCharsets.UTF_8))
                .collect(Collectors.joining("&"));

        HttpRequest request = (HttpRequest) HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(bodyForm))
                .uri(URI.create("http://127.0.0.1:8000/api/token"))
                .setHeader("User-Agent", "Java 11 HttpClient Bot")
                .header("Content-Type", "application/x-www-form-urlencoded")
                .build();

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println("Code de réponse : " + response.statusCode());
        System.out.println("Corps de la réponse : " + response.body());    }


}