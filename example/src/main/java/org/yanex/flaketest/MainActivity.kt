package org.yanex.flaketest

import android.content.res.Configuration
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import org.yanex.flake.FlakeContext
import org.yanex.flake.FlakeLayout
import org.yanex.flake.FlakeManager
import org.yanex.flaketest.flakes.ListFlake

abstract class FlakeActivity : AppCompatActivity() {
    private var internalFlakeContext: FlakeContext? = null

    protected val flakeContext: FlakeContext
        get() = internalFlakeContext!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val flakeContext = FlakeContext.create(this, savedInstanceState)

        flakeContext.messageListener = { messageReceived(it) }
        this.internalFlakeContext = flakeContext
    }

    open fun messageReceived(message: Any) {}

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        flakeContext.onConfigurationChanged(newConfig)
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        flakeContext.saveInstanceState(outState)
    }
}

class MainActivity : FlakeActivity() {

    private val topLevelFlakeManager by lazy {
        val flakeLayout = findViewById(R.id.flakeLayout) as FlakeLayout
        FlakeManager.create(flakeLayout, flakeContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val flakeManager = topLevelFlakeManager
        flakeManager.retainPreviousFlake = true

        if (!flakeManager.restoreState()) {
            flakeManager.show(ListFlake())
        }
    }

    override fun onBackPressed() {
        if (!topLevelFlakeManager.onBackPressed()) {
            super.onBackPressed()
        }
    }
}
