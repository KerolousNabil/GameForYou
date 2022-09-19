package View

import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.my.MainActivity
import com.example.my.R
import com.google.firebase.auth.FirebaseAuth

class LoginScreen : AppCompatActivity() {
    var myauth = FirebaseAuth.getInstance()
    lateinit var sign_up : Button
    lateinit var login :Button
    lateinit var emailtext : TextView
    lateinit var  passwordtext : TextView
    lateinit var btn_forget_password :Button



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_screen)

        window.statusBarColor = this.resources.getColor(R.color.backgrounds)
        window.navigationBarColor = this.resources.getColor(android.R.color.transparent)

        val sp = getSharedPreferences("sp" , Context.MODE_PRIVATE)
        val Email = sp.getString("email", "")
        val Password = sp.getString("password", "")



        if (supportActionBar != null) {
            supportActionBar!!.hide()
        }

        sign_up = findViewById(R.id.signupbtn)
        login = findViewById(R.id.loginbtn)
        emailtext = findViewById(R.id.textEmail)
        passwordtext = findViewById(R.id.textPaassword)
        btn_forget_password = findViewById(R.id.edit)
        emailtext.text = Email
        passwordtext.text = Password


        btn_forget_password.setOnClickListener {


            val bulder = AlertDialog.Builder(this)
            bulder.setTitle("Forget password")
            val view = layoutInflater.inflate(R.layout.dialog_forgot_password,null)
            val username = view.findViewById<EditText>(R.id.et_username)
            bulder.setView(view)
            bulder.setPositiveButton("Reset", DialogInterface.OnClickListener { _, _ ->
                forgotPassword(username)
            })
            bulder.setNegativeButton("close", DialogInterface.OnClickListener { _, _ ->  })
            bulder.show()
        }










        login.setOnClickListener{

                view ->

            val email = emailtext.text.toString().trim()
            val password = passwordtext.text.toString().trim()
            if (email.isEmpty())
            {
                emailtext.error = "please enter your email"
                return@setOnClickListener
            }
            if (password.isEmpty())
            {
                passwordtext.error = "please enter your email"
                return@setOnClickListener
            }

            login(view , email , password )


        }


        sign_up.setOnClickListener {

            val email = emailtext.text.toString().trim()
            val password = passwordtext.text.toString().trim()

            if (email.isEmpty())
            {
                emailtext.error = "please enter your email"
                return@setOnClickListener
            }
            if (password.isEmpty())
            {
                passwordtext.error = "please enter your email"
                return@setOnClickListener
            }

            signup(email, password)

        }

    }
    override fun onBackPressed() {

        val builder = AlertDialog.Builder(this@LoginScreen)
        builder.setTitle("Warming")
        builder.setMessage("Are you sure you want to Exit?")
        builder.setPositiveButton("Yes") { dialog, id ->

            finish()

        }
        builder.setNegativeButton("No") { dialog, id ->

            dialog.dismiss()

        }
        val alert: AlertDialog = builder.create()
        alert.setCancelable(false)
        alert.show()
    }



    fun signup(email:String , password:String)
    {
        myauth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                task ->
            if (task.isSuccessful) Toast.makeText(this, " Done Successfully " , Toast.LENGTH_LONG).show()
            else Toast.makeText(this, " Sorry "+ task.exception?.message , Toast.LENGTH_LONG).show()
        }

    }



    fun login(view: View, email:String, password:String) {

        myauth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            var progressDialog = ProgressDialog(this@LoginScreen)
            progressDialog.setTitle("Login progress")
            progressDialog.setMessage("Please wait .......")
            progressDialog.show()
            if (task.isSuccessful) {
               Toast.makeText(this, "Loading Successfully", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)
                val bundle = Bundle()
                bundle.putString("email",email)
                intent.putExtras(bundle)
                startActivity(intent)
                val sp = getSharedPreferences("sp" , Context.MODE_PRIVATE)
                val editsp = sp.edit()
                editsp.putString("email",emailtext.text.toString() )
                editsp.putString("password",passwordtext.text.toString() )
                editsp.apply()





            } else{
                Toast.makeText(this, " Sorry " + task.exception?.message, Toast.LENGTH_LONG).show()
                progressDialog.dismiss()
            }
        }

    }


    fun forgotPassword(username : EditText){
        if (username.text.toString().isEmpty()) {
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(username.text.toString()).matches()) {
            return
        }


        myauth.sendPasswordResetEmail(username.text.toString())
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this,"Email sent successfully please check your Email.",Toast.LENGTH_LONG).show()
                }
                else {
                    Toast.makeText(this,"Sorry Email doesn't Exist",Toast.LENGTH_SHORT).show()
                }
            }

    }

    }