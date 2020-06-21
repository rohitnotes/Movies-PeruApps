package peruapps.movies.ui.splash

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import org.koin.android.ext.android.inject
import peruapps.movies.R
import peruapps.movies.ui.navigator.Navigator

class SplashActivity : AppCompatActivity() {

    private val viewModel: SplashViewModel by inject()

    private val navigator: Navigator by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        setViewModel()
    }

    private fun setViewModel() {
        viewModel.apply {
            isAuthLiveData.observe(::getLifecycle, ::observerIsAuth)
            checkUserAuth()
        }
    }

    private fun observerIsAuth(splashScreenState: SplashScreenState) {
        when (splashScreenState) {
            SplashScreenState.AuthRequired -> {
                navigator.goToAuth()
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            }
            SplashScreenState.AuthNotRequired -> {
                navigator.goToList()
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            }
        }
    }
}