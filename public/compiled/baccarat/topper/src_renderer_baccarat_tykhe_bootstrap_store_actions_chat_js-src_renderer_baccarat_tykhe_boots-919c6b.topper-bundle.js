/*
 * ATTENTION: The "eval" devtool has been used (maybe by default in mode: "development").
 * This devtool is neither made for production nor for readable output files.
 * It uses "eval()" calls to create a separate source file in the browser devtools.
 * If you are trying to read the output file, select a different devtool (https://webpack.js.org/configuration/devtool/)
 * or disable the default devtool with "devtool: false".
 * If you are looking for production-ready output files, see mode: "production" (https://webpack.js.org/configuration/mode/).
 */
(self["webpackChunktykhe_electron_apps_baccarat"] = self["webpackChunktykhe_electron_apps_baccarat"] || []).push([["src_renderer_baccarat_tykhe_bootstrap_store_actions_chat_js-src_renderer_baccarat_tykhe_boots-919c6b"],{

/***/ "./src/renderer/baccarat/tykhe/bootstrap/store/actions/chat.js":
/*!*********************************************************************!*\
  !*** ./src/renderer/baccarat/tykhe/bootstrap/store/actions/chat.js ***!
  \*********************************************************************/
/***/ ((__unused_webpack_module, __webpack_exports__, __webpack_require__) => {

"use strict";
eval("__webpack_require__.r(__webpack_exports__);\n/* harmony export */ __webpack_require__.d(__webpack_exports__, {\n/* harmony export */   \"default\": () => (__WEBPACK_DEFAULT_EXPORT__)\n/* harmony export */ });\n/* harmony import */ var redux_actions__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! redux-actions */ \"./node_modules/redux-actions/es/createActions.js\");\n\n/* harmony default export */ const __WEBPACK_DEFAULT_EXPORT__ = ((0,redux_actions__WEBPACK_IMPORTED_MODULE_0__[\"default\"])({\n  MESSAGE: {\n    READ: null,\n    REQUEST: null,\n    SUCCESS: null,\n    FAILURE: null,\n    SET: null\n  },\n  ROOM_JOIN: {\n    REQUEST: null,\n    SUCCESS: null,\n    FAILURE: null,\n    SET: null\n  },\n  ROOM_LEAVE: {\n    REQUEST: null,\n    SUCCESS: null,\n    FAILURE: null,\n    SET: null\n  },\n  ROSTER_UPDATE: {\n    REQUEST: null,\n    SUCCESS: null,\n    FAILURE: null,\n    SET: null\n  },\n  MUTE: {\n    SET: null,\n    TOGGLE: null\n  }\n}));\n\n//# sourceURL=webpack://tykhe-electron-apps-baccarat/./src/renderer/baccarat/tykhe/bootstrap/store/actions/chat.js?");

/***/ }),

/***/ "./src/renderer/baccarat/tykhe/bootstrap/views/GameMenu/constants.js":
/*!***************************************************************************!*\
  !*** ./src/renderer/baccarat/tykhe/bootstrap/views/GameMenu/constants.js ***!
  \***************************************************************************/
/***/ ((__unused_webpack_module, __webpack_exports__, __webpack_require__) => {

"use strict";
eval("__webpack_require__.r(__webpack_exports__);\n/* harmony export */ __webpack_require__.d(__webpack_exports__, {\n/* harmony export */   actionNames: () => (/* binding */ actionNames),\n/* harmony export */   desktopLinks: () => (/* binding */ desktopLinks),\n/* harmony export */   linksMap: () => (/* binding */ linksMap),\n/* harmony export */   localeKeys: () => (/* binding */ localeKeys),\n/* harmony export */   mobileLinks: () => (/* binding */ mobileLinks)\n/* harmony export */ });\n/* harmony import */ var react__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! react */ \"./node_modules/react/index.js\");\n/* harmony import */ var react__WEBPACK_IMPORTED_MODULE_0___default = /*#__PURE__*/__webpack_require__.n(react__WEBPACK_IMPORTED_MODULE_0__);\n/* harmony import */ var _assets_icons_home_svg__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! ../../assets/icons/home.svg */ \"./src/renderer/baccarat/tykhe/bootstrap/assets/icons/home.svg\");\n/* harmony import */ var _assets_icons_home_svg__WEBPACK_IMPORTED_MODULE_1___default = /*#__PURE__*/__webpack_require__.n(_assets_icons_home_svg__WEBPACK_IMPORTED_MODULE_1__);\n/* harmony import */ var _assets_icons_chat_mobile_svg__WEBPACK_IMPORTED_MODULE_2__ = __webpack_require__(/*! ../../assets/icons/chat-mobile.svg */ \"./src/renderer/baccarat/tykhe/bootstrap/assets/icons/chat-mobile.svg\");\n/* harmony import */ var _assets_icons_chat_mobile_svg__WEBPACK_IMPORTED_MODULE_2___default = /*#__PURE__*/__webpack_require__.n(_assets_icons_chat_mobile_svg__WEBPACK_IMPORTED_MODULE_2__);\n/* harmony import */ var _assets_icons_cashier_svg__WEBPACK_IMPORTED_MODULE_3__ = __webpack_require__(/*! ../../assets/icons/cashier.svg */ \"./src/renderer/baccarat/tykhe/bootstrap/assets/icons/cashier.svg\");\n/* harmony import */ var _assets_icons_cashier_svg__WEBPACK_IMPORTED_MODULE_3___default = /*#__PURE__*/__webpack_require__.n(_assets_icons_cashier_svg__WEBPACK_IMPORTED_MODULE_3__);\n/* harmony import */ var _assets_icons_tips_svg__WEBPACK_IMPORTED_MODULE_4__ = __webpack_require__(/*! ../../assets/icons/tips.svg */ \"./src/renderer/baccarat/tykhe/bootstrap/assets/icons/tips.svg\");\n/* harmony import */ var _assets_icons_tips_svg__WEBPACK_IMPORTED_MODULE_4___default = /*#__PURE__*/__webpack_require__.n(_assets_icons_tips_svg__WEBPACK_IMPORTED_MODULE_4__);\n/* harmony import */ var _assets_icons_payouts_svg__WEBPACK_IMPORTED_MODULE_5__ = __webpack_require__(/*! ../../assets/icons/payouts.svg */ \"./src/renderer/baccarat/tykhe/bootstrap/assets/icons/payouts.svg\");\n/* harmony import */ var _assets_icons_payouts_svg__WEBPACK_IMPORTED_MODULE_5___default = /*#__PURE__*/__webpack_require__.n(_assets_icons_payouts_svg__WEBPACK_IMPORTED_MODULE_5__);\n/* harmony import */ var _assets_icons_history_svg__WEBPACK_IMPORTED_MODULE_6__ = __webpack_require__(/*! ../../assets/icons/history.svg */ \"./src/renderer/baccarat/tykhe/bootstrap/assets/icons/history.svg\");\n/* harmony import */ var _assets_icons_history_svg__WEBPACK_IMPORTED_MODULE_6___default = /*#__PURE__*/__webpack_require__.n(_assets_icons_history_svg__WEBPACK_IMPORTED_MODULE_6__);\n/* harmony import */ var _assets_icons_settings_svg__WEBPACK_IMPORTED_MODULE_7__ = __webpack_require__(/*! ../../assets/icons/settings.svg */ \"./src/renderer/baccarat/tykhe/bootstrap/assets/icons/settings.svg\");\n/* harmony import */ var _assets_icons_settings_svg__WEBPACK_IMPORTED_MODULE_7___default = /*#__PURE__*/__webpack_require__.n(_assets_icons_settings_svg__WEBPACK_IMPORTED_MODULE_7__);\n/* harmony import */ var _assets_icons_info_svg__WEBPACK_IMPORTED_MODULE_8__ = __webpack_require__(/*! ../../assets/icons/info.svg */ \"./src/renderer/baccarat/tykhe/bootstrap/assets/icons/info.svg\");\n/* harmony import */ var _assets_icons_info_svg__WEBPACK_IMPORTED_MODULE_8___default = /*#__PURE__*/__webpack_require__.n(_assets_icons_info_svg__WEBPACK_IMPORTED_MODULE_8__);\n/* harmony import */ var _assets_icons_support_svg__WEBPACK_IMPORTED_MODULE_9__ = __webpack_require__(/*! ../../assets/icons/support.svg */ \"./src/renderer/baccarat/tykhe/bootstrap/assets/icons/support.svg\");\n/* harmony import */ var _assets_icons_support_svg__WEBPACK_IMPORTED_MODULE_9___default = /*#__PURE__*/__webpack_require__.n(_assets_icons_support_svg__WEBPACK_IMPORTED_MODULE_9__);\n/* harmony import */ var _assets_icons_statistics_svg__WEBPACK_IMPORTED_MODULE_10__ = __webpack_require__(/*! ../../assets/icons/statistics.svg */ \"./src/renderer/baccarat/tykhe/bootstrap/assets/icons/statistics.svg\");\n/* harmony import */ var _assets_icons_statistics_svg__WEBPACK_IMPORTED_MODULE_10___default = /*#__PURE__*/__webpack_require__.n(_assets_icons_statistics_svg__WEBPACK_IMPORTED_MODULE_10__);\n/* harmony import */ var _assets_icons_language_svg__WEBPACK_IMPORTED_MODULE_11__ = __webpack_require__(/*! ../../assets/icons/language.svg */ \"./src/renderer/baccarat/tykhe/bootstrap/assets/icons/language.svg\");\n/* harmony import */ var _assets_icons_language_svg__WEBPACK_IMPORTED_MODULE_11___default = /*#__PURE__*/__webpack_require__.n(_assets_icons_language_svg__WEBPACK_IMPORTED_MODULE_11__);\n/* harmony import */ var _assets_icons_circle_svg__WEBPACK_IMPORTED_MODULE_12__ = __webpack_require__(/*! ../../assets/icons/circle.svg */ \"./src/renderer/baccarat/tykhe/bootstrap/assets/icons/circle.svg\");\n/* harmony import */ var _assets_icons_circle_svg__WEBPACK_IMPORTED_MODULE_12___default = /*#__PURE__*/__webpack_require__.n(_assets_icons_circle_svg__WEBPACK_IMPORTED_MODULE_12__);\n\n\n\n\n\n\n\n\n\n\n\n\n\nconst LanguageSelector = /*#__PURE__*/(0,react__WEBPACK_IMPORTED_MODULE_0__.lazy)(() => __webpack_require__.e(/*! import() */ \"src_renderer_baccarat_tykhe_bootstrap_components_LanguageSelector_index_js\").then(__webpack_require__.bind(__webpack_require__, /*! ../../components/LanguageSelector */ \"./src/renderer/baccarat/tykhe/bootstrap/components/LanguageSelector/index.js\")));\nconst linksMap = {\n  home: {\n    key: \"home\",\n    \"data-e2e\": \"menu_link_home\",\n    icon: (_assets_icons_home_svg__WEBPACK_IMPORTED_MODULE_1___default()),\n    onClick: () => {}\n  },\n  chat: {\n    key: \"chat\",\n    \"data-e2e\": \"menu_link_chat\",\n    icon: {\n      svg: (_assets_icons_chat_mobile_svg__WEBPACK_IMPORTED_MODULE_2___default())\n    },\n    drawer: true\n  },\n  cashier: {\n    key: \"cashier\",\n    \"data-e2e\": \"menu_link_cashier\",\n    icon: {\n      svg: (_assets_icons_cashier_svg__WEBPACK_IMPORTED_MODULE_3___default())\n    },\n    onClick: () => {}\n  },\n  tips: {\n    key: \"tips\",\n    \"data-e2e\": \"menu_link_tips\",\n    icon: (_assets_icons_tips_svg__WEBPACK_IMPORTED_MODULE_4___default()),\n    onClick: () => {}\n  },\n  stats: {\n    key: \"stats\",\n    \"data-e2e\": \"menu_link_stats\",\n    icon: (_assets_icons_statistics_svg__WEBPACK_IMPORTED_MODULE_10___default()),\n    onClick: () => {},\n    drawer: true\n  },\n  payouts: {\n    key: \"payouts\",\n    \"data-e2e\": \"menu_link_payouts\",\n    icon: (_assets_icons_payouts_svg__WEBPACK_IMPORTED_MODULE_5___default()),\n    onClick: () => {},\n    drawer: true\n  },\n  playerHistory: {\n    key: \"playerHistory\",\n    \"data-e2e\": \"menu_link_player_history\",\n    icon: (_assets_icons_history_svg__WEBPACK_IMPORTED_MODULE_6___default()),\n    onClick: () => {},\n    drawer: true\n  },\n  settings: {\n    key: \"settings\",\n    \"data-e2e\": \"menu_link_settings\",\n    icon: (_assets_icons_settings_svg__WEBPACK_IMPORTED_MODULE_7___default()),\n    onClick: () => {},\n    drawer: true\n  },\n  gameInfo: {\n    key: \"gameInfo\",\n    \"data-e2e\": \"menu_link_game_info\",\n    icon: (_assets_icons_info_svg__WEBPACK_IMPORTED_MODULE_8___default()),\n    onClick: () => {},\n    drawer: true\n  },\n  liveSupport: {\n    key: \"liveSupport\",\n    \"data-e2e\": \"menu_link_live_support\",\n    icon: (_assets_icons_support_svg__WEBPACK_IMPORTED_MODULE_9___default()),\n    onClick: () => {},\n    drawer: true\n  },\n  languages: {\n    key: \"languages\",\n    icon: (_assets_icons_language_svg__WEBPACK_IMPORTED_MODULE_11___default()),\n    onClick: () => {},\n    drawer: false,\n    render: /*#__PURE__*/react__WEBPACK_IMPORTED_MODULE_0___default().createElement(react__WEBPACK_IMPORTED_MODULE_0__.Suspense, {\n      fallback: null,\n      key: \"languageSelectorWrapper\"\n    }, /*#__PURE__*/react__WEBPACK_IMPORTED_MODULE_0___default().createElement(LanguageSelector, {\n      key: \"languageSelector\",\n      \"data-e2e\": \"menu_link_languages\"\n    }))\n  },\n  responsibleGaming: {\n    key: \"responsibleGaming\",\n    \"data-e2e\": \"menu_link_responsible_gaming\",\n    icon: (_assets_icons_circle_svg__WEBPACK_IMPORTED_MODULE_12___default()),\n    onClick: () => {},\n    drawer: false\n  }\n};\nconst localeKeys = {\n  welcome: \"general.welcome\",\n  home: \"general.home\",\n  chat: \"games_ui.chat\",\n  cashier: \"general.cashier\",\n  stats: \"general.statistics\",\n  tips: \"games_ui.tips\",\n  payouts: \"games_ui.limits_and_payouts\",\n  playerHistory: \"general.player_history\",\n  settings: \"games_ui.settings\",\n  gameInfo: \"general.game_info\",\n  liveSupport: \"games_ui.live_support\",\n  responsibleGaming: \"games_ui.responsible_gaming\"\n};\nconst actionNames = {\n  cashier: \"cashierRequest\",\n  home: \"homeRequest\",\n  tips: \"tipsRequest\",\n  responsibleGaming: \"responsibleGamingRequest\",\n  liveSupport: \"readSupportMessages\",\n  chat: \"readRoomMessages\"\n};\nconst desktopLinks = [\"cashier\", \"playerHistory\", \"settings\", \"gameInfo\", \"languages\"]; // eslint-disable-next-line\n\nconst mobileLinks = [\"home\", \"cashier\", \"chat\", \"tips\", \"stats\", \"payouts\", \"playerHistory\", \"settings\", \"gameInfo\", \"liveSupport\", \"languages\", \"responsibleGaming\"];\n\n//# sourceURL=webpack://tykhe-electron-apps-baccarat/./src/renderer/baccarat/tykhe/bootstrap/views/GameMenu/constants.js?");

/***/ }),

/***/ "./src/renderer/baccarat/tykhe/bootstrap/assets/icons/cashier.svg":
/*!************************************************************************!*\
  !*** ./src/renderer/baccarat/tykhe/bootstrap/assets/icons/cashier.svg ***!
  \************************************************************************/
/***/ ((module) => {

eval("module.exports = \"<svg xmlns=\\\"http://www.w3.org/2000/svg\\\" viewBox=\\\"0 0 24 24\\\"><path fill=\\\"#fff\\\" d=\\\"M19.9 16.8c0-2.3-.8-4.6-2.4-6.3v-1c0-.4-.3-.7-.7-.7-.3 0-.5.1-.6.4-.3-.2-.7-.5-1-.7v-.7c0-.4-.3-.7-.7-.7-.4 0-.7.3-.7.6-1.1-.4-2.2-.6-3.3-.6V4.3c0-.4-.3-.7-.7-.7H4.9c-.4 0-.7.3-.7.7v15.4c0 .4.3.7.7.7h14.2c.4 0 .7-.3.7-.7 0-.3-.1-.5-.3-.6.3-.7.4-1.5.4-2.3zM18.1 19H5.6V5h3.3v4c0 .4.3.7.7.7.4 0 .7-.3.7-.7v-.4c2.2 0 4.2.9 5.8 2.5l.2.2c1.3 1.5 2 3.4 2 5.4.1.9 0 1.6-.2 2.3z\\\"></path><path fill=\\\"#fff\\\" d=\\\"M7.2 5.9c-.4 0-.7.3-.7.7v1.2c0 .4.3.7.7.7s.8-.2.8-.7V6.7c0-.5-.3-.8-.8-.8z\\\"></path></svg>\"\n\n//# sourceURL=webpack://tykhe-electron-apps-baccarat/./src/renderer/baccarat/tykhe/bootstrap/assets/icons/cashier.svg?");

/***/ }),

/***/ "./src/renderer/baccarat/tykhe/bootstrap/assets/icons/chat-mobile.svg":
/*!****************************************************************************!*\
  !*** ./src/renderer/baccarat/tykhe/bootstrap/assets/icons/chat-mobile.svg ***!
  \****************************************************************************/
/***/ ((module) => {

eval("module.exports = \"<svg xmlns=\\\"http://www.w3.org/2000/svg\\\" viewBox=\\\"0 0 24 24\\\"><path fill=\\\"#fff\\\" d=\\\"M17.9 5H6.1C4.6 5 3.4 6.2 3.3 7.7v8.5c0 1.5 1.2 2.8 2.8 2.8h7.7l3.6 2.5c.2.1.4.1.5-.1 0-.1.1-.1.1-.2V19c1.5-.1 2.6-1.3 2.6-2.7V7.8c.1-1.5-1.2-2.7-2.7-2.8zm1.3 11.3c0 .7-.6 1.2-1.2 1.2H6.1c-.7 0-1.2-.6-1.2-1.2V7.8c0-.7.6-1.2 1.2-1.2h11.8c.7 0 1.2.6 1.2 1.2v8.5z\\\"></path><path fill=\\\"#fff\\\" d=\\\"M12.6 9.9c0-.4-.3-.7-.8-.8H6.4c-.4.1-.7.4-.7.8s.3.8.8.8H12c.3 0 .6-.4.6-.8zM17.5 13.5H12c-.4 0-.8.3-.8.8s.4.7.8.7h5.5c.4 0 .8-.3.8-.8s-.4-.7-.8-.7z\\\"></path></svg>\"\n\n//# sourceURL=webpack://tykhe-electron-apps-baccarat/./src/renderer/baccarat/tykhe/bootstrap/assets/icons/chat-mobile.svg?");

/***/ }),

/***/ "./src/renderer/baccarat/tykhe/bootstrap/assets/icons/circle.svg":
/*!***********************************************************************!*\
  !*** ./src/renderer/baccarat/tykhe/bootstrap/assets/icons/circle.svg ***!
  \***********************************************************************/
/***/ ((module) => {

eval("module.exports = \"<svg xmlns=\\\"http://www.w3.org/2000/svg\\\" viewBox=\\\"0 0 24 24\\\"><path fill=\\\"#fff\\\" d=\\\"M11.75 3a8.77 8.77 0 018.75 8.75A8.75 8.75 0 1111.75 3zm0 1.75a7 7 0 107 7 7.016 7.016 0 00-7-7z\\\"></path></svg>\"\n\n//# sourceURL=webpack://tykhe-electron-apps-baccarat/./src/renderer/baccarat/tykhe/bootstrap/assets/icons/circle.svg?");

/***/ }),

/***/ "./src/renderer/baccarat/tykhe/bootstrap/assets/icons/history.svg":
/*!************************************************************************!*\
  !*** ./src/renderer/baccarat/tykhe/bootstrap/assets/icons/history.svg ***!
  \************************************************************************/
/***/ ((module) => {

eval("module.exports = \"<svg xmlns=\\\"http://www.w3.org/2000/svg\\\" viewBox=\\\"0 0 24 24\\\"><path fill=\\\"#fff\\\" d=\\\"M11.64 3.25a8.72 8.72 0 0 0-5.9 2.29v-.93a.75.75 0 1 0-1.5 0V7.79a.72.72 0 0 0 .09.2V8a.72.72 0 0 0 .67.32h2.86a.75.75 0 1 0 0-1.5H6.58a7.24 7.24 0 1 1-1 9.21.75.75 0 0 0-1-.2.74.74 0 0 0-.2 1 8.75 8.75 0 1 0 7.26-13.58z\\\"></path><path fill=\\\"#fff\\\" d=\\\"M12.66 15.63v-3.1a1.22 1.22 0 0 1 .16-.62l2.26-4a.75.75 0 0 0-.29-1 .74.74 0 0 0-1 .28l-2.25 4a2.69 2.69 0 0 0-.36 1.36v3.1a.75.75 0 0 0 1.5 0z\\\"></path></svg>\"\n\n//# sourceURL=webpack://tykhe-electron-apps-baccarat/./src/renderer/baccarat/tykhe/bootstrap/assets/icons/history.svg?");

/***/ }),

/***/ "./src/renderer/baccarat/tykhe/bootstrap/assets/icons/home.svg":
/*!*********************************************************************!*\
  !*** ./src/renderer/baccarat/tykhe/bootstrap/assets/icons/home.svg ***!
  \*********************************************************************/
/***/ ((module) => {

eval("module.exports = \"<svg xmlns=\\\"http://www.w3.org/2000/svg\\\" viewBox=\\\"0 0 24 24\\\"><path fill=\\\"#fff\\\" d=\\\"M20.52 11.66l-8-8a.75.75 0 0 0-1.06 0l-8 8a.74.74 0 0 0 0 1.06A.75.75 0 0 0 4 13a.79.79 0 0 0 .53-.22l.47-.54v7.52a.75.75 0 0 0 .75.75h3.58a.76.76 0 0 0 .75-.75v-4.69H14v4.69a.75.75 0 0 0 .75.75h3.55a.76.76 0 0 0 .75-.75v-7.47l.43.43a.75.75 0 1 0 1.06-1.06zm-3 7.35h-2.04v-4.69a.76.76 0 0 0-.75-.75h-5.4a.75.75 0 0 0-.75.75V19h-2v-8.26L12 5.26l5.53 5.53z\\\"></path></svg>\"\n\n//# sourceURL=webpack://tykhe-electron-apps-baccarat/./src/renderer/baccarat/tykhe/bootstrap/assets/icons/home.svg?");

/***/ }),

/***/ "./src/renderer/baccarat/tykhe/bootstrap/assets/icons/info.svg":
/*!*********************************************************************!*\
  !*** ./src/renderer/baccarat/tykhe/bootstrap/assets/icons/info.svg ***!
  \*********************************************************************/
/***/ ((module) => {

eval("module.exports = \"<svg xmlns=\\\"http://www.w3.org/2000/svg\\\" viewBox=\\\"0 0 24 24\\\"><path fill=\\\"#fff\\\" d=\\\"M12 4a8.75 8.75 0 1 0 8.75 8.75A8.76 8.76 0 0 0 12 4zm0 16a7.25 7.25 0 1 1 7.25-7.25A7.26 7.26 0 0 1 12 20z\\\"></path><path fill=\\\"#fff\\\" d=\\\"M12 9.94a1.08 1.08 0 0 0 .8-.33 1.06 1.06 0 0 0 .32-.8A1.09 1.09 0 0 0 12.8 8a1.16 1.16 0 0 0-1.6 0 1.09 1.09 0 0 0-.32.8 1.06 1.06 0 0 0 .32.8 1.08 1.08 0 0 0 .8.34zM13.12 16.5h-.37v-4.84a.75.75 0 0 0-.75-.75h-1.12a.75.75 0 1 0 0 1.5h.37v4.09h-.37a.75.75 0 1 0 0 1.5h2.24a.75.75 0 1 0 0-1.5z\\\"></path></svg>\"\n\n//# sourceURL=webpack://tykhe-electron-apps-baccarat/./src/renderer/baccarat/tykhe/bootstrap/assets/icons/info.svg?");

/***/ }),

/***/ "./src/renderer/baccarat/tykhe/bootstrap/assets/icons/language.svg":
/*!*************************************************************************!*\
  !*** ./src/renderer/baccarat/tykhe/bootstrap/assets/icons/language.svg ***!
  \*************************************************************************/
/***/ ((module) => {

eval("module.exports = \"<svg xmlns=\\\"http://www.w3.org/2000/svg\\\" viewBox=\\\"0 0 24 24\\\"><path fill=\\\"#fff\\\" d=\\\"M12 3.25A8.75 8.75 0 1 0 20.75 12 8.77 8.77 0 0 0 12 3.25zm0 16c-.23-.09-.62-.78-1-2.07h1.88a4 4 0 0 1-.88 2.07zm0-14.5c.23.09.62.78 1 2.07h-1.94A4 4 0 0 1 12 4.75zm1.92.25a7.25 7.25 0 0 1 3.14 1.8h-2.58a9.52 9.52 0 0 0-.56-1.8zm-3.4 6.23a23.1 23.1 0 0 1 .25-2.93h2.46a23.1 23.1 0 0 1 .25 2.93zm3 1.5a23.1 23.1 0 0 1-.25 2.93h-2.5a23.1 23.1 0 0 1-.25-2.93zm-4-5.93H6.94A7.25 7.25 0 0 1 10.08 5a9.52 9.52 0 0 0-.56 1.82zm-.27 1.5A28.17 28.17 0 0 0 9 11.25H4.79a7.21 7.21 0 0 1 1-2.93zM9 12.75a28.17 28.17 0 0 0 .24 2.93H5.76a7.21 7.21 0 0 1-1-2.93zm.51 4.43a9.52 9.52 0 0 0 .56 1.8 7.25 7.25 0 0 1-3.14-1.8zm5 0h2.58A7.25 7.25 0 0 1 13.92 19a9.52 9.52 0 0 0 .56-1.82zm.27-1.5a28.17 28.17 0 0 0 .22-2.93h4.22a7.21 7.21 0 0 1-1 2.93zm.22-4.43a28.17 28.17 0 0 0-.24-2.93h3.49a7.21 7.21 0 0 1 1 2.93z\\\"></path></svg>\"\n\n//# sourceURL=webpack://tykhe-electron-apps-baccarat/./src/renderer/baccarat/tykhe/bootstrap/assets/icons/language.svg?");

/***/ }),

/***/ "./src/renderer/baccarat/tykhe/bootstrap/assets/icons/payouts.svg":
/*!************************************************************************!*\
  !*** ./src/renderer/baccarat/tykhe/bootstrap/assets/icons/payouts.svg ***!
  \************************************************************************/
/***/ ((module) => {

eval("module.exports = \"<svg xmlns=\\\"http://www.w3.org/2000/svg\\\" viewBox=\\\"0 0 24 24\\\"><path fill=\\\"#fff\\\" d=\\\"M7.5 11.75A4.25 4.25 0 1 0 3.25 7.5a4.26 4.26 0 0 0 4.25 4.25zm0-7A2.75 2.75 0 1 1 4.75 7.5 2.75 2.75 0 0 1 7.5 4.75zM16.5 12.25a4.25 4.25 0 1 0 4.25 4.25 4.26 4.26 0 0 0-4.25-4.25zm0 7a2.75 2.75 0 1 1 2.75-2.75 2.75 2.75 0 0 1-2.75 2.75zM20.53 3.47a.75.75 0 0 0-1.06 0l-16 16a.75.75 0 0 0 0 1.06.75.75 0 0 0 1.06 0l16-16a.75.75 0 0 0 0-1.06z\\\"></path></svg>\"\n\n//# sourceURL=webpack://tykhe-electron-apps-baccarat/./src/renderer/baccarat/tykhe/bootstrap/assets/icons/payouts.svg?");

/***/ }),

/***/ "./src/renderer/baccarat/tykhe/bootstrap/assets/icons/settings.svg":
/*!*************************************************************************!*\
  !*** ./src/renderer/baccarat/tykhe/bootstrap/assets/icons/settings.svg ***!
  \*************************************************************************/
/***/ ((module) => {

eval("module.exports = \"<svg xmlns=\\\"http://www.w3.org/2000/svg\\\" viewBox=\\\"0 0 24 24\\\"><path fill=\\\"#fff\\\" d=\\\"M19.43 9.73a9 9 0 0 0-.57-1.36 2.07 2.07 0 0 0-.2-1.92l-.92-1a1.92 1.92 0 0 0-2.09-.35 9.25 9.25 0 0 0-1.37-.58 1.91 1.91 0 0 0-1.76-1.32h-1a1.89 1.89 0 0 0-1.79 1.37 8.76 8.76 0 0 0-1.32.54 2 2 0 0 0-2 .14l-1 .91a1.92 1.92 0 0 0-.29 2.24 10.3 10.3 0 0 0-.55 1.32 1.91 1.91 0 0 0-1.32 1.76v1a1.89 1.89 0 0 0 1.32 1.75 9 9 0 0 0 .57 1.36 2 2 0 0 0 .21 1.92l.9.94a1.9 1.9 0 0 0 2.1.36 8.61 8.61 0 0 0 1.37.58 1.91 1.91 0 0 0 1.76 1.32h1a1.89 1.89 0 0 0 1.75-1.32 8.7 8.7 0 0 0 1.36-.57 2.07 2.07 0 0 0 1.92-.2l.94-.91a1.9 1.9 0 0 0 .36-2.1 9.65 9.65 0 0 0 .58-1.37 1.91 1.91 0 0 0 1.32-1.76v-1a1.89 1.89 0 0 0-1.28-1.75zm-.18 2.79a.41.41 0 0 1-.4.34h-.56l-.16.54a7.62 7.62 0 0 1-.77 1.84l-.22.47.27.41a.48.48 0 0 1 0 .56l-.77.76a.48.48 0 0 1-.51 0l-.43-.4-.49.32a7.32 7.32 0 0 1-1.84.77l-.54.21v.57a.4.4 0 0 1-.34.34h-1a.41.41 0 0 1-.34-.4v-.57l-.54-.15a7.79 7.79 0 0 1-1.84-.77l-.47-.22-.41.27a.5.5 0 0 1-.56 0l-.76-.77a.48.48 0 0 1 0-.51l.4-.43-.32-.49a7.79 7.79 0 0 1-.77-1.84l-.21-.54h-.58a.4.4 0 0 1-.34-.34v-1a.41.41 0 0 1 .4-.34h.56l.16-.54a7.32 7.32 0 0 1 .77-1.84L7 8.28l-.47-.53a.46.46 0 0 1 0-.52l.77-.76a.48.48 0 0 1 .52 0l.49.5.51-.33a7.79 7.79 0 0 1 1.84-.77l.54-.21v-.57a.4.4 0 0 1 .34-.34h1a.41.41 0 0 1 .34.4v.57l.54.15a7.32 7.32 0 0 1 1.84.77l.47.22.41-.27a.5.5 0 0 1 .56 0l.76.77a.48.48 0 0 1 0 .51l-.4.43.32.49a7.79 7.79 0 0 1 .77 1.84l.21.54h.57a.4.4 0 0 1 .34.34z\\\"></path><path fill=\\\"#fff\\\" d=\\\"M12 9.35A2.65 2.65 0 1 0 14.65 12 2.65 2.65 0 0 0 12 9.35zm0 3.8A1.15 1.15 0 1 1 13.15 12 1.16 1.16 0 0 1 12 13.15z\\\"></path></svg>\"\n\n//# sourceURL=webpack://tykhe-electron-apps-baccarat/./src/renderer/baccarat/tykhe/bootstrap/assets/icons/settings.svg?");

/***/ }),

/***/ "./src/renderer/baccarat/tykhe/bootstrap/assets/icons/statistics.svg":
/*!***************************************************************************!*\
  !*** ./src/renderer/baccarat/tykhe/bootstrap/assets/icons/statistics.svg ***!
  \***************************************************************************/
/***/ ((module) => {

eval("module.exports = \"<svg xmlns=\\\"http://www.w3.org/2000/svg\\\" viewBox=\\\"0 0 24 24\\\"><path fill=\\\"#fff\\\" d=\\\"M20.29 11.82a1.78 1.78 0 0 0-1.33-.6H14A1.26 1.26 0 0 1 12.78 10V5a1.78 1.78 0 0 0-.6-1.33 1.73 1.73 0 0 0-1.37-.41 8.76 8.76 0 0 0-.52 17.25 9.43 9.43 0 0 0 1.75.17 8.68 8.68 0 0 0 5.07-1.63 8.78 8.78 0 0 0 3.59-5.85 1.73 1.73 0 0 0-.41-1.38zm-4.05 6.05a7.28 7.28 0 0 1-5.67 1.2A7.25 7.25 0 0 1 11 4.79a.26.26 0 0 1 .14.06.27.27 0 0 1 .09.2V10A2.76 2.76 0 0 0 14 12.72h5a.27.27 0 0 1 .2.09.22.22 0 0 1 .05.18 7.23 7.23 0 0 1-3.01 4.88z\\\"></path><path fill=\\\"#fff\\\" d=\\\"M14.88 10.38h4a1.38 1.38 0 0 0 1.22-.53 1 1 0 0 0 .11-.94 8.73 8.73 0 0 0-5.15-5.15 1.09 1.09 0 0 0-1 .13 1.07 1.07 0 0 0-.46.89v4.35a1.27 1.27 0 0 0 .4.87 1.28 1.28 0 0 0 .88.38zm.25-5a7.2 7.2 0 0 1 3.46 3.47h-3.46z\\\"></path></svg>\"\n\n//# sourceURL=webpack://tykhe-electron-apps-baccarat/./src/renderer/baccarat/tykhe/bootstrap/assets/icons/statistics.svg?");

/***/ }),

/***/ "./src/renderer/baccarat/tykhe/bootstrap/assets/icons/support.svg":
/*!************************************************************************!*\
  !*** ./src/renderer/baccarat/tykhe/bootstrap/assets/icons/support.svg ***!
  \************************************************************************/
/***/ ((module) => {

eval("module.exports = \"<svg xmlns=\\\"http://www.w3.org/2000/svg\\\" viewBox=\\\"0 0 24 24\\\"><path fill=\\\"#fff\\\" d=\\\"M21 13.32a2.4 2.4 0 0 0-.65-1.84A2 2 0 0 0 19 9.74a6.94 6.94 0 0 0-13.82-.05 2.08 2.08 0 0 0-1.49 1.76A2.31 2.31 0 0 0 3 13.32a2.3 2.3 0 0 0 .69 1.87A2.08 2.08 0 0 0 5.75 17H6a7 7 0 0 0 6.13 3.7.75.75 0 1 0 0-1.5 5.48 5.48 0 0 1-4.8-2.9 2.05 2.05 0 0 0 .49-1.3v-3.32a2.08 2.08 0 0 0-1.15-1.85 5.44 5.44 0 0 1 10.85-.07 2.07 2.07 0 0 0-1.3 1.92V15a2.07 2.07 0 0 0 4.13.2 2.38 2.38 0 0 0 .65-1.88zM5.17 15v-3.32a.58.58 0 1 1 1.15 0V15a.58.58 0 1 1-1.15 0zm13.71 0a.58.58 0 0 1-1.16 0v-3.32a.58.58 0 0 1 1.16 0z\\\"></path></svg>\"\n\n//# sourceURL=webpack://tykhe-electron-apps-baccarat/./src/renderer/baccarat/tykhe/bootstrap/assets/icons/support.svg?");

/***/ }),

/***/ "./src/renderer/baccarat/tykhe/bootstrap/assets/icons/tips.svg":
/*!*********************************************************************!*\
  !*** ./src/renderer/baccarat/tykhe/bootstrap/assets/icons/tips.svg ***!
  \*********************************************************************/
/***/ ((module) => {

eval("module.exports = \"<svg viewBox=\\\"0 0 24 24\\\" xmlns=\\\"http://www.w3.org/2000/svg\\\"><path fill=\\\"#010101\\\" d=\\\"M 12.671 5.74 L 14.346 5.74 L 14.958 4.945 L 8.669 4.945 L 8.056 5.74 L 9.117 5.74 C 10.221 5.74 11.242 5.809 11.65 6.603 L 8.669 6.603 L 8.056 7.399 L 11.773 7.399 L 11.773 7.433 C 11.773 8.02 11.201 8.918 9.322 8.918 L 8.383 8.918 L 8.383 9.678 L 12.059 13.583 L 13.733 13.583 L 9.894 9.54 C 11.487 9.471 12.957 8.711 13.162 7.399 L 14.346 7.399 L 14.958 6.603 L 13.121 6.603 C 13.08 6.293 12.916 5.981 12.671 5.74 Z\\\" style></path><path d=\\\"M 11.965 1.751 C 5.969 1.735 2.204 8.217 5.189 13.418 C 5.637 14.2 6.218 14.898 6.905 15.481 L 9.815 15.481 C 5.265 13.826 4.213 7.865 7.921 4.752 C 11.63 1.639 17.318 3.708 18.16 8.476 C 18.69 11.483 16.985 14.437 14.115 15.481 L 17.035 15.481 C 21.608 11.601 20.266 4.226 14.62 2.206 C 13.768 1.901 12.87 1.747 11.965 1.751 Z\\\"></path><path d=\\\"M12.5,9.85a.51.51,0,0,0-.12-.37A1.31,1.31,0,0,0,12,9.21a3.47,3.47,0,0,1-1.06-.58,1.12,1.12,0,0,1-.33-.87,1.12,1.12,0,0,1,.35-.85,1.5,1.5,0,0,1,.93-.4V5.87h.49v.66a1.37,1.37,0,0,1,.88.45,1.43,1.43,0,0,1,.31.95V8h-1a.85.85,0,0,0-.13-.5A.42.42,0,0,0,12,7.29a.38.38,0,0,0-.32.13.56.56,0,0,0-.1.35.53.53,0,0,0,.11.34,1.16,1.16,0,0,0,.42.27,4,4,0,0,1,1,.6,1.12,1.12,0,0,1,.33.87,1.1,1.1,0,0,1-.34.85,1.44,1.44,0,0,1-.9.39v.61H11.8v-.62a1.64,1.64,0,0,1-1-.41,1.35,1.35,0,0,1-.38-1.05v0h1a.75.75,0,0,0,.17.55.58.58,0,0,0,.43.17.42.42,0,0,0,.33-.13A.49.49,0,0,0,12.5,9.85Z\\\"></path></svg>\"\n\n//# sourceURL=webpack://tykhe-electron-apps-baccarat/./src/renderer/baccarat/tykhe/bootstrap/assets/icons/tips.svg?");

/***/ })

}]);