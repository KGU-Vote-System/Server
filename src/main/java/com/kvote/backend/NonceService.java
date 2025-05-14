package com.kvote.backend;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class NonceService {
    private final Map<String, String> nonceStore = new ConcurrentHashMap<>();
    //concurrentHashMap 이란 ?
    /*
    여러 스레드가 동시에 put/get을 요청하면 데이터가 꼬일 수 있음
    하지만 -> concurrentHashMap은 내부적으로 락을 걸어줌 -> 운체 때 느낌 ㅇㅋ?
    멀티 스레드 환경에서도 안정성이 있음
    */

    public String generateNonce(String walletAddress) {
        String nonce = UUID.randomUUID().toString().substring(0, 8); // 0에서 8랜덤 문자열
        nonceStore.put(walletAddress, nonce);
        return nonce;
    }
/*
    uuid는 전역 고유 식별자 128비트 짜리 랜덤 문자열 -> 흐음 이거 무작위로 객체 생성해서
    문자열로 변환한다음에 8글자만 잘라서 사용하는거임-> 충돌가능성이 있지만
    오래 저장하는것이 아닌 1회성 로그인이면 충분함
* */
    public String getNonce(String walletAddress) {
        return nonceStore.get(walletAddress.toLowerCase());
    }
/* 왜 toLowerCase를 해주나 ? -> 이거는 지갑은 보통 대소문자를 구분하지 않음
  하지만 Map은 구분 -> A랑 a랑 당연하게도 다름 -> 그래서 전부 소문자로 통일해서 Map충돌 방지
  * */

    public void removeNonce(String walletAddress) {
        nonceStore.remove(walletAddress.toLowerCase());
    }
}
