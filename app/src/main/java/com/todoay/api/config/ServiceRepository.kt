package com.todoay.api.config

import com.todoay.api.domain.auth.AuthService
import com.todoay.api.domain.auth.email.EmailService
import com.todoay.api.domain.category.CategoryService
import com.todoay.api.domain.hashtag.HashtagService
import com.todoay.api.domain.profile.ProfileService
import com.todoay.api.domain.todo.common.TodoService
import com.todoay.api.domain.todo.daily.DailyTodoService
import com.todoay.api.domain.todo.dueDate.DueDateTodoService

/**
 * Retrofit Service를 사용하는 Service Interface 객체를 초기화하는 object 클래스.
 * type:
 *      AuthServiceRepository
 *      ProfileServiceRepository
 */
object ServiceRepository {

    /**
     * 토큰이 필요 없는 Retrofit Service 객체를 생성하고 초기화하는 메소드.
     * @param service : 생성할 Service 클래스
     * @return Retrofit Service 객체
     */
    fun <T> createServiceWithoutToken(service: Class<T>) : T {
        return RetrofitService.getServiceWithoutToken().create(service)
    }

    /**
     * 토큰이 필요한 Retrofit Service 객체를 생성하고 초기화하는 메솓.
     * @param service : 생성할 Service 클래스
     * @return Retrofit Service 객체
     */
    fun <T> createServiceWithToken(service: Class<T>) : T {
        return RetrofitService.getServiceWithToken().create(service)
    }

    /**
     * Auth Service를 사용하는 Service의 객체를 초기화하는 object 클래스.
     */
    object AuthServiceRepository {

        /**
         * 토큰이 필요없는 EmailService 객체를 생성하고 호출하는 메소드.
         * @return emailService
         */
        fun callEmailService() : EmailService {
            return createServiceWithoutToken(EmailService::class.java)
        }

        /**
         * 토큰이 필요없는 AuthService 객체를 생성하고 호출하는 메소드.
         * @return loginService
         */
        fun callAuthServiceWithoutToken() : AuthService {
            return createServiceWithoutToken(AuthService::class.java)
        }

        /**
         * 토큰이 필요한 AuthService 객체를 생성하고 호출하는 메소드.
         * @return loginService
         */
        fun callAuthServiceWithToken() : AuthService {
            return createServiceWithToken(AuthService::class.java)
        }

    }

    /**
     * Profile Service를 사용하는 Service의 객체를 초기화하는 object 클래스.
     * NEED TOKEN !!
     */
    object ProfileServiceRepository {

        /**
         * 토큰이 필요한 ProfileService 객체를 생성하고 호출하는 메소드.
         * @return profileService
         */
        fun callProfileService() : ProfileService {
            return createServiceWithToken(ProfileService::class.java)
        }
    }

    /**
     * 투두 Service를 사용하는 Service의 객체롤 초기화하는 object 클래스.
     * NEED TOKEN !!
     */
    object TodoServiceRepository {

        /**
         * 토큰이 필요한 TodoService 객체를 생성하고 호출하는 메소드.
         *
         * @return todoService
         */
        fun callTodoService() : TodoService {
            return createServiceWithToken(TodoService::class.java)
        }

        /**
         * 토큰이 필요한 DailyTodoService 객체를 생성하고 호출하는 메소드.
         * @return dailyTodoService
         */
        fun callDailyTodoService() : DailyTodoService {
            return createServiceWithToken(DailyTodoService::class.java)
        }

        /**
         * 토큰이 필요한 DueDateTodoService 객체를 생성하고 호출하는 메소드.
         * @return dueDateTodoService
         */
        fun callDueDateTodoService() : DueDateTodoService {
            return createServiceWithToken(DueDateTodoService::class.java)
        }
    }

    /**
     * Category Service를 사용하는 Service의 객체를 초기화하는 object 클래스.
     * NEED TOKEN !!
     */
    object CategoryServiceRepository {

        /**
         * 토큰이 필요한 CategoryService 객체를 생성하고 호출하는 메소드.
         */
        fun callCategoryService() : CategoryService {
            return createServiceWithToken(CategoryService::class.java)
        }
    }

    /**
     * Hashtag Service를 사용하는 Service의 객체를 초기화하는 object 클래스.
     * NEED TOKEN !!
     */
    object HashtagServiceRepository {

        /**
         * 토큰이 필요한 HashtagService 객체를 생성하고 호출하는 메소드.
         */
        fun callHashtagService() : HashtagService {
            return createServiceWithToken(HashtagService::class.java)
        }
    }

}