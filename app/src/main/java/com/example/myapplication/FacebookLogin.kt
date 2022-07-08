package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_login.*


class FacebookLogin() : AppCompatActivity() {
    //firebase auth
    lateinit var firebaseAuth: FirebaseAuth
    // Facebook Sign-In Methods
    private var callbackManager: CallbackManager?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        firebaseAuth = FirebaseAuth.getInstance()
        callbackManager = CallbackManager.Factory.create()

        facebookLogin()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //페북 로그인
        callbackManager?.onActivityResult(requestCode, resultCode, data)
    } // onActivityResult end

    // 페이스북 로그인
    private fun facebookLogin(){
        LoginManager.getInstance()
            .logInWithReadPermissions(this, listOf("email", "public_profile"))

        LoginManager.getInstance()
            .registerCallback(callbackManager, object: FacebookCallback<LoginResult> {
                override fun onSuccess(result: LoginResult?) {
                    if (result?.accessToken!=null){
                        //facebook 계정 정보를 firebase 서버에 전달(로그인)
                        val accessToken=result.accessToken
                        firebaseAuthWithFacebook(result?.accessToken)
                    } else {
                        Log.d("Facebook", "Fail Facebook Login")
                        Snackbar.make(findViewById<LinearLayout>(R.id.activity_login), "로그인에 실패하였습니다.", Snackbar.LENGTH_SHORT).show()
                    }
                }

                override fun onCancel() { //취소된 경우
                    TODO("Not yet implemented")
                }

                override fun onError(error: FacebookException?) { //에러난 경우
                    TODO("Not yet implemented")
                }
            })
    }

    private fun firebaseAuthWithFacebook(accessToken: AccessToken?) {
        //AccessToken으로 Facebook 인증
        val credential = FacebookAuthProvider.getCredential(accessToken?.token!!)

        //성공 시 firebase에 유저 정보 보내기(로그인)
        firebaseAuth?.signInWithCredential(credential)
            ?.addOnCompleteListener{
                    task ->
                if(task.isSuccessful){
                    toProfileActivity(task.result?.user)
                } else{
                    Toast.makeText(this, task.exception?.message, Toast.LENGTH_LONG).show()
                }
            }

    }

    // toProfileActivity
    fun toProfileActivity(user: FirebaseUser?){
        if(user !=null){
            startActivity(Intent(this, ProfileActivity::class.java))
            finish()
        }
    } // toMainActivity end
}