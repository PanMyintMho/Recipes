package com.example.recipes.food.fragment.instruction

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import com.example.recipes.databinding.FragmentInstructionBinding
import com.example.recipes.food.models.Result
import com.example.recipes.food.util.Constants.Companion.RECIPES_RESULT_KEY

class InstructionFragment : Fragment() {

    private  var _bindindg: FragmentInstructionBinding?=null
    private val bindindg get() = _bindindg!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        // Inflate the layout for this fragment
        _bindindg = FragmentInstructionBinding.inflate(inflater, container, false)

        val args = arguments
        val myBundle: Result?= args?.getParcelable(RECIPES_RESULT_KEY)
        bindindg.insructionWebView.webViewClient = object : WebViewClient() {}
        val websiteUrl: String = myBundle!!.sourceUrl
        bindindg.insructionWebView.loadUrl(websiteUrl)
        return bindindg.root

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _bindindg =null
    }

}