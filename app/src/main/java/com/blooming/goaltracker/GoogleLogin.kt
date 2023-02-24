package com.blooming.goaltracker

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore

@Suppress("DEPRECATION")
class GoogleLogin() : AppCompatActivity(){

    //firebase auth
    lateinit var firebaseAuth: FirebaseAuth
    var fireStore : FirebaseFirestore?=null
    //google client
    lateinit var googleSignInClient: GoogleSignInClient
    //private const val TAG = "GoogleActivity"
    var RC_SIGN_IN=9001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        firebaseAuth = FirebaseAuth.getInstance()
        fireStore= FirebaseFirestore.getInstance()

        val gso= GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("470402455172-qtfn7a7up7efsa1dn84ttbnu53nrp951.apps.googleusercontent.com")
            .requestEmail()
            .build()

        googleSignInClient= GoogleSignIn.getClient(this, gso)

        googleLogin()
    }

    public override fun onStart() {
        super.onStart()
        val account = GoogleSignIn.getLastSignedInAccount(this)
        if(account!==null){ // 이미 로그인 되어 있을시 바로 메인 화면으로 이동
            toMainActivity(firebaseAuth.currentUser)
        }
    }  //onStart end

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //구글 로그인
        if(requestCode==RC_SIGN_IN){
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                //Google Sign In was successful, authenticate with Firebase
                val account = task.getResult((ApiException::class.java))
                Log.d("LoginActivity", "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w("LoginActivity", "Google sign in failed", e)
            }
        }
    } // onActivityResult end

    // firebaseAuthWithGoogle
    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        Log.d("LoginActivity", "firebaseAuthWithGoogle:" + acct.id!!)

        //Google SignInAccount 객체에서 id 토큰 가져와서 firebase auth로 교환하고 firebase에 인증
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if(task.isSuccessful) {
                    Log.w("LoginActivity", "firebaseAuthWithGoogle 성공", task.exception)
                    //account db 저장
                    var accountEmails = ArrayList<String>()
                    fireStore?.collection("Account")?.addSnapshotListener{ querySanpshot, firebaseFirestoreException ->
                        for (snapshot in querySanpshot!!.documents) {
                            var item = snapshot.toObject(Account::class.java)!!
                            Log.d("email", item.email)
                            accountEmails.add(item.email)
                        }
                        Log.d("accoutEmails: ", accountEmails.toString())

                        var userAccount = Account()
                        var accountUId : String ?= ""
                        accountUId=firebaseAuth?.currentUser?.uid.toString()
                        userAccount.email= firebaseAuth?.currentUser?.email.toString()

                        if (accountEmails.contains(userAccount.email)){
                            Toast.makeText(this, "로그인 성공", Toast.LENGTH_SHORT).show()
                            toMainActivity(firebaseAuth.currentUser)
                        }
                        else {
                            fireStore?.collection("Account")?.document(accountUId)?.set(userAccount)
                            Toast.makeText(this, "계정 생성 완료", Toast.LENGTH_SHORT).show()
                            toProfileActivity(firebaseAuth.currentUser)
                        }
                    }
                } else{
                    Log.w("LoginActivity", "firebaseAuthWithGoogle 실패", task.exception)
                    Snackbar.make(findViewById<LinearLayout>(R.id.activity_login), "로그인에 실패하였습니다.", Snackbar.LENGTH_SHORT).show()
                }
            }
    } //firebaseAuthWithGoogle end

    // toProfileActivity
    fun toProfileActivity(user: FirebaseUser?){
        if(user !=null){
            startActivity(Intent(this, ProfileActivity::class.java))
            finish()
        }
    } // toProfileActivity end

    // toMainActivity
    fun toMainActivity(user: FirebaseUser?){
        if(user !=null){
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    // googleLogin
    private fun googleLogin(){
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    } //signIn end


}