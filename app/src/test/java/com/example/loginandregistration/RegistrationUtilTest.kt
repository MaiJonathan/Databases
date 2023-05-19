package com.example.loginandregistration

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class RegistrationUtilTest {
    //methodName_someCondition_expectedResult
    @Test
    fun validatePassword_emptyPassword_isFalse(){
        val actual = RegistrationUtil.validatePassword("","")
        assertThat(actual).isFalse()
    }
    @Test
    fun validatePassword_passwordsDontMatch_isFalse(){
        val actual = RegistrationUtil.validatePassword("WhatRhymesWithLinus1","WhatRhymesWithLinus2")
        assertThat(actual).isFalse()
    }
    @Test
    fun validatePassword_passwordTooShort_isFalse(){
        val actual = RegistrationUtil.validatePassword("Pie1","Pie1")
        assertThat(actual).isFalse()
    }
    @Test
    fun validatePassword_passwordNoDigit_isFalse(){
        val actual = RegistrationUtil.validatePassword("UgandanKnuckles","UgandanKnuckles")
        assertThat(actual).isFalse()
    }
    @Test
    fun validatePassword_passwordNoCapital_isFalse(){
        val actual = RegistrationUtil.validatePassword("linuswongistooshort4me","linuswongistooshort4me")
        assertThat(actual).isFalse()

    }
    @Test
    fun validatePassword_workingPassword_isTrue(){
        val actual = RegistrationUtil.validatePassword("LinusBad20","LinusBad20")
        assertThat(actual).isTrue()

    }
    //Make tests for failures of
//    min length of 8 chars
//    at least one digit
//    at least one capital letter
//    good working passsword

    //username
    @Test
    fun validateUsername_sameUsername_isFalse(){
        val actual = RegistrationUtil.validateUsername("bob")
        assertThat(actual).isFalse()
    }
    @Test
    fun validateUsername_shortUsername_isfalse(){
        val actual = RegistrationUtil.validateUsername("su")
        assertThat(actual).isFalse()
    }
    @Test
    fun validateUsername_emptyUsername_isfalse(){
        val actual = RegistrationUtil.validateUsername("")
        assertThat(actual).isFalse()
    }
    @Test
    fun validateUsername_workingUsername_isTrue(){
        val actual = RegistrationUtil.validateUsername("SusOfTheLin")
        assertThat(actual).isTrue()
    }
    //email
    @Test
    fun validateEmail_sameEmail_isFalse(){
        val actual = RegistrationUtil.validateEmail("LinusWong@gmail.com")
        assertThat(actual).isFalse()
    }
    @Test
    fun validateEmail_wrongEmail1_isFalse(){
        val actual = RegistrationUtil.validateEmail("@gmail.com")
        assertThat(actual).isFalse()
    }
    @Test
    fun validateEmail_wrongEmail2_isFalse(){
        val actual = RegistrationUtil.validateEmail("LinusWong@.com")
        assertThat(actual).isFalse()
    }
    @Test
    fun validateEmail_wrongEmail3_isFalse(){
        val actual = RegistrationUtil.validateEmail("LinusWong@gmail.")
        assertThat(actual).isFalse()
    }
    @Test
    fun validateEmail_wrongEmail4_isFalse(){
        val actual = RegistrationUtil.validateEmail("lin@s.com@s")
        assertThat(actual).isFalse()
    }
    @Test
    fun validateEmail_correctEmail_isTrue(){
        val actual = RegistrationUtil.validateEmail("Tomatos@gmail.com")
        assertThat(actual).isTrue()
    }
}