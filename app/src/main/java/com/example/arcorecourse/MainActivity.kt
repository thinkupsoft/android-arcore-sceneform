package com.example.arcorecourse

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.ar.core.Anchor
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.ux.ArFragment
import com.google.ar.sceneform.ux.TransformableNode
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.CompletableFuture

class MainActivity : AppCompatActivity() {

    lateinit var arFragment: ArFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        arFragment = fragment as ArFragment
        arFragment.setOnTapArPlaneListener { hitResult, _, _ ->
            val modelRendedable = ModelRenderable.builder()
                .setSource(this, R.raw.chair)
                .build()
            CompletableFuture
                .allOf(modelRendedable)
                .thenAccept {
                    addNode(hitResult.createAnchor(), modelRendedable.get())
                }
        }
    }

    private fun addNode(anchor: Anchor, model: ModelRenderable) {
        val anchorNode = AnchorNode(anchor)
        val node = TransformableNode(arFragment.transformationSystem).apply {
            renderable = model
            setParent(anchorNode)
            arFragment.arSceneView.scene.addChild(anchorNode)
            select()
        }
    }
}
