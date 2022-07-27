package com.todoay.api.config

import com.todoay.api.domain.auth.email.EmailService
import com.todoay.api.domain.auth.login.LoginService
import com.todoay.api.domain.auth.nickname.NicknameService
import com.todoay.api.domain.auth.password.ModifyPasswordService
import com.todoay.api.domain.auth.signUp.SignUpService
import com.todoay.api.domain.profile.ProfileService

/**
 * Retrofit Service를 사용하는 Service의 객체를 초기화하는 object 클래스.
 * type:
 *      AuthServiceRepository
 *      ProfileServiceRepository
 */
object ServiceRepository {

    /**
     * Auth Service를 사용하는 Service의 객체를 초기화하는 object 클래스.
     */
    object AuthServiceRepository {
        /**
         * emailService는 EmailAPI를 위한 Retrofit Service 객체를 말한다.
         */
        val emailService: EmailService =
            RetrofitService.getService().create(EmailService::class.java)

        /**
         * loginService는 LoginAPI를 위한 Retrofit Service 객체를 말한다.
         */
        val loginService: LoginService =
            RetrofitService.getService().create(LoginService::class.java)

        /**
         * nicknameService는 NicknameAPI를 위한 Retrofit Service 객체를 말한다.
         */
        val nicknameService: NicknameService =
            RetrofitService.getService().create(NicknameService::class.java)

        /**
         * modifyPasswordService는 ModifyPasswordAPI를 위한 Retrofit Service 객체를 말한다.
         */
        val modifyPasswordService: ModifyPasswordService =
            RetrofitService.getService().create(ModifyPasswordService::class.java)

        /**
         * signUpService는 SignUpAPI를 위한 Retrofit Service 객체를 말한다.
         */
        val signUpService: SignUpService =
            RetrofitService.getService().create(SignUpService::class.java)
    }

    /**
     * Profile Service를 사용하는 Service의 객체를 초기화하는 object 클래스.
     */
    object ProfileServiceRepository {

        /**
         * profileService는 ProfileAPI를 위한 Retrofit Service 객체를 말한다.
         */
        val profileService: ProfileService =
            RetrofitService.getService().create(ProfileService::class.java)

    }

}