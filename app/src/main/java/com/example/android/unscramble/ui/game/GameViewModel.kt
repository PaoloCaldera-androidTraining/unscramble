package com.example.android.unscramble.ui.game

import android.util.Log
import androidx.lifecycle.ViewModel

/*  ViewModel is a particular class that is responsible for holding and processing data needed
    by the UI components (activities and fragments).
    ViewModel objects are automatically retained when the device or emulator experiences a
    configuration change, and therefore are unaffected by the UI components lifecycle.
 */

/*  ViewModel must contain only references to the data needed for the UI, and not references
    of the UI components (activities and fragments).
*/

class GameViewModel : ViewModel() {

    /*  BACKING PROPERTY
        Backing property consists of the creation of two variables for a single data, one mutable
        and one immutable.
        - mutable: private var. It can be accessed only inside the ViewModel class and can be
            modified since it is a var
        - immutable: public val. It can be accessed by any class, for example the UI components,
            but it cannot be modified
        This configuration protects the app data from unwanted and unsafe changes: the ViewModel
        class must be the only class where the data prepared for the UI has to be managed
     */

    private var _score: Int = 0
    val score: Int get() = _score

    private var _currentWordCount: Int = 0
    val currentWordCount: Int get() = _currentWordCount

    private lateinit var _currentScrambledWord: String
    val currentScrambledWord: String get() = _currentScrambledWord


    // Internal property of the ViewModel class
    private lateinit var currentWord: String
    private var wordsList: MutableList<String> = mutableListOf()


    init {
        nextWord()
    }

    companion object {
        private const val LOG_TAG = "GameViewModel"
    }

    // Increase the word count and go to the next word
    fun nextWord() {
        _currentWordCount++
        generateScrambledWord()
    }

    // Pick a new word and generate from it the corresponding scrambled word
    private fun generateScrambledWord() {

        // Generate the new current word, checking if it has already been used
        do {
            currentWord = allWordsList.random()
        } while (wordsList.contains(currentWord))

        // The currentWord that is going to be used is added in the list of already used words
        wordsList.add(currentWord)

        // Shuffle the current word until the shuffled one is different from the current word
        val tempScrambledWord = currentWord.toCharArray()
        do {
            tempScrambledWord.shuffle()
        } while (String(tempScrambledWord).equals(currentWord, false))

        // Assign the shuffled word to the current scrambled word to display
        _currentScrambledWord = String(tempScrambledWord)
    }

    // Evaluate if the input word is equal to the current word. If so, increase the score
    fun evaluateInputWord(input: String): Boolean {
        val result = currentWord.equals(input, false)
        if (result) { _score += SCORE_INCREASE }

        return result
    }

    // Re-initialize all the backing properties of the viewModel object
    fun reinitializeData() {
        _score = 0
        _currentWordCount = 0
        wordsList.clear()
        nextWord()
    }
}