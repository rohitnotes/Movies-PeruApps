package peruapps.movies.ui.auth

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf
import peruapps.movies.R
import peruapps.movies.databinding.ActivityAuthBinding
import peruapps.movies.ui.dialog.MessageDialog
import peruapps.movies.ui.navigator.Navigator

class AuthActivity : AppCompatActivity() {

    private val viewModel: AuthViewModel by inject()
    private val binding: ActivityAuthBinding by inject {
        parametersOf(
            this,
            R.layout.activity_auth
        )
    }
    private val navigator: Navigator by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setBinding()
        setViewModel()
    }

    private fun setBinding() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
    }

    private fun setViewModel() {
        viewModel.apply {
            authScreenStateLiveData.observe(::getLifecycle, ::observerScreenState)
            authStateLiveData.observe(::getLifecycle, ::observerAuthState)
            showMessageErrorLiveData.observe(::getLifecycle, ::observerShowError)
        }
    }

    /** Observers **/

    private fun observerScreenState(authScreenState: AuthScreenState) {
        when (authScreenState) {
            AuthScreenState.Loading -> {
                binding.loadingView.visibility = View.VISIBLE
            }
            AuthScreenState.NotLoading -> {
                binding.loadingView.visibility = View.GONE
            }
            AuthScreenState.Success -> {
                navigator.goToList()
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                finish()
            }
        }
    }

    private fun observerAuthState(authState: AuthState) {
        when (authState) {
            AuthState.ErrorDisabled -> {
                binding.emailInputLayout.isErrorEnabled = false
                binding.passwordInputLayout.isErrorEnabled = false
            }
            AuthState.EmailInvalid -> {
                binding.emailInputLayout.isErrorEnabled = true
                binding.emailInputLayout.error = getString(R.string.auth_email_error_message)
            }
            AuthState.PasswordInvalid -> {
                binding.passwordInputLayout.isErrorEnabled = true
                binding.passwordInputLayout.error = getString(R.string.auth_password_error_message)
            }
        }
    }

    private fun observerShowError(message: String) {
        MessageDialog.Builder(this)
            .setMessage(message)
            .setTitle(getString(R.string.error))
            .setOnClickAccept { it.dismiss() }
            .show()
    }

    companion object {
        fun getCallingIntent(context: Context): Intent {
            return Intent(context, AuthActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
        }
    }
}