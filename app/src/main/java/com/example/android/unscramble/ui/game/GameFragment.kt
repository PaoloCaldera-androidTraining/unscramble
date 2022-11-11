/*
 * Copyright (C) 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 */

package com.example.android.unscramble.ui.game

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.android.unscramble.R
import com.example.android.unscramble.databinding.GameFragmentBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

/**
 * Fragment where the game is played, contains the game logic.
 */
class GameFragment : Fragment() {

    /*  Kotlin property DELEGATION: the responsibility of instantiating and returning the
        referenced object is given to the viewModels() class.
        By doing this, the viewModel variable is automatically retained when the device or
        emulator experiences a configuration change
     */
    private val viewModel: GameViewModel by viewModels()

    // Binding object instance with access to the views in the game_fragment.xml layout
    private lateinit var binding: GameFragmentBinding

    companion object {
        private const val LOG_TAG = "GameFragment"
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout XML file and return a binding object instance
        binding = DataBindingUtil
            .inflate(inflater, R.layout.game_fragment, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // DATA BINDING variables, that are referenced directly in the UI
        binding.gameViewModel = viewModel
        binding.maxNoOfWords = MAX_NO_OF_WORDS
        binding.lifecycleOwner = viewLifecycleOwner

        Log.d(LOG_TAG, "UI WORD COUNT: ${viewModel.currentWordCount.value}")
        Log.d(LOG_TAG, "UI SCRAMBLED WORD: ${viewModel.currentScrambledWord.value}")

        // Setup a click listener for the Submit and Skip buttons.
        binding.submit.setOnClickListener { onSubmitWord() }
        binding.skip.setOnClickListener { onSkipWord() }

        /* LiveData observers: the update occurs only when the state is STARTED or RESUMED
        viewModel.currentWordCount.observe(viewLifecycleOwner) { newWordCount ->
            binding.wordCount.text = getString(R.string.word_count, newWordCount, MAX_NO_OF_WORDS)
        }
        viewModel.score.observe(viewLifecycleOwner) { newScore ->
            binding.score.text = getString(R.string.score, newScore)
        }
        viewModel.currentScrambledWord.observe(viewLifecycleOwner) { newWord ->
            binding.textViewUnscrambledWord.text = newWord
        }*/
    }

    // Submit button: try to verify if the input word is the same as the scrambled one
    private fun onSubmitWord() {
        if (!viewModel.evaluateInputWord(binding.textInputEditText.text.toString())) {
            setErrorTextField(true)
            return
        }

        if (viewModel.currentWordCount.value!! >= MAX_NO_OF_WORDS) {
            showFinalScoreDialog()
            return
        }

        setErrorTextField(false)
        viewModel.nextWord()
    }

    // Skip button: go to the next word without increasing the score
    private fun onSkipWord() {
        if (viewModel.currentWordCount.value!! >= MAX_NO_OF_WORDS) {
            showFinalScoreDialog()
            return
        }

        setErrorTextField(false)
        viewModel.nextWord()
    }


    // Creates and displays an alert dialog with the final score
    private fun showFinalScoreDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.congratulations))
            .setMessage(getString(R.string.you_scored, viewModel.score.value))
            .setNegativeButton(getString(R.string.exit)) { _, _ -> exitGame() }
            .setPositiveButton(getString(R.string.play_again)) { _, _ -> restartGame() }
            .setCancelable(false)
            .show()
    }

    // Restart the game by re-initializing the viewModel and then updating the UI
    private fun restartGame() {
        setErrorTextField(false)
        viewModel.reinitializeData()
    }

    // Exit the game by finishing the activity and deleting the viewModel instance
    private fun exitGame() {
        activity?.finish()
    }


    // Sets the text field error status in case of error
    // Clears the text input when it goes to the next word
    private fun setErrorTextField(error: Boolean) {
        if (error) {
            binding.textField.isErrorEnabled = true
            binding.textField.error = getString(R.string.try_again)
        } else {
            binding.textField.isErrorEnabled = false
            binding.textInputEditText.text = null
        }
    }

}
