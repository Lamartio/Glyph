if (typeof kotlin === 'undefined') {
  throw new Error("Error loading module 'js_test'. Its dependency 'kotlin' was not found. Please, check whether 'kotlin' is loaded prior to 'js_test'.");
}
var js_test = function (_, Kotlin) {
  'use strict';
  Kotlin.defineModule('js_test', _);
  return _;
}(typeof js_test === 'undefined' ? {} : js_test, kotlin);
