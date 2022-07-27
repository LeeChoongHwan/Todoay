package com.todoay.api.config

/**
 * RetrofitURL은 Retrofit의 baseURL을 설정하기 위해
 * URL에 설정되는 IP 주소를 저장하는 클래스로써,
 * ipAddress 변수에 서버의 IP 주소를 선언해야 함.
 *
 * ExampleRetrofitURL 클래스는 보안 상 개인 또는 클라우드의 IP 주소를 비공개하기 위해
 * RetrofitURL 클래스를 gitignore 설정하여 RetrofitURL 클래스의 포맷을 제공하기 위한 클래스이다.
 *
 * 사용법:
 *      1. 해당 클래스 파일을 동일한 경로(com.todoay.api.config)에 이름 중 'Example'을 제거하여 복사
 *      2. 복사된 파일('RetrofitURL.kt')의 ipAddress 변수에 서버의 IP 주소를 선언.
 */
object ExampleRetrofitURL {
    const val ipAddress = "서버의 IP 주소를 입력해주세요"
}