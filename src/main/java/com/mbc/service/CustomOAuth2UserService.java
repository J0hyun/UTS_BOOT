//package com.mbc.service;
//
//import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
//import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
//import org.springframework.security.oauth2.core.user.OAuth2User;
//import org.springframework.stereotype.Service;
//
//@Service
//public class CustomOAuth2UserService extends DefaultOAuth2UserService {
//
//    @Override
//    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
//        OAuth2User user = super.loadUser(userRequest);
//
//        // 사용자 정보 커스터마이징
////        String username = user.getAttribute("name"); // 예: Google 로그인에서 'name' 속성 가져오기
////        System.out.println("OAuth2 User Name: " + username);
//        String email = user.getAttribute("email");
//        System.out.println("OAuth2 User Email: " + email);
//
//        return user;
//    }
//}
