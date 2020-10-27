package jp.co.cosmos.demo.controller;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProviderClientBuilder;
import com.amazonaws.services.cognitoidp.model.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class HealthController {

    @RequestMapping("/health")
    @ResponseBody
    public ResponseEntity<String> health() {
        return new ResponseEntity<>("healthy", HttpStatus.OK);
    }

    @RequestMapping("/signup")
    @ResponseBody
    public ResponseEntity<String> signup() {
        AWSCognitoIdentityProvider awsCognitoIdentityProvider = AWSCognitoIdentityProviderClientBuilder.standard()
                .withCredentials(new DefaultAWSCredentialsProviderChain())
                .withRegion("ap-northeast-1")
                .build();

        SignUpRequest signUpRequest = new SignUpRequest();
        AttributeType email = new AttributeType();
        email.setName("email");
        email.setValue("cosmosdingli@gmail.com");

        signUpRequest
                .withClientId("dq1tq4kmgnmlaehri1sb97u22")
                .withUserAttributes(email)
                .withUsername("xiaodingzi")
                .withPassword("12345678");

        SignUpResult signUpResult = awsCognitoIdentityProvider.signUp(signUpRequest);

        return new ResponseEntity<>(signUpResult.getUserSub(), HttpStatus.OK);
    }

    @RequestMapping("/login")
    @ResponseBody
    public ResponseEntity<String> login() {
        AWSCognitoIdentityProvider awsCognitoIdentityProvider = AWSCognitoIdentityProviderClientBuilder.standard()
                .withCredentials(new DefaultAWSCredentialsProviderChain())
                .withRegion("ap-northeast-1")
                .build();

        AdminInitiateAuthRequest adminInitiateAuthRequest = new AdminInitiateAuthRequest();
        Map<String, String> authParameters = new HashMap<>();
        authParameters.put("USERNAME", "xiaodingzi");
        authParameters.put("PASSWORD", "12345678");

        adminInitiateAuthRequest.withAuthFlow(AuthFlowType.ADMIN_NO_SRP_AUTH)
                .withUserPoolId("ap-northeast-1_gTgOZx7nz")
                .withClientId("dq1tq4kmgnmlaehri1sb97u22")
                .withAuthParameters(authParameters);

        AdminInitiateAuthResult adminInitiateAuthResult = awsCognitoIdentityProvider.adminInitiateAuth(adminInitiateAuthRequest);

        return new ResponseEntity<>(adminInitiateAuthResult.getAuthenticationResult().getIdToken(), HttpStatus.OK);
    }
}