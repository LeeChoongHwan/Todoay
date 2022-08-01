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
    fun <T> createService(service: Class<T>) : T {
        return RetrofitService.getService().create(service)
    }

    /**
     * Auth Service를 사용하는 Service의 객체를 초기화하는 object 클래스.
     */
    object AuthServiceRepository {
        /**
         * emailService는 EmailAPI를 위한 Retrofit Service 객체를 말한다.
         */
        fun callEmailService() : EmailService {
            return createService(EmailService::class.java)
        }
        /**
         * loginService는 LoginAPI를 위한 Retrofit Service 객체를 말한다.
         */
        fun callLoginService() : LoginService {
            return createService(LoginService::class.java)
        }

        /**
         * nicknameService는 NicknameAPI를 위한 Retrofit Service 객체를 말한다.
         */
        fun callNicknameService() : NicknameService {
            return createService(NicknameService::class.java)
        }

        /**
         * modifyPasswordService는 ModifyPasswordAPI를 위한 Retrofit Service 객체를 말한다.
         */
        fun callModifyPasswordService() : ModifyPasswordService {
            return createService(ModifyPasswordService::class.java)
        }

        /**
         * signUpService는 SignUpAPI를 위한 Retrofit Service 객체를 말한다.
         */
        fun callSignUpService() : SignUpService {
            return createService(SignUpService::class.java)
        }

        /**
         * refreshService RefreshAPI를 위한 Retrofit Service 객체를 말한다.
         */
        fun callRefreshService() : RefreshService {
            return createService(RefreshService::class.java)
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
            return createService(ProfileService::class.java)
        }

    }

}