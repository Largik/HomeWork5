package com.test.homework5

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.jakewharton.rxbinding4.widget.textChanges
import io.reactivex.rxjava3.core.Observable

class AuthFragment : Fragment() {
    companion object {
        private const val KEY_LOGIN = "login"
        private const val KEY_PASSWORD = "passwd"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        savedInstanceState?.let {
            view?.findViewById<EditText>(R.id.loginText)?.setText(it.getString(KEY_LOGIN))
            view?.findViewById<EditText>(R.id.passwordText)?.setText(it.getString(KEY_PASSWORD))
        }

        return inflater.inflate(R.layout.authorization, container, false)
    }

    @SuppressLint("ResourceAsColor")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val login = view.findViewById<EditText>(R.id.loginText)
        val passwd = view.findViewById<EditText>(R.id.passwordText)

        Observable.combineLatest(
            login.textChanges(),
            passwd.textChanges()
        ) { l, p ->
            l.length >= 6 && p.length >= 6
        }.subscribe( {
            if (it) {
                view.findViewById<Button>(R.id.btnLogin).setBackgroundColor(R.color.teal_700)
            }
            view.findViewById<Button>(R.id.btnLogin).isEnabled = it == true
        },{
            Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_SHORT).show()
        })

        view.findViewById<Button>(R.id.btnLogin).setOnClickListener {
            findNavController().navigate(AuthFragmentDirections.actionAuthFragmentToSearchFragment())
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        view?.findViewById<EditText>(R.id.loginText)?.text?.let {
            outState.putString(KEY_LOGIN, it.toString())
        }
        view?.findViewById<EditText>(R.id.passwordText)?.text?.let {
            outState.putString(KEY_PASSWORD, it.toString())
        }
    }

}