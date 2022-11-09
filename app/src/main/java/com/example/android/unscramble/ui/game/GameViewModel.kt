package com.example.android.unscramble.ui.game

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

    private var _currentScrambledWord: String = "test"
    val currentScrambledWord: String get() = _currentScrambledWord

}