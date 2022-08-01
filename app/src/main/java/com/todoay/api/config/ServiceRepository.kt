package com.todoay.api.config

import com.todoay.api.domain.auth.email.EmailService
import com.todoay.api.domain.auth.login.LoginService
import com.todoay.api.domain.auth.nickname.NicknameService
import com.todoay.api.domain.auth.password.ModifyPasswordService
import com.todoay.api.domain.auth.refresh.RefreshService
import com.todoay.api.domain.auth.signUp.SignUpService
import com.todoay.api.domain.profile.ProfileService
import java.sql.Ref

/**
 * Retrofit Service를 사용하는 Service의 객체를 초기화하는 object 클래스.
 * type:
 *      AuthServiceRepository
 *      ProfileServiceRepository
 */
object ServiceRepository {

    /**
     * 각 Service에 해당하는 Retrofit Service를 생성하여 리턴하는 메소드.
     * @param service : 생성할 Service 클래스
     * @return Retrofit Service 객체
     */
    fun <T> createServiceWithoutToken(service: Class<T>) : T {
        return RetrofitService.getServiceWithoutToken().create(service)
    }

    fun <T> createServiceWithToken(service: Class<T>) : T {
        return RetrofitService.getServiceWithToken().create(service)
    }

    /**
     * Auth Service를 사용하는 Service의 객체를 초기화하는 object 클래스.
     */
    object AuthServiceRepository {
        /**
         * emailService는 EmailAPI를 위한 Retrofit Service 객체를 말한다.
         */
        fun callEmailService() : EmailService {
            return createServiceWithoutToken(EmailService::class.java)
        }

        /**
         * loginService는 LoginAPI를 위한 Retrofit Service 객체를 말한다.
         */
        fun callLoginService() : LoginService {
            return createServiceWithoutToken(LoginService::class.java)
        }

        /**
         * nicknameService는 NicknameAPI를 위한 Retrofit Service 객체를 말한다.
         */
        fun callNicknameService() : NicknameService {
            return createServiceWithoutToken(NicknameService::class.java)
        }

        /**
         * modifyPasswordService는 ModifyPasswordAPI를 위한 Retrofit Service 객체를 말한다.
         */
        fun callModifyPasswordService() : ModifyPasswordService {
            return createServiceWithToken(ModifyPasswordService::class.java)
        }

        /**
         * signUpService는 SignUpAPI를 위한 Retrofit Service 객체를 말한다.
         */
        fun callSignUpService() : SignUpService {
            return createServiceWithoutToken(SignUpService::class.java)
        }

        /**
         * refreshService RefreshAPI를 위한 Retrofit Service 객체를 말한다.
         */
        fun callRefreshService() : RefreshService {
            return RetrofitService.getServiceRefreshToken().create(RefreshService::class.java)
        }
    }

    /**
     * Profile Service를 사용하는 Service의 객체를 초기화하는 object 클래스.
     */
    object ProfileServiceRepository {

        /**
         * profileService는 ProfileAPI를 위한 Retrofit Service 객체를 말한다.
         */
        fun callProfileService() : ProfileService {
            return createServiceWithToken(ProfileService::class.java)
        }

    }

}